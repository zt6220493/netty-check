package com.ecar.eoc.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.ecar.eoc.utils.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.entity.eoc.EocTripSpy;
import com.ecar.eoc.entity.eoc.MsgTerminal;
import com.ecar.eoc.entity.eoc.SimInfo;
import com.ecar.eoc.entity.gps.Point;
import com.ecar.eoc.entity.report.PushDayData;
import com.ecar.eoc.entity.report.PushWeekData;
import com.ecar.eoc.entity.report.UbiOneTrip;
import com.ecar.eoc.entity.trip.EocGps;
import com.ecar.eoc.entity.vo.BaiduAddrVO;
import com.ecar.eoc.entity.vo.request.CommonReqVO;
import com.ecar.eoc.entity.vo.request.ReportListReqVO;
import com.ecar.eoc.entity.vo.request.TripReportReqVO;
import com.ecar.eoc.entity.vo.request.WeekReportReqVO;
import com.ecar.eoc.entity.vo.request.WeekWechatReqVO;
import com.ecar.eoc.entity.vo.response.LastWeekScoreVO;
import com.ecar.eoc.entity.vo.response.PushWeekDataResVO;
import com.ecar.eoc.entity.vo.response.TripReportDtlVO;
import com.ecar.eoc.entity.vo.response.TripReportGpsResVO;
import com.ecar.eoc.entity.vo.response.TripReportGpsVO;
import com.ecar.eoc.entity.vo.response.TripReportVO;
import com.ecar.eoc.entity.vo.response.TripResPageVO;
import com.ecar.eoc.entity.vo.response.UbiWeekReportResVO;
import com.ecar.eoc.entity.vo.response.WeekReportDtlVO;
import com.ecar.eoc.entity.vo.response.WeekReportVO;
import com.ecar.eoc.entity.vo.response.WeekResPageVO;
import com.ecar.eoc.mapper.eocdb.eoc.TripMapper;
import com.ecar.eoc.mapper.mongo.DayDataMongoDAO;
import com.ecar.eoc.mapper.mongo.GpsMongoDAO;
import com.ecar.eoc.mapper.mongo.TripReportMongoDAO;
import com.ecar.eoc.mapper.mongo.WeekReportMongoDAO;
import com.ecar.eoc.mapper.simdb.SimMapper;

@Service
public class ReportService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private TripReportMongoDAO tripReportMongoDAO;
	@Autowired
	private WeekReportMongoDAO weekReportMongoDAO;
	@Autowired
    private Environment env;	//配置文件
	@Autowired
	private GpsMongoDAO gpsMongoDAO;
	@Autowired
	private TripMapper tripMapper;
	@Autowired
	private DayDataMongoDAO dayDataMongoDAO;
	@Autowired
	private SimMapper simMapper;
	
	private static String[] strArray = new String[]{"炉火纯青","得心应手","驾熟就轻","出神入化","技艺超群","技术精湛","操作娴熟","穿梭自如","灵活稳重","稳稳当当"};
	
	/**
	 * 里程报告列表查询
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> tripReportlist(ReportListReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		MsgTerminal terminal = terminalService.getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		//计算查询起始时间
		Date date  = new Date();
		String startTime = DateUtil.formatDate(DateUtil.getNumDateBefore(date, -56), "yyyy/MM/dd HH:mm:ss");
		//总数据条数
		int totalCount = tripReportMongoDAO.countPageList(terminalId,startTime);
		TripResPageVO data = new TripResPageVO();
		if(totalCount==0) {
			data.setPageSize(0);
			data.setPageIndex(0);
			data.setPageNum(0);
			data.setTotalCount(0);
			result.setData(data);
			return result;
		}
		int pageSize = requestVO.getPageSize().intValue();
		int pageNum = (totalCount%pageSize==0?0:1) + totalCount/pageSize;
		if(requestVO.getPageIndex().intValue()>=pageNum) {
			result.setErrorCode("200010");
			result.setErrorMessage("查询条件分页下标越界");
			result.setSuccess("false");
			return result;
		}
		data.setPageIndex(requestVO.getPageIndex());
		data.setPageNum(pageNum);
		data.setTotalCount(totalCount);
		data.setPageSize(pageSize);
		List<UbiOneTrip> list = tripReportMongoDAO.queryPageList(requestVO.getPageSize(), 
				requestVO.getPageIndex(), terminalId, startTime);
		//查询的数据为空，返回空结果
		if(null==list) {
			result.setData(data);
			return result;
		}
		List<TripReportVO> dataList = new ArrayList<>();
		for(UbiOneTrip obj:list) {
			TripReportVO tripReport = new TripReportVO();
			tripReport.setTerminalId(obj.getTerminalId());
			tripReport.setTripCode(obj.getTripCode());
			tripReport.setStartLat(obj.getStartLat().doubleValue()+"");
			tripReport.setStartLon(obj.getStartLon().doubleValue()+"");
			try {
//				double[] gpsArrStart = CoordinateConversion.gps84_To_Gcj02(obj.getStartLat().doubleValue(), obj.getStartLon().doubleValue());
//				String addressStart = GaoDeMapUtil.getAddrStrByGps(gpsArrStart[0], gpsArrStart[1]);
				String addressStart = AddressUtil.getBaiduAddrByGps(obj.getStartLat().doubleValue(), obj.getStartLon().doubleValue()).getAddressName();
				tripReport.setStartAddr(addressStart);
			} catch (Exception e) {
				logger.error("tripReportlist get address error,terminalId:"+terminalId);
			}
			tripReport.setStartTime(obj.getStartTime());
			tripReport.setEndLat(obj.getEndLat().doubleValue()+"");
			tripReport.setEndLon(obj.getEndLon().doubleValue()+"");
			tripReport.setEndTime(obj.getEndTime());
			try {
//				double[] gpsArrEnd = CoordinateConversion.gps84_To_Gcj02(obj.getEndLat().doubleValue(), obj.getEndLon().doubleValue());
//				String addressEnd = GaoDeMapUtil.getAddrStrByGps(gpsArrEnd[0], gpsArrEnd[1]);
				
				String addressEnd = AddressUtil.getBaiduAddrByGps(obj.getEndLat().doubleValue(), obj.getEndLon().doubleValue()).getAddressName();
				tripReport.setEndAddr(addressEnd);
			} catch (Exception e) {
				logger.error("tripReportlist get address error,terminalId:"+terminalId);
			}
			tripReport.setMileage(obj.getMileage().floatValue()+"");
			tripReport.setMaxSpeed(obj.getMaxSpeed().floatValue()+"");
			tripReport.setAvgSpeed(obj.getAvgSpeed().floatValue()+"");
			
			if(obj.getTotalTime()<6) {
				tripReport.setDuration("0.1");
			}else {
				int totalTime = obj.getTotalTime();
				double totalTimeH = totalTime*1.0/60;
				totalTimeH = (double)Math.round(totalTimeH*10)/10;
				
				tripReport.setDuration(totalTimeH+"");
			}
			dataList.add(tripReport);
		}
		data.setList(dataList);
		result.setData(data);
		return result;
	}
	
	/**
	 * 周报列表查询
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> weekReportlist(ReportListReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		MsgTerminal terminal = terminalService.getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		//计算查询起始时间
		Date date  = new Date();
		String startTime = DateUtil.formatDate(DateUtil.getNumDateBefore(date, -56), "yyyy-MM-dd");
		String endTime = DateUtil.formatDate(DateUtil.getWeekStartDate(new Date()), "yyyy-MM-dd");
		//总数据条数
		int totalCount = weekReportMongoDAO.countPageList(terminalId,startTime,endTime);
		WeekResPageVO data = new WeekResPageVO();
		if(totalCount==0) {
			data.setPageSize(0);
			data.setPageIndex(0);
			data.setPageNum(0);
			data.setTotalCount(0);
			result.setData(data);
			return result;
		}
		int pageSize = requestVO.getPageSize().intValue();
		int pageNum = (totalCount%pageSize==0?0:1) + totalCount/pageSize;
		if(requestVO.getPageIndex().intValue()>=pageNum) {
			result.setErrorCode("200010");
			result.setErrorMessage("查询条件分页下标越界");
			result.setSuccess("false");
			return result;
		}
		data.setPageIndex(requestVO.getPageIndex());
		data.setPageNum(pageNum);
		data.setTotalCount(totalCount);
		data.setPageSize(pageSize);
		List<PushWeekData> list = weekReportMongoDAO.queryPageList(requestVO.getPageSize(), 
				requestVO.getPageIndex(), terminalId, startTime,endTime);
		//查询的数据为空，返回空结果
		if(null==list) {
			result.setData(data);
			return result;
		}
		List<WeekReportVO> dataList = new ArrayList<WeekReportVO>();
		for(PushWeekData obj:list) {
			WeekReportVO weekReport = new WeekReportVO();
			weekReport.setTerminalId(obj.getTerminalId());
			weekReport.setStartDate(obj.getStartdate());
			weekReport.setScore(obj.getScore());
			weekReport.setMileage(obj.getMileage()+"");
			double duration = Double.valueOf(obj.getDuration()).doubleValue()/60;
			duration  = (double)Math.round(duration*10)/10;
			weekReport.setDuration(duration+"");
			weekReport.setAvgSpeed(obj.getAvgSpeed()+"");
			//时间展示
			String startDateStr =  DateUtil.formatDate(DateUtil.toDate(obj.getStartdate(), "yyyy-MM-dd"), "MM月dd日");
			String endDateStr = DateUtil.formatDate(DateUtil.toDate(obj.getEnddate(), "yyyy-MM-dd"), "MM月dd日");
			weekReport.setTimeStr(startDateStr+"-"+endDateStr);
			dataList.add(weekReport);
		}
		data.setList(dataList);
		result.setData(data);
		return result;
	}
	
	/**
	 * 删除里程报告
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> deleteTripReport(TripReportReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		tripReportMongoDAO.updateUbiOneTripForDelete(requestVO.getTripCode());
		return result;
	}
	/**
	 * 删除周报记录
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> deleteWeekReport(WeekReportReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		int terminalId = requestVO.getTerminalId().intValue();
		weekReportMongoDAO.updateWeekReportForDelete(terminalId, requestVO.getStartDate());
		return result;
	}
	
	/**
	 * 里程报告详情查询
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> queryTripReport(TripReportReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		UbiOneTrip oneTrip = tripReportMongoDAO.queryByTripCode(requestVO.getTripCode());
		if(null==oneTrip) {
			result.setSuccess("false");
			result.setErrorCode("30040");
			result.setErrorMessage("里程报告记录不存在");
			return result;
		}
		//记录被删除，不予查询
		if(oneTrip.getDelete()!=null && oneTrip.getDelete().intValue()==1) {
			result.setSuccess("false");
			result.setErrorCode("30041");
			result.setErrorMessage("里程报告记录不存在或者被删除");
			return result;
		}
		TripReportDtlVO dtl = new TripReportDtlVO();
		dtl.setScore(oneTrip.getScore());
		dtl.setScoreTag(LabelUtil.labelContorl(Integer.valueOf(oneTrip.getScore())));
		dtl.setMileage(oneTrip.getMileage()+"");
		boolean startAddrFlag = false; //是否更新
		boolean endAddrFlag = false;
		try {
			if(StringUtils.isEmpty(oneTrip.getEndAdress())) {
				String endAddress = AddressUtil.getBaiduAddrByGps(oneTrip.getEndLat(), oneTrip.getEndLon()).getAddressName();
				if(!StringUtils.isEmpty(endAddress)) {
					dtl.setEndAdress(endAddress);
					oneTrip.setEndAdress(endAddress);
					endAddrFlag = true;
				}
			}
		} catch (Exception e) {
			logger.warn("queryTripReport getAddrStrByGps error,cause by {}",e);
		}
		int totalTime = oneTrip.getTotalTime();
		if(totalTime<6) {
			dtl.setTotalTime("0.1");
		}else {
			double hours = totalTime*1.0/60;
			hours = (double)Math.round(hours*10)/10;
			dtl.setTotalTime(hours+"");
		}
		dtl.setStartTime(oneTrip.getStartTime());
		dtl.setEndTime(oneTrip.getEndTime());
		dtl.setMaxSpeed(oneTrip.getMaxSpeed().floatValue()+"");
		dtl.setAvgSpeed(oneTrip.getAvgSpeed().floatValue()+"");
		dtl.setAcceTimes(oneTrip.getAcceTimes());
		dtl.setDeceTimes(oneTrip.getDeceTimes());
		dtl.setTurnTimes(oneTrip.getTurnTimes());
		dtl.setOverSpeedTimes(oneTrip.getOverSpeedTimes());
		dtl.setTiredDrivingTimes(oneTrip.getTiredDrivingTimes());
		//计算排名百分比
		//排名城市未计算出，则此处开始原先的计算逻辑
    	if(StringUtils.isEmpty(oneTrip.getRankingCity())){
    		BaiduAddrVO startBDAddr = AddressUtil.getBaiduAddrByGps(oneTrip.getStartLat(), oneTrip.getStartLon());
    		if(null!=startBDAddr){
    			if(!StringUtils.isEmpty(startBDAddr.getAddressName())){
    				dtl.setStartAdress(startBDAddr.getAddressName());
    				oneTrip.setStartAdress(startBDAddr.getAddressName());
    			}
    			//行程报告中城市或者城市编码非空，则需要对用户进行排名计算
    			if(!StringUtils.isEmpty(startBDAddr.getCityCode())){
    				oneTrip.setCityCode(Integer.valueOf(startBDAddr.getCityCode()));
    				oneTrip.setRankingCity(startBDAddr.getCity());
    				String startRankTime = DateUtil.getStartRankTime(oneTrip.getStartTime());
    				String endRankTime = DateUtil.getEndRankTime(oneTrip.getStartTime());
    				long totalCount = tripReportMongoDAO.getRankingTotalCount(oneTrip.getCityCode(),startRankTime, endRankTime);
    				if(0==totalCount) {
    					oneTrip.setPreRanking(99);
    					dtl.setRankingPercent("99%");
    					dtl.setRankingTrend(1);
    				}else {
    					long currentRankingCount = tripReportMongoDAO.getCurrentRanking(oneTrip.getTerminalId(),startRankTime, endRankTime, oneTrip.getRanking());
    					int percent = 100 - (int)(currentRankingCount*100/totalCount);
    					oneTrip.setPreRanking(percent);
    					if(oneTrip.getPreRanking()!=percent || !startBDAddr.getCity().equals(oneTrip.getRankingCity()) 
    							|| !startBDAddr.getCityCode().equals(oneTrip.getCityCode()+"")) {
    						tripReportMongoDAO.updateRanking(oneTrip.getTripCode(), startBDAddr.getCity(), startBDAddr.getCityCode(),dtl.getStartAdress(),
    								dtl.getEndAdress(), percent);
    					}
    					dtl.setRankingCity(startBDAddr.getCity());
    					dtl.setRankingPercent(percent+"%");
        				int judge = percent-oneTrip.getPreRanking();
        				if(judge>=0) {
        					dtl.setRankingTrend(1);
        				}else {
        					dtl.setRankingTrend(0);
        				}
    				}
        		}
    			dtl.setRankingCity(startBDAddr.getCity());
    		}
        }else {
        	if(StringUtils.isEmpty(oneTrip.getStartAdress())) {
        		BaiduAddrVO startBDAddr = AddressUtil.getBaiduAddrByGps(oneTrip.getStartLat(), oneTrip.getStartLon());
        		if(null!=startBDAddr) {
        			dtl.setStartAdress(startBDAddr.getAddressName());
        		}
        	}
        	String startRankTime = DateUtil.getStartRankTime(oneTrip.getStartTime());
			String endRankTime = DateUtil.getEndRankTime(oneTrip.getStartTime());
    		long totalCount = tripReportMongoDAO.getRankingTotalCount(oneTrip.getTerminalId(),startRankTime, endRankTime);
    		if(0==totalCount) {
				oneTrip.setPreRanking(99);
				dtl.setRankingPercent("99%");
				dtl.setRankingTrend(1);
			}else {
				long currentRankingCount = tripReportMongoDAO.getCurrentRanking(oneTrip.getTerminalId(),startRankTime, endRankTime, oneTrip.getRanking());
				int percent = 100 - (int)(currentRankingCount*100/totalCount);
				oneTrip.setPreRanking(percent);
				if(oneTrip.getPreRanking()!=percent) {
					String addressStart = null;
					String addressEnd = null;
					if(startAddrFlag) {
						addressStart = dtl.getStartAdress();
					}
					if(endAddrFlag) {
						addressEnd = dtl.getEndAdress();
					}
					tripReportMongoDAO.updateRanking(oneTrip.getTripCode(), null, null,addressStart,addressEnd, percent);
				}
				dtl.setRankingCity(oneTrip.getRankingCity());
				dtl.setRankingPercent(percent+"%");
				int judge = percent-oneTrip.getPreRanking();
				if(judge>=0) {
					dtl.setRankingTrend(1);
				}else {
					dtl.setRankingTrend(0);
				}
			}
        }
    	if(oneTrip.getCongestionDura()==0) {
    		dtl.setCongestionDura(oneTrip.getCongestionDura()+"");
    	}
    	if(oneTrip.getCongestionDura()<6) {
			dtl.setCongestionDura("0.1");
		}else {
			double congest = oneTrip.getCongestionDura()*1.0/60;
			congest = (double)Math.round(congest*10)/10;
			dtl.setCongestionDura(congest+"");
		}
    	dtl.setControlScore(oneTrip.getControlScore());
    	dtl.setEconomyScore(oneTrip.getEconomyScore());
    	dtl.setFocusScore(oneTrip.getFocusScore());
    	dtl.setRoadScore(oneTrip.getRoadScore());
    	dtl.setEnvirScore(oneTrip.getEnvirScore());
		result.setData(dtl);
		return result;
	}
	
	/**
	 * 里程报告GPS查询
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> tripReportGps(TripReportReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		Date startTime = DateUtil.toDate(requestVO.getStartTime(), "yyyy/MM/dd HH:mm:ss");  //行程开始时间转换为Date类型
		Date endTime = DateUtil.toDate(requestVO.getEndTime(), "yyyy/MM/dd HH:mm:ss");  //行程结束时间转换为Date类型
		List<EocGps> gpsList = new ArrayList<EocGps>();
		if("false".equals(env.getProperty("ecar.iot.IS_PRODUCT_ENVIRONMENT"))) {
			gpsList = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), startTime, endTime, "eocGps");
		}else {
			Date currentDate=new Date();  //当前时间
			int daysBetween = DateUtil.getDaysBetween(startTime,endTime);  //行程开始时间和行程结束时间对年月日判断相差几日
			int startBetween = DateUtil.getDaysBetween(startTime,currentDate);  //行程开始时间和当前时间对年月日判断相差几日
			//判断行程开始时间和当前时间相差多少天，超过10天则去腾讯云上面读GPS数据
			if(startBetween>9) {
				//对象存储查询GPS
				gpsList= readCloud(requestVO.getTerminalId(),startTime,gpsList);
			}else {
				//高速mongo拉取数据
				if (daysBetween==0) {  //为同一天
					//读mongodb
					long subTime=(endTime.getTime()-startTime.getTime())/1000;
					int subHour=(int)((subTime-1)/3600+1);
					if (subTime<=4200) {  //小于1个小时10分钟，就一次性全查出来
						gpsList = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), startTime, endTime, getCollection(startTime));
					} else {
						//按照一个小时一个小时的查询，放到一个list中返回
						for (int i=1;i<=subHour;i++) {
							Date afterDate = getAfterDate(startTime,i);
							Date beforeDate = getAfterDate(startTime,i-1);
							List<EocGps> list = null;
							if (i==1) {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), startTime, afterDate, getCollection(startTime));
							} else if (afterDate.getTime()>endTime.getTime()) {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), beforeDate, endTime, getCollection(beforeDate));
							} else {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), beforeDate, afterDate, getCollection(beforeDate));
							}
							//查询GPS数据
							if(null!=list && list.size()>0) {
								gpsList.addAll(list);
							}
						}
					}
				}else{
					//将结束时间的时分秒去掉
					Date date = DateUtil.removeHMS(endTime);
					long time = date.getTime();  //去掉时分秒后的毫秒值
					//第一天开始时间和去掉时分秒的时间做毫秒值相差计算
					long subStartTime=(time-startTime.getTime())/1000;
					int subStartHour=(int)((subStartTime-1)/3600+1);
					List<EocGps> startList =new ArrayList<EocGps>();
					if (subStartTime<=4200) {  //小于1个小时10分钟，就一次性全查出来
						startList = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), startTime, date, getCollection(startTime));
					}else{
						//按照一个小时一个小时的查询，放到一个list中返回
						for (int i=1;i<=subStartHour;i++) {
							Date afterDate = getAfterDate(startTime,i);
							Date beforeDate = getAfterDate(startTime,i-1);
							List<EocGps> list = null;
							if (i==1) {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), startTime, afterDate, getCollection(startTime));
							} else if (afterDate.getTime()>time) {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), beforeDate, date, getCollection(beforeDate));
							} else {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), beforeDate, afterDate, getCollection(beforeDate));
							}
							//查询GPS数据
							if(null!=list && list.size()>0) {
								startList.addAll(list);
							}
						}
					}
					if(startList!=null && startList.size()>0) {
						gpsList.addAll(startList);
					}
					//第二天结束时间和去掉时分秒的时间做毫秒值相差计算
					long subEndTime=(endTime.getTime()-time)/1000;  
					int subEndHour=(int)((subEndTime-1)/3600+1);
					List<EocGps> endResult =new ArrayList<EocGps>();
					if (subEndTime<=4200) {  //小于1个小时10分钟，就一次性全查出来
						endResult = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), date, endTime, getCollection(startTime));
					} else {
						//按照一个小时一个小时的查询，放到一个list中返回
						for (int i=1;i<=subEndHour;i++) {
							Date afterDate = getAfterDate(date,i);
							Date beforeDate = getAfterDate(date,i-1);
							List<EocGps> list = null;
							if (i==1) {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), date, afterDate, getCollection(date));
							} else if (afterDate.getTime()>endTime.getTime()) {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), beforeDate, endTime, getCollection(beforeDate));
							} else {
								list = gpsMongoDAO.queryGpsByTerTime(requestVO.getTerminalId(), beforeDate, afterDate, getCollection(beforeDate));
							}
							//查询GPS数据
							if(null!=list && list.size()>0) {
								endResult.addAll(list);
							}
						}
					}
					if(endResult!=null && endResult.size()>0) {
						gpsList.addAll(endResult);
					}
				}
			}
		}
		
		if (gpsList.size()>0) {
			//将集合去重(由于放在set集合中去重的，导致顺利乱了)
			gpsList = removeDuplicate(gpsList);
			//对集合进行时间排序
			Collections.sort(gpsList);
			//将查询出来的GPS点进行过滤
			List<EocGps> gpsFilter = gpsFilter(gpsList);
			gpsList = gpsTimeFilter(gpsFilter);
		}
		TripReportGpsResVO data = new TripReportGpsResVO();
		if(gpsList!=null && gpsList.size()>0) {
			List<TripReportGpsVO> gpsVOList = new ArrayList<TripReportGpsVO>(); 
			for(EocGps obj:gpsList) {
				TripReportGpsVO gpsVO = new TripReportGpsVO();
				Point point = CoordinateConversion.wgs_gcj_encrypts(obj.getLat(), obj.getLon());
				//step3:转换成百度地图展示的GPS点
				Point p1 = CoordinateConversion.google_bd_encrypt(point.getLat(), point.getLon());
				gpsVO.setGpsType(0);
				gpsVO.setLat(p1.getLat());
				gpsVO.setLon(p1.getLon());
				gpsVO.setTime(obj.getTime());
				gpsVOList.add(gpsVO);
			}
			data.setGpsList(gpsVOList);
		}
		data.setTerminalId(requestVO.getTerminalId());
		data.setStartTime(requestVO.getStartTime());
		data.setEndTime(requestVO.getEndTime());
		result.setData(data);
		return result;
	}
	
	/**
	 *  周报详情
	 * @param requestVO
	 * @return
	 * @throws ParseException 
	 */
	public BaseInfo<Object> weekReportDtl(WeekReportReqVO requestVO) throws ParseException {
		BaseInfo<Object> result = new BaseInfo<Object>();
		String startDate = requestVO.getStartDate();
		Date startTime = DateUtil.toDate(startDate, "yyyy-MM-dd");
		String beforeDate = DateUtil.formatDate(DateUtil.getNumDateBefore(startTime, -7), "yyyy-MM-dd");
//		List<String> dateStr = DateUtil.getWeekDate_US_UP(beforeDate);
		PushWeekData startWeekReport = weekReportMongoDAO.queryByTerDate(requestVO.getTerminalId(), startDate);
		if(null==startWeekReport) {
			result.setErrorCode("440012");
			result.setErrorMessage("无周报数据记录");
			return result;
		}
		PushWeekData beforeWeekReport = weekReportMongoDAO.queryByTerDate(requestVO.getTerminalId(), beforeDate);
		WeekReportDtlVO data = new WeekReportDtlVO();
		data.setTerminalId(requestVO.getTerminalId());
		data.setScore(startWeekReport.getScore());
		data.setStartDate(startDate);
		data.setEndDate(startWeekReport.getEnddate());
		data.setDrivers(Integer.valueOf(startWeekReport.getTravels()));
		float mileage =  Float.valueOf(startWeekReport.getMileage());
		data.setMileage(mileage);
		float duration = Float.valueOf(startWeekReport.getDuration())/60;
		duration = (float)(Math.round(duration*10))/10;
		data.setDuration(duration);
		Float maxMileage = null;
		if(!StringUtils.isEmpty(startWeekReport.getMaxMileage())) {
			maxMileage = Float.valueOf(startWeekReport.getMaxMileage());
			data.setMaxMileage(maxMileage);
		}
		float maxSpeed = Float.valueOf(startWeekReport.getMaxSpeed());
		data.setMaxSpeed(maxSpeed);
		if(null!=startWeekReport.getPec()) {
			int pec = startWeekReport.getPec();
			data.setPeccancy(pec);
		}
		int acc = Integer.valueOf(StringUtils.isEmpty(startWeekReport.getAcce())?"0":startWeekReport.getAcce());
		data.setAcce(acc);
		int brake = Integer.valueOf(StringUtils.isEmpty(startWeekReport.getBrake())?"0":startWeekReport.getBrake());
		data.setBrake(brake);
		int turn = Integer.valueOf(StringUtils.isEmpty(startWeekReport.getTurn())?"0":startWeekReport.getTurn());
		data.setTurn(turn);
		//上周的周报记录为空
		if(null==beforeWeekReport) {
			data.setMileageTrend(mileage);
			data.setDurationTrend(duration);
			if(null!=maxMileage) {
				data.setMaxMileageTrend(maxMileage);
			}
			data.setMaxSpeedTrend(maxSpeed);
			if(null!=startWeekReport.getPec()) {
				int pec = startWeekReport.getPec();
				data.setPeccancyTrend(pec);
			}
			data.setAcceTrend(acc);
			data.setBrakeTrend(brake);
			data.setTurnTrend(turn);
		}else {
			float mileageOld = Float.valueOf(beforeWeekReport.getMileage());
			float mileageTrend = mileage-mileageOld;
			mileageTrend = (float)(Math.round(mileageTrend*10))/10;
			data.setMileageTrend(mileageTrend);
			float durationOld = Float.valueOf(beforeWeekReport.getDuration())/60;
			float durationTrend = duration-durationOld;
			durationTrend = (float)(Math.round(durationTrend*10))/10;
			data.setDurationTrend(durationTrend);
			if(null!=maxMileage) {
				if(!StringUtils.isEmpty(beforeWeekReport.getMaxMileage())) {
					float maxMileageOld = Float.valueOf(beforeWeekReport.getMaxMileage());
					float maxMileageTrend = maxMileage.floatValue()-maxMileageOld;
					maxMileageTrend = (float)(Math.round(maxMileageTrend*10))/10;
					data.setMaxMileageTrend(maxMileageTrend);
				}else {
					data.setMaxMileageTrend(maxMileage);
				}
			}
			float maxSpeedOld = Float.valueOf(beforeWeekReport.getMaxSpeed());
			float maxSpeedTrend = maxSpeed-maxSpeedOld;
			maxSpeedTrend = (float)(Math.round(maxSpeedTrend*10))/10;
			data.setMaxSpeedTrend(maxSpeedTrend);
			if(null!=startWeekReport.getPec()) {
				int pec = startWeekReport.getPec();
				if(null!=beforeWeekReport.getPec()) {
					int pecOld = beforeWeekReport.getPec();
					data.setPeccancyTrend(pec-pecOld);
				}else {
					data.setPeccancyTrend(pec);
				}
			}
			int accOld = Integer.valueOf(StringUtils.isEmpty(beforeWeekReport.getAcce())?"0":beforeWeekReport.getAcce());
			data.setAcceTrend(acc-accOld);
			int brakeOld = Integer.valueOf(StringUtils.isEmpty(beforeWeekReport.getBrake())?"0":beforeWeekReport.getBrake());
			data.setBrakeTrend(brake-brakeOld);
			int turnOld = Integer.valueOf(StringUtils.isEmpty(beforeWeekReport.getTurn())?"0":beforeWeekReport.getTurn());
			data.setTurnTrend(turn-turnOld);
		}
		//查询本周几天内的
		int tsStart = DateUtil.getDateSeconds(DateUtil.toDate(startWeekReport.getStartdate(), "yyyy-MM-dd"));
		int tsEnd =  DateUtil.getDateSeconds(DateUtil.toDate(startWeekReport.getEnddate(), "yyyy-MM-dd"));
		List<PushDayData> dayList = dayDataMongoDAO.queryPageList(requestVO.getTerminalId(), tsStart, tsEnd);
		//有日报数据
		if(null!=dayList && dayList.size()>0) {
			String mileageDurations = "";
			boolean dayFlag = false;	//
			if(DateUtil.getWeekNum(dayList.get(dayList.size()-1).getDate())==7) {
				float mileageLast = Float.valueOf(dayList.get(dayList.size()-1).getMileage());
				mileageLast = (float)(Math.round(mileageLast*10))/10;
				float duraH = Float.valueOf(dayList.get(dayList.size()-1).getDuration())/60;
				duraH = (float)(Math.round(duraH*10))/10;
				mileageDurations += "7#"+mileageLast+"#"+duraH+",";
				dayFlag = true;
			}
			
			if(dayFlag) {
				if(dayList.size()>=2) {
					for(int i=0;i<dayList.size()-1;i++) {
						int weekDayNum = DateUtil.getWeekNum(dayList.get(i).getDate());
						float mileageF = Float.valueOf(dayList.get(i).getMileage());
						mileageF = (float)(Math.round(mileageF*10))/10;
						float duraHF = Float.valueOf(dayList.get(i).getDuration())/60;
						duraHF = (float)(Math.round(duraHF*10))/10;
						mileageDurations += weekDayNum+"#"+mileageF+"#"+duraHF+",";
					}
				}
			}else {
				for(int i=0;i<dayList.size();i++) {
					int weekDayNum = DateUtil.getWeekNum(dayList.get(i).getDate());
					float mileageF = Float.valueOf(dayList.get(i).getMileage());
					mileageF = (float)(Math.round(mileageF*10))/10;
					float duraHF = Float.valueOf(dayList.get(i).getDuration())/60;
					duraHF = (float)(Math.round(duraHF*10))/10;
					mileageDurations += weekDayNum+"#"+mileageF+"#"+duraHF+",";
				}
			}
			if(!StringUtils.isEmpty(mileageDurations)) {
				mileageDurations = mileageDurations.substring(0, mileageDurations.length()-1);
			}
			data.setMileageDurations(mileageDurations);
		}
		//里程打败值（待写）
		if(data.getMileage()<100) {
			data.setMileageDefeat("63");
		}else if(data.getMileage()<200) {
			data.setMileageDefeat("87");
		}else {
			data.setMileageDefeat("95");
		}
		//时长打败值（待写）
		if(data.getDuration()<300) {
			data.setDurationDefeat("65");
		}else if(data.getDuration()<600) {
			data.setDurationDefeat("85");
		}else {
			data.setDurationDefeat("95");
		}
		//设置车牌号码
		data.setCarNum(startWeekReport.getCarNum());
		//查询套餐以及到期时间信息
		SimInfo simInfo = simMapper.findSimInfo(requestVO.getIccid());
		if(null!=simInfo) {
			if(null!=simInfo.getRegisterDate()) {
				int onlineDays = DateUtil.getDateDiffDay(simInfo.getRegisterDate(), new Date());
				data.setOnlineDays(onlineDays);
			}
			data.setComboName(simInfo.getComboName());
			if(null!=simInfo.getExpireDate()) {
				data.setValidate(DateUtil.formatDate(simInfo.getExpireDate(), "yyyy-MM-dd"));
			}
		}
		result.setData(data);
		return result;
	}
	
	/**
	 * 
	 * @param requestVO
	 * @return
	 * @throws ParseException 
	 */
	public BaseInfo<Object> lastWeekReportScore(CommonReqVO requestVO) throws ParseException{
		BaseInfo<Object> result = new BaseInfo<Object>();
		MsgTerminal terminal = terminalService.getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		List<String> dateStr = DateUtil.getWeekDate_US_UP(DateUtil.format(new Date(), "yyyy-MM-dd"));
		PushWeekData startWeekReport = weekReportMongoDAO.queryByTerDate(terminalId, dateStr.get(0));
		LastWeekScoreVO data = new LastWeekScoreVO();
		data.setTerminalId(terminalId);
		if(null!=startWeekReport) {
			data.setScore(startWeekReport.getScore());
		}
		result.setData(data);
		return result;
	}
	
	/**
	 * 查询周报微信模板推送信息
	 * @param requestVO
	 * @author pom
	 * @return
	 * @throws ParseException 
	 * @throws UnsupportedEncodingException 
	 */
	public BaseInfo<Object> getUbiWeekTripReport(WeekWechatReqVO requestVO) throws ParseException, UnsupportedEncodingException{
		BaseInfo<Object> result = new BaseInfo<Object>();
		String startDate = requestVO.getDate();
		//不传查询时间，默认为查询上周周报信息
		if(StringUtils.isEmpty(startDate)) {
			startDate = DateUtil.getWeekDate_US_UP(DateUtil.getDateString(new Date())).get(0);
		}
		PushWeekData pushWeekData = weekReportMongoDAO.queryByTerDate(requestVO.getTerminalId(), startDate);
		if(null!=pushWeekData) {
			UbiWeekReportResVO data = new UbiWeekReportResVO();
			String reportStr = strArray[new Random().nextInt(10)];
			data.setUser(requestVO.getTerminalId());
			data.setReportmsg(URLEncoder.encode(pushWeekData.getAvgSpeed()+"_"+pushWeekData.getMileage()+"_"+pushWeekData.getTravels()+"_"+reportStr+"_"+pushWeekData.getScore()+"_"+pushWeekData.getDuration()+"_"+startDate,"utf-8"));
			result.setData(data);
		}
		return result;
	}
	
	public BaseInfo<Object> getWeekData(WeekWechatReqVO requestVO) throws ParseException{
		BaseInfo<Object> result = new BaseInfo<Object>();
		String startDate = requestVO.getDate();
		//不传查询时间，默认为查询上周周报信息
		if(StringUtils.isEmpty(startDate)) {
			startDate = DateUtil.getWeekDate_US_UP(DateUtil.getDateString(new Date())).get(0);
		}
		PushWeekData pushWeekData = weekReportMongoDAO.queryByTerDate(requestVO.getTerminalId(), startDate);
		if(null!=pushWeekData) {
			PushWeekDataResVO data = new PushWeekDataResVO();
			data.setTerminalId(requestVO.getTerminalId());
			data.setDate(startDate);
			data.setAvgSpeed(pushWeekData.getAvgSpeed());
			data.setMaxSpeed(pushWeekData.getMaxSpeed());
			data.setMileage(pushWeekData.getMileage());
			data.setScore(pushWeekData.getScore());
			data.setTravels(pushWeekData.getTravels());
			result.setData(data);
		}
		return result;
	}
	
	
	
	private List<EocGps> gpsTimeFilter(List<EocGps> list){
		if(null==list || list.size()==0) {
			return null;
		}
		List<EocGps> result = new ArrayList<EocGps>();
		long lastTime = 0;
		for(int i=0;i<list.size();i++) {
			if(i==0) {
				lastTime = list.get(i).getTime().getTime();
				result.add(list.get(i));
			}else if(i==(result.size()-1)) {
				lastTime = list.get(i).getTime().getTime();
				result.add(list.get(i));
			}else {
				if(list.get(i).getTime().getTime()-lastTime>=10*1000) {
					lastTime = list.get(i).getTime().getTime();
					result.add(list.get(i));
				}
			}
		}
		return result;
	}
	
	private List<EocGps> gpsFilter(List<EocGps> list) {
		List<EocGps> resultList = new ArrayList<EocGps>();
		long tempSeconds;
		double tempDistance;
		double speed;
		int t=0;
		for(int i=0;i<(list.size()-1);i++) {
			if(t>(list.size()-1)) {
				break;
			}
			//两个点直接相差的时间
			tempSeconds = getSeconds(list.get(t),list.get(i+1));
			//两点直接的距离
			tempDistance = geo_distance(list.get(i+1).getLat(),list.get(i+1).getLon(),list.get(t).getLat(),list.get(t).getLon());
			//若两点时间间隔小于0,
			if(tempSeconds<=0) {
				t++;
				continue;
			}
			//两个点直接的平均速度
			speed = tempDistance/tempSeconds;
			
			//车辆运行速度在高速时，默认车辆不漂移，车辆高速定义为80~150km/h
			if(list.get(t).getSpeed()>80 && list.get(t).getSpeed()<150) {
				t++;
				resultList.add(list.get(i));
				continue;
			}
			if(tempDistance == 0) {	//相邻两点坐标未移动，抛弃前一个点数据
				t++;
			} else if(speed>=100) {	//平均速度严重过大时，认为i点之前的点包括i点，gps长时间传输过来的数据可能有问题，则下次基准点抛弃i点选为i+2点
				t+=2;
				i++;
				if(i>(list.size()-1)) {
					break;
				}
				resultList.add(list.get(i));
			} else if(speed>=50) {	//一段距离计算出来的平均速度大于50m/s,即180km/h时，认为该点存在漂移，过滤掉该点
				t+=2;
				i++;
			} else if(speed>=36) {//当速度达到130km/h时，比较改点附近点的速度，若是速度都未达到100km/h(28m/s)，认为改点并未处于高速运行状态，可能为漂移点，应该过滤
				//附近有速度大于100km/h的点，认为改点漂移可能较小，忽略改点漂移可能
				if((i>0 && list.get(i-1).getSpeed()>28) || (i<(list.size()-1) && list.get(i+1).getSpeed()>28)) {
					t++;
					resultList.add(list.get(i));
				} else {
					t+=2;
					i++;
				}
			} else {
				t++;
				resultList.add(list.get(i));
			}
		}
		//将最后一个点直接加进来
		resultList.add(list.get(list.size()-1));
		return resultList;
	}
	
	private static double geo_distance(double lat1, double lng1, double lat2,
			double lng2) {
		// earth's mean radius in KM
		double r = 6378.137;
		lat1 = Math.toRadians(lat1);
		lng1 = Math.toRadians(lng1);
		lat2 = Math.toRadians(lat2);
		lng2 = Math.toRadians(lng2);
		double d1 = Math.abs(lat1 - lat2);
		double d2 = Math.abs(lng1 - lng2);
		double p = Math.pow(Math.sin(d1 / 2), 2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.pow(Math.sin(d2 / 2), 2);
		double dis = r * 2 * Math.asin(Math.sqrt(p))*1000;
		return dis;
	}
	
	private long getSeconds(EocGps eocgps1,EocGps eocgps2) {
		return ((eocgps2.getTime().getTime()-eocgps1.getTime().getTime())/1000);
	}
	
	private List<EocGps> removeDuplicate(List<EocGps> list) {   
		HashSet<EocGps> h = new HashSet<EocGps>(list);
		list.clear();   
		list.addAll(h);   
		return list;   
	}
	
	private String getCollection(Date time) {
		String format = DateUtil.format(time, "yyyyMMdd");
		return "eocGps_"+format;
	}
	private  Date getAfterDate(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, amount);
		return calendar.getTime();
	}
	private  List<EocGps> readCloud(Integer terminalId,Date time,List<EocGps> result){
		SimpleDateFormat sdfAll = new SimpleDateFormat("yyyyMMddHHmmss");
		String urlPrefix ="http://gpsdb-10012948.cossh.myqcloud.com/";
		String rootDir = "/mydata/tempfile/";
		File cityDir = new File(rootDir);
		if(!cityDir.exists()) {
			cityDir.mkdirs();
		}
		String dateStr=null;
		try{
			//数据不在高速MONGO，直接去对象存储里面读取行程文件，并且在内存中解析
			dateStr = sdfAll.format(time);
			result = copyAndReadZip(terminalId, result, urlPrefix, rootDir,
					dateStr);
		} catch (Exception e) {
			try {
				Date start = addMinutes(time,-10);  //减少10分钟
				Date end =addMinutes(time,10);  //增加10分钟
				String tableName = "EOC_MESSAGE_TRIP_"+(terminalId%16);
				//查询在这段时间内的行程信息
				EocTripSpy eocTripSpy = tripMapper.getMaxMileageTrip(terminalId, start, end, tableName);
				if (eocTripSpy!=null) {
					dateStr=sdfAll.format(eocTripSpy.getStartTime());
					result = copyAndReadZip(terminalId, result, urlPrefix, rootDir,dateStr);
				}
			} catch (Exception e2) {
				logger.error("GpsQueryMGImpl readCloud param:terminalId:"+terminalId+",startTime:"+time+",readCloud fail,fail at tecent cos,cause by: "+e2);
			}
		}
			return result;
	}
	private List<EocGps> copyAndReadZip(Integer terminalId,List<EocGps> result, 
			String urlPrefix, String rootDir,String dateStr) throws Exception {
		String requestUrl = urlPrefix+dateStr.substring(0, 8)+"/"+dateStr.substring(8, 12)+"/"+terminalId+"-"+dateStr+".zip";
		URL httpurl = new URL(requestUrl);
		File f = new File(rootDir + dateStr+".zip");
		FileUtils.copyURLToFile(httpurl, f);
		result = readZipFile(rootDir + dateStr+".zip",terminalId+"-"+dateStr+".txt",result);
		//清理掉查询过的行程文件
		f.delete();
		return result;
	}

	public void test(String time) {
		Integer terminalId = 3843451;
//		String time = "2019-09-26 13:08:28";
		Date date = DateUtil.toDate(time,"yyyy-MM-dd HH:mm:ss");
		List<EocGps> list = new ArrayList<>();
		List<EocGps> result = readCloud(terminalId, date, list);
		System.out.println(result.size());
	}

	@SuppressWarnings("unchecked")
	private  List<EocGps> readZipFile(String file,String readFileName,List<EocGps> list) throws Exception {   
	    ZipFile zip = new ZipFile(file);
	    Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
	    ZipEntry ze;
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    while (entries.hasMoreElements()) { 
	    	ze = entries.nextElement();
	    	// 读取目标对象  
	        if (ze.getName().equals(readFileName)) {  
	            Scanner scanner = new Scanner(zip.getInputStream(ze));  
	            while (scanner.hasNextLine()) {
	            	String line = scanner.nextLine();
	            	if(line!=null&&!line.equals("")){
	            		String gpsArr[] = line.split(",");
	            		if(gpsArr.length>=6){
	            			try {
								EocGps eocGps =new EocGps();
								eocGps.setTerminalId(Integer.valueOf(gpsArr[0]));
								eocGps.setTime(sdf.parse(gpsArr[1]));
								eocGps.setLon(Double.valueOf(gpsArr[2]));
								eocGps.setLat(Double.valueOf(gpsArr[3]));
								eocGps.setSpeed(Float.valueOf(gpsArr[5]));
								list.add(eocGps);
							} catch (Exception e) {
								logger.error("GpsQueryMGImpl findMany readZipFile error!!!:{}",e);
							}
	            		}
	            	}
	            }  
	            scanner.close();  
	        }
	    }    
	    zip.close();
	    if(list!=null && list.size()>0){
	    	return list;
	    }
	    return null;
	}
	private  Date addMinutes(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}

	public static void main(String[] args) {
		String urlPrefix ="http://gpsdb-10012948.cossh.myqcloud.com/";
		String rootDir = "/mydata/tempfile/";
		Integer terminalId = 123;
		Date date = new Date();
		SimpleDateFormat sdfAll = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdfAll.format(date);
		String requestUrl = urlPrefix+dateStr.substring(0, 8)+"/"+dateStr.substring(8, 12)+"/"+terminalId+"-"+dateStr+".zip";
		System.out.println(requestUrl);
	}
}
