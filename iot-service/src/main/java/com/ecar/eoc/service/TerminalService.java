package com.ecar.eoc.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ecar.eoc.entity.eoc.EocTripSpy;
import com.ecar.eoc.entity.vo.request.*;
import com.ecar.eoc.entity.vo.response.*;
import com.ecar.eoc.mapper.eocdb.eoc.TripMapper;
import com.ecar.eoc.utils.CoordinateConversion;
import com.ecar.eoc.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.common.Constant;
import com.ecar.eoc.config.ConfigurationManager;
import com.ecar.eoc.entity.eoc.DefenceEvent;
import com.ecar.eoc.entity.eoc.MsgTerminal;
import com.ecar.eoc.entity.report.UbiSendLimit;
import com.ecar.eoc.entity.trip.EocGps;
import com.ecar.eoc.entity.vo.response.conf.ConfServer;
import com.ecar.eoc.entity.vo.response.conf.OffUploadConf;
import com.ecar.eoc.entity.vo.response.conf.AccOn;
import com.ecar.eoc.entity.vo.response.conf.UploadConf;
import com.ecar.eoc.mapper.eocdb.eoc.DefenceEventMapper;
import com.ecar.eoc.mapper.eocdb.eoc.TerminalMapper;
import com.ecar.eoc.mapper.mongo.GpsMongoDAO;
import com.ecar.eoc.mapper.mongo.UbiSendLimitDAO;
import com.ecar.eoc.utils.GpsEcarUtil;
import com.ecar.eoc.utils.HttpUtil;

@Service
public class TerminalService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	// Long time 为过期时间，单位是毫秒，终端跟后台的心跳是2分钟之前，参数可设为5分钟。
	private static final long REDIS_OUT_TIME = 10 * 60 * 1000L;	//
	
	private static final long SLEEP_HEART_BEAT_TIME = 6 * 60 * 1000l;
	
	@Autowired
	private TerminalMapper terminalMapper;
	@Autowired
	private DefenceEventMapper defenceEventMapper;
	@Autowired
	private RedisTemplate<String,Object>  redisTemple;
	@Autowired
    private Environment env;	//配置文件
	@Autowired
	private GpsMongoDAO tripDAO;
	@Autowired
	private TerminalService terminalService;
	@Autowired
	private UbiSendLimitDAO ubiSendLimitDAO;
	@Autowired
	private TripMapper tripMapper;
	
	public BaseInfo<PriventThieveryResVO> priventThievery(PriventThieveryReqVO requestVO) {
		BaseInfo<PriventThieveryResVO> result = new BaseInfo<PriventThieveryResVO>();
		MsgTerminal terminal = getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		String defenceStr = Constant.DEVICETYPE_THIEVERY+terminal.getTerminalId().intValue();
		Object oldStatus = redisTemple.opsForValue().get(defenceStr);
		//撤防
//		if(requestVO.getStatus().intValue()==1) {
//			redisTemple.delete(defenceStr);
//		}else {
//			redisTemple.opsForValue().set(defenceStr, requestVO.getStatus().intValue()+"");
//		}
		redisTemple.opsForValue().set(defenceStr, requestVO.getStatus().intValue()+"");
		
		try {
			DefenceEvent defence = new DefenceEvent();
			defence.setImei(terminal.getDeviceId());
			defence.setTerminalId(terminal.getTerminalId());
			defence.setOpenId(requestVO.getOpenId());
			defence.setPublicId(requestVO.getPublicId());
			defence.setNewStatus(requestVO.getStatus().toString());
			if(oldStatus==null){
				defence.setOldStatus("1");
			}else{
				defence.setOldStatus(oldStatus.toString());
			}
			defence.setCreateTime(new Date());
			defenceEventMapper.insert(defence);
		} catch (Exception e) {
			logger.error("priventThievery sync data to mysql error,params:{}",JSON.toJSONString(requestVO));
		}
		PriventThieveryResVO data = new PriventThieveryResVO();
		data.setStatus(requestVO.getStatus());
		data.setTerminalId(terminal.getTerminalId());
		result.setData(data);
		return result;
	}
	
	public BaseInfo<PriventThieveryResVO> priventThieveryStatus(CommonReqVO requestVO){
		BaseInfo<PriventThieveryResVO> result = new BaseInfo<PriventThieveryResVO>();
		MsgTerminal terminal = getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		String defenceStr = Constant.DEVICETYPE_THIEVERY+terminal.getTerminalId().intValue();
		Object oldStatus = redisTemple.opsForValue().get(defenceStr);
		Integer status = 1;	//默认为撤防状态
		if(null!=oldStatus) {
			status = Integer.valueOf(oldStatus.toString());
		}
		PriventThieveryResVO obj = new PriventThieveryResVO();
		obj.setStatus(status);
		obj.setTerminalId(terminal.getTerminalId());
		result.setData(obj);
		return result;
	}
	
	/**
	 * 查询车辆在线离线状态
	 * 该接口无休眠状态
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<OnlineResVO> onlineStatus(CommonReqVO requestVO){
		BaseInfo<OnlineResVO> result = new BaseInfo<OnlineResVO>();
		MsgTerminal terminal = getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		Integer status = queryDeviceStatus(terminalId);
		//对在线状态再进行分类：在线 ，休眠
//		if(status!=null && status.intValue()==1) {
//			try {
//				Object heartStatusObj = redisTemple.opsForValue().get("heartbeat_"+terminalId);
//				if(null!=heartStatusObj) {
//					String heartStatusStr = heartStatusObj.toString();
//					String[] heartStatusArr = heartStatusStr.split(":");
//					//验证数据正确
//					if(heartStatusArr.length==2) {
//						long heartTime = Long.valueOf(heartStatusArr[1]);
//						int acc = Integer.valueOf(heartStatusArr[0]);
//						//据当前时间的心跳时间小于终端设定的休眠心跳时间，判定心跳暂时有效(休眠)
//						if(System.currentTimeMillis()-heartTime<SLEEP_HEART_BEAT_TIME
//								&& acc==0) {
//							status = 2;	//设置为休眠状态
//						}
//					}
//				}
//			} catch (Exception e) {
//				logger.error("onlineStatus get sleep status error,request params:{}, cause by {}",JSON.toJSONString(requestVO),e);
//			}
//		}
		OnlineResVO data = new OnlineResVO();
		data.setImei(terminal.getDeviceId());
		data.setStatus(status);
		result.setData(data);
		return result;
	}

	/**
	 * 查询车辆在线离线状态
	 * 该接口无休眠状态
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<OnlineResVO> runStatus(CommonReqVO requestVO){
		BaseInfo<OnlineResVO> result = new BaseInfo<OnlineResVO>();
		MsgTerminal terminal = getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		Integer status = queryDeviceStatus(terminalId);
		//对在线状态再进行分类：在线 ，休眠
		if(status!=null && status.intValue()==1) {
			try {
				Object heartStatusObj = redisTemple.opsForValue().get("heartbeat_"+terminalId);
				if(null!=heartStatusObj) {
					String heartStatusStr = heartStatusObj.toString();
					String[] heartStatusArr = heartStatusStr.split(":");
					//验证数据正确
					if(heartStatusArr.length==2) {
						long heartTime = Long.valueOf(heartStatusArr[1]);
						int acc = Integer.valueOf(heartStatusArr[0]);
						//据当前时间的心跳时间小于终端设定的休眠心跳时间，判定心跳暂时有效(休眠)
						if(System.currentTimeMillis()-heartTime<SLEEP_HEART_BEAT_TIME
								&& acc==0) {
							status = 2;	//设置为休眠状态
						}
					}
				}
			} catch (Exception e) {
				logger.error("onlineStatus get sleep status error,request params:{}, cause by {}",JSON.toJSONString(requestVO),e);
			}
		}
		OnlineResVO data = new OnlineResVO();
		data.setImei(terminal.getDeviceId());
		data.setStatus(status);
		result.setData(data);
		return result;
	}

	/**
	 * 查询车辆在线离线状态 (只有在线离线，无休眠状态)
	 * @param terminalId
	 * @return
	 */
	public BaseInfo<StatusResVO> status(Integer terminalId){
		BaseInfo<StatusResVO> result = new BaseInfo<StatusResVO>();
		Integer status = queryDeviceStatus(terminalId);
		StatusResVO data = new StatusResVO();
		data.setTerminalId(terminalId);
		data.setStatus(status);
		result.setData(data);
		return result;
	}
	
	/**
	 * 查询设备网络状态
	 * @param terminalId
	 * @return 0 离线,1 在线
	 */
	private Integer queryDeviceStatus(int terminalId) {
		if(0==terminalId) {
			return 0;
		}
		String appKey = "APPKEY_"+terminalId%10;
		Object obj = redisTemple.opsForHash().get(appKey, terminalId+"");
		//没有状态记录，返回离线状态
		if(null==obj || "null".equals(obj)) {
			return 0;
		}
		String[] sts = obj.toString().split(":");
		String gwName = null;
		if(sts.length>1) {
			String saveTime = sts[1];
			long sTime = Long.valueOf(saveTime);
			if (System.currentTimeMillis() - sTime > REDIS_OUT_TIME){
				redisTemple.opsForHash().delete(appKey, terminalId+"");
				return 0;
			}
			gwName = sts[0];
		}
		if(sts.length>0) {
			gwName = sts[0];
		}
		if(!StringUtils.isEmpty(gwName)) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public BaseInfo<Object> getTraTripline(TripUrlRequestVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer url = new StringBuffer();
		url.append(ConfigurationManager.getProperty("eoc.trip.url"));
		url.append("?");
		url.append("terminalId");
		url.append("=");
		url.append(requestVO.getTerminalId());
		map.put("url", url.toString());
		result.setData(map);
		return result;
	} 
	
	public BaseInfo<Object> findCar(CommonReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		MsgTerminal terminal = getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		List<EocGps> list = new ArrayList<EocGps>();
		EocGps gps = null;
		//测试环境mongo配置
		if("false".equals(env.getProperty("ecar.iot.IS_PRODUCT_ENVIRONMENT"))) {
			gps = tripDAO.queryLast(terminalId);
			if(null!=gps) {
				list.add(gps);
			}
		}else {
			Date startTime = null;
			Date endTime = null;

			//获取redis中最新的行程
			String tripJson = (String)redisTemple.opsForValue().get("latesttrip_prefix_"+terminalId);
			//获取数据库中最新的行程信息
			String tableName = "EOC_MESSAGE_TRIP_"+(terminalId%16);
			EocTripSpy tripSpy = tripMapper.getNewestTrip(terminalId,tableName);

			if (!StringUtils.isEmpty(tripJson) && tripSpy != null) {
				//redis中有行程，则最新的行程还未入库
				Date jsonStartTime = null;
				Date jsonEndTime = null;
				JSONObject trip = JSON.parseObject(tripJson);
				if (trip != null && trip.getLong("startTime") != null && trip.getLong("endTime") != null) {
					jsonStartTime = new Date(trip.getLong("startTime"));
					jsonEndTime = new Date(trip.getLong("endTime"));
					if (jsonStartTime != null && jsonStartTime.after(tripSpy.getEndTime())) {
						startTime = jsonStartTime;
					} else if (jsonStartTime != null && jsonEndTime.before(tripSpy.getEndTime())) {
						endTime = tripSpy.getEndTime();
					}
				}
			} else if (!StringUtils.isEmpty(tripJson)){
				JSONObject trip = JSON.parseObject(tripJson);
				if (trip != null && trip.getLong("startTime") != null) {
					startTime = new Date(trip.getLong("startTime"));
				}
			} else if (tripSpy != null) {
				endTime = tripSpy.getEndTime();
			}

			if(startTime != null || endTime != null) {
				String collectionName = null;
				//到MONGO中，查询最后六个点【策略：逐表轮询】
				SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
				if (startTime != null) {
					collectionName = sf.format(startTime);
				}
				if (endTime != null) {
					collectionName = sf.format(endTime);
				}
				List<EocGps> gpsList = tripDAO.queryGpsByTripTime(terminalId,startTime,endTime,"eocGps_" + collectionName);
//				List<EocGps> gpsList = tripDAO.queryGpsByTripTime(terminalId,startTime,endTime,collectionName);
				if (gpsList != null && gpsList.size() > 0) {
					if (gpsList.size() != 0) {
						list.addAll(gpsList);
					}
				}
			}
		}

		if (list.isEmpty()) {
			//mongodb中查询点位为空，则取redis中最新的点位
			String gpsJson = (String)redisTemple.opsForValue().get("lastPoint_"+terminalId);
			if(!StringUtils.isEmpty(gpsJson)) {
				EocGps obj = JSON.parseObject(gpsJson, EocGps.class);
				if (obj != null) {
					list.add(obj);
				}
			}
		}
		
		boolean oClFlag = false;	//控制终端上报位置软件版本标识
		//判断软件版本
		if(terminal.getAppRegistType()!=null) {
			//1. 翼卡在线在线查车主动控制版本
			if(terminal.getAppRegistType().intValue()==0) {
				String ecarVersion = ConfigurationManager.getProperty("ecar.online.version");
				if(!StringUtils.isEmpty(ecarVersion) && terminal.getOsVersion().compareTo(ecarVersion)>0) {
					oClFlag = true;
				}
			}else if(terminal.getAppRegistType().intValue()==1) {
				String sosVersion = ConfigurationManager.getProperty("sos.online.version","4.3.10.0.0");
				if(!StringUtils.isEmpty(sosVersion) && !StringUtils.isEmpty(terminal.getOsVersion()) && terminal.getOsVersion().compareTo(sosVersion)>=0) {
					oClFlag = true;
				}
			}
		}
		//可以控制机器上报位置在线查车逻辑
		if(oClFlag) {
			//设备未上传gps点
			if(null!=list && list.size()>0 && 
					(System.currentTimeMillis()-list.get(list.size()-1).getTime().getTime()<60*1000)) {
				FindCarResVO data = new FindCarResVO();
				EocGps newGps = list.get(list.size()-1);
				data.setTerminalId(newGps.getTerminalId());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timeStr = format.format(newGps.getTime());
				data.setTime(timeStr);
				if(newGps.getDirect()!=null) {
					data.setDirect(newGps.getDirect().intValue()+"");
				}
				if(newGps.getHeight()!=null) {
					data.setHeight(newGps.getHeight().intValue()+"");
				}
				if(newGps.getLat()!=null) {
					data.setLat(newGps.getLat().doubleValue()+"");
					data.setLon(newGps.getLon().doubleValue()+"");
				}
				data.setSpeed(newGps.getSpeed().floatValue()+"");
				if(newGps.getAcc()!=null) {
					data.setPowerOnOff(newGps.getAcc().intValue()+"");
				}
				result.setData(data);
			}else {
				Integer status = queryDeviceStatus(terminalId);
				//在线设备，直接控制终端上报位置信息
				if(status!=null && status.intValue()==1) {
					//查询设备的acc状态信息
					try {
						Object heartStatusObj = redisTemple.opsForValue().get("heartbeat_"+terminalId);
						if(null!=heartStatusObj) {
							String heartStatusStr = heartStatusObj.toString();
							String[] heartStatusArr = heartStatusStr.split(":");
							//验证数据正确
							if(heartStatusArr.length==2) {
								int acc = Integer.valueOf(heartStatusArr[0]);
								//据当前时间的心跳时间小于终端设定的休眠心跳时间，判定心跳暂时有效(休眠)
								if(acc!=0) {
									//判定为在线状态,从终端获取设备位置信息
									String url = ConfigurationManager.getProperty("online.queryCar.url");
									String param = "terminalId="+terminalId;
									String response = HttpUtil.sendGet(url, param, 3000);	//3秒请求延迟，最多
									FindCarResVO data = new FindCarResVO();
									if(!StringUtils.isEmpty(response)) {
										JSONObject json = JSON.parseObject(response);
										Object value = json.get("data");
										if(null!=value && !("".equals(value.toString()))) {
											String[] tcpMsg = value.toString().split(",");
											Long gpsTime = Long.valueOf(tcpMsg[0]);
											SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											String timeStr = format.format(new Date(gpsTime));
											data.setTerminalId(terminalId);
											data.setTime(timeStr);
											data.setLat(tcpMsg[1]);
											data.setLon(tcpMsg[2]);
											data.setSpeed(tcpMsg[3]);
											result.setData(data);
										}else {
											getCarGps(result, list);
										}
									}else {
										getCarGps(result, list);
									}
								}else {
									//休眠状态，不从终端获取位置信息(休眠状态下设备联网次数过多会导致断网)
									getCarGps(result, list);
								}
							}
						}
					}catch(Exception e1) {
						logger.error("findCar error,params:{},cause by {}",JSON.toJSONString(requestVO),e1);
					}
				}else {
					getCarGps(result, list);
				}
			}
		}else {
			getCarGps(result, list);
		}
		return result;
	}

	/**
	 * 根据查询的gps集合计算最后一个点
	 * @param result
	 * @param list
	 */
	private void getCarGps(BaseInfo<Object> result, List<EocGps> list) {
		EocGps newGps = null;
		if(list!=null && list.size()>0){
			newGps = GpsEcarUtil.getCorrectGps(list);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeStr = format.format(newGps.getTime());
			FindCarResVO data = new FindCarResVO();
			data.setTerminalId(newGps.getTerminalId());
			data.setTime(timeStr);
			if(newGps.getDirect()!=null) {
				data.setDirect(newGps.getDirect().intValue()+"");
			}
			if(newGps.getHeight()!=null) {
				data.setHeight(newGps.getHeight().intValue()+"");
			}
			if(newGps.getLat()!=null) {
				data.setLat(newGps.getLat().doubleValue()+"");
				data.setLon(newGps.getLon().doubleValue()+"");
			}
			if (newGps.getSpeed() != null) {
				data.setSpeed(newGps.getSpeed().floatValue()+"");
			}

			if(newGps.getAcc()!=null) {
				data.setPowerOnOff(newGps.getAcc().intValue()+"");
			}
			result.setData(data);
		}else {
			result.setData(null);
		}
	}
	
	/**
	 * 里程报告提醒阈值保存
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> saveLimit(LimitReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		MsgTerminal terminal = terminalService.getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		UbiSendLimit obj = new UbiSendLimit();
		obj.setLimit(requestVO.getLimit());
		obj.setTerminalId(terminalId);
		ubiSendLimitDAO.upsert(obj);
		return result;
	}
	
	/**
	 * 里程报告提醒阈值查询;
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> queryLimit(CommonReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		MsgTerminal terminal = terminalService.getMsgTerminalByImei(requestVO.getImei(),requestVO.getIccid(),requestVO.getPublicId());
		if(null==terminal){
			result.setSuccess("false");
			result.setErrorCode("110039");
			result.setErrorMessage("设备未注册!");
			return result;
		}
		int terminalId = terminal.getTerminalId().intValue();
		UbiSendLimit obj = ubiSendLimitDAO.queryOne(terminalId);
		int limit = 0;
		if(null!=obj) {
			limit = obj.getLimit();
		}else {
			limit = env.getProperty("ecar.iot.tripReport_default_notice_mileage", Integer.class, 20);
		}
		LimitResVO data = new LimitResVO();
		data.setTerminalId(terminalId);
		data.setLimit(limit);
		result.setData(data);
		return result;
	}
	
	//TODO 20191009
	public MsgTerminal getMsgTerminalByImei2(String imei) {
		if(StringUtils.isEmpty(imei)) {
			return null;
		}
		MsgTerminal msgTerminal = new MsgTerminal();
		msgTerminal.setDeviceId(imei);
		msgTerminal = terminalMapper.getLastMsgTerminal(msgTerminal);
		
		if (null != msgTerminal) {
			return msgTerminal;
		}
		if (imei.length() == 15) {
			String deviceIdTemp = imei.substring(0, imei.length() - 1);
			msgTerminal = new MsgTerminal();
			msgTerminal.setDeviceId(deviceIdTemp);
			msgTerminal = terminalMapper.getLastMsgTerminal(msgTerminal);
		}
		return msgTerminal;
	}
	
	//TODO 20191009
	public MsgTerminal getMsgTerminalByImei(String imei,String iccid,String publicId) {
	    String flag = ConfigurationManager.getProperty("USER-OPEN");
        if(StringUtils.isEmpty(flag) || "false".equals(flag)) {
            return getMsgTerminalByImei2(imei);
        }
        
        if(StringUtils.isEmpty(imei)) {
            return null;
        }
        
        String param = "publicId=" + publicId + "&deviceId=" + imei + "&iccid=" + iccid;
        String url = ConfigurationManager.getProperty("ECLOUD_USER_URL");
        String result = HttpUtil.sendGet(url, param, 5000);
        if(StringUtils.isEmpty(result)) {
            logger.error("TerminalService | getMsgTerminalByImei fail,param={}", param);
            return null;
        }
        
        JSONObject jsonObject = JSON.parseObject(result);
        MsgTerminal msgTerminal = null;
        if(jsonObject.containsKey("success") && "true".equals(jsonObject.getString("success"))) {
            msgTerminal = JSON.parseObject(jsonObject.getString("data"), MsgTerminal.class);
        }else {
            logger.error("TerminalService | getMsgTerminalByImei fail,param={},result={}", param, result);
        }
        return msgTerminal;
    }
	
	//TODO 20191009
	public MsgTerminal getMsgTerminalById(Integer terminalId) {
        String flag = ConfigurationManager.getProperty("USER-OPEN");
        if(StringUtils.isEmpty(flag) || "false".equals(flag)) {
            MsgTerminal m = new MsgTerminal();
            m.setTerminalId(terminalId);
            return terminalMapper.getLastMsgTerminal(m);
        }
        
        if(null == terminalId) {
            return null;
        }
        
        String param = "terminalId="+terminalId;
        String url = ConfigurationManager.getProperty("ECLOUD_USER_URL");
        String result = HttpUtil.sendGet(url, param, 5000);
        if(StringUtils.isEmpty(result)) {
            logger.error("TerminalService | getMsgTerminalById fail,param={}", param);
            return null;
        }
        
        JSONObject jsonObject = JSON.parseObject(result);
        MsgTerminal msgTerminal = null;
        if(jsonObject.containsKey("success") && "true".equals(jsonObject.getString("success"))) {
            msgTerminal = JSON.parseObject(jsonObject.getString("data"), MsgTerminal.class);
        }else {
            logger.error("TerminalService | getMsgTerminalById fail,param={},result={}", param, result);
        }
        return msgTerminal;
    }
	
	public BaseInfo<Object> commonConf(ConfReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		ConfResVO data = new ConfResVO();
		ConfServer server = new ConfServer();
		server.setHost(ConfigurationManager.getProperty("tcp.server.host", "218.17.158.22"));
		server.setPort(ConfigurationManager.getProperty("tcp.server.port", "9999"));
		data.setServer(server);
		UploadConf nomalUpload = new UploadConf();
		nomalUpload.setUploadCount(ConfigurationManager.getInteger("nomal.upload.count", 15));
		nomalUpload.setUploadPeriod(ConfigurationManager.getInteger("nomal.upload.period", 15000));
		data.setNomalUpload(nomalUpload);
		UploadConf acUpload = new UploadConf();
		acUpload.setUploadCount(ConfigurationManager.getInteger("ac.upload.count", 20));
		acUpload.setUploadPeriod(ConfigurationManager.getInteger("ac.upload.period", 5000));
		data.setAcUpload(acUpload);
		OffUploadConf offUpload = new OffUploadConf();
		offUpload.setKeepCount(200000);
		data.setOffUpload(offUpload);
		AccOn accOn = new AccOn();
		accOn.setMode(ConfigurationManager.getInteger("accOn.mode", 0));
		accOn.setPeriod(ConfigurationManager.getInteger("accOn.period", 300));
		data.setAccOn(accOn);;
		data.setThresholdTime(Long.valueOf(new Date().getTime()-ConfigurationManager.getInteger("iotConf.offlinetime.threshod", 
				3*3600000)));
		data.setExpireDuration(ConfigurationManager.getInteger("iotConf.valid.period", 2));
		data.setCollectPeriod(ConfigurationManager.getInteger("iotConf.collect.period", 1000)); //采样频率
		data.setToken(System.currentTimeMillis()+""+Thread.currentThread().getId());
		result.setData(data);
		return result;
	}
	
	/**
	 * 同步车辆最新位置信息
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> locationSync(LocationSyncReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		//1.数据正确性判定
		if(null==requestVO.getLat() || null==requestVO.getLon()
				|| null==requestVO.getTime()) {
			result.setSuccess("false");
			result.setErrorCode("30012");
			result.setErrorMessage("参数不能为空");
			return result;
		}
		EocGps gps = new EocGps();
		gps.setTerminalId(requestVO.getTerminalId());
		gps.setTime(new Date(requestVO.getTime().longValue()*1000));
		gps.setLat(requestVO.getLat());
		gps.setLon(requestVO.getLon());
		gps.setSpeed(requestVO.getSpeed());
		gps.setDirect(requestVO.getDirect());
		gps.setHeight(requestVO.getHeight());
		gps.setAcc(requestVO.getAcc());
		gps.setOnline(10); 	//来源标识位
		
		//2.gps正确性判定
		if(!GpsEcarUtil.isGpsRight(gps)) {
			result.setSuccess("false");
			result.setErrorCode("30013");
			result.setErrorMessage("gps取值范围不正确");
			return result;
		}
		if(!GpsEcarUtil.isInChina(gps)) {
			result.setSuccess("false");
			result.setErrorCode("30013");
			result.setErrorMessage("gps不在中国");
			return result;
		}
		//3.查询历史GPS点
		String gpsJson = (String)redisTemple.opsForValue().get("lastPoint_"+requestVO.getTerminalId());
		EocGps obj = null;
		if(!StringUtils.isEmpty(gpsJson)) {
			obj = JSON.parseObject(gpsJson, EocGps.class);
		}
		
		//4.比对更新最新的gps位置，并且标识最新位置来源
		if(null==obj || obj.getTime().getTime() < gps.getTime().getTime()) {
			redisTemple.opsForValue().set("lastPoint_"+requestVO.getTerminalId(), JSON.toJSONString(gps));
		}
		return result;
	}

	/**
	 * 第三方（小镜）GPS数据同步
	 * @param requestVO
	 * @return
	 */
	public BaseInfo<Object> thirdGpsSync(ThirdGpsReqVO requestVO){
		BaseInfo<Object> result = new BaseInfo<Object>();
		//TODO 暂时只做记录，后期再做存储
		return result;
	}

    /**
     * 围栏数据同步
     * @param requestVO
     * @return
     */
    public void fenceSync(FenceReqVO requestVO,String coordType){
        //坐标系转换为wgs84 默认wgs84 wgs84：GPS 坐标 bd_map：百度坐标系 amap 高德坐标系
        if (!StringUtils.isEmpty(coordType)) {
            double[] point;
            switch (coordType) {
                case "wgs84": break;
                case "bd_map":
                    point = CoordinateConversion.bd09_To_gps84(requestVO.getLat(),requestVO.getLon());
                    requestVO.setLat(point[0]);
                    requestVO.setLon(point[1]);
                    break;
                case "amap":
                    point = CoordinateConversion.gcj02_To_Gps84(requestVO.getLat(),requestVO.getLon());
                    requestVO.setLat(point[0]);
                    requestVO.setLon(point[1]);
                    break;
                default: break;
            }
        }
        String key = "trip_fence_" + requestVO.getTerminalId();
        String value = (String) redisTemple.opsForValue().get(key);
        List<FenceReqVO> list = new ArrayList<>();
        Boolean updateTag = false;
        if (!StringUtils.isEmpty(value)) {
            list = JSON.parseArray(value).toJavaList(FenceReqVO.class);
            if (list != null && !list.isEmpty()) {
                for (FenceReqVO reqVO : list) {
                    if (reqVO == null) {
                        continue;
                    }
                    if (requestVO.getId().equals(reqVO.getId())) {
                        reqVO.setLat(requestVO.getLat());
                        reqVO.setLon(requestVO.getLon());
                        reqVO.setRadius(requestVO.getRadius());
                        reqVO.setType(requestVO.getType());
                        reqVO.setCallbackUrl(requestVO.getCallbackUrl());
                        reqVO.setPreStatus(null);
                        updateTag = true;
                    }
                }
            }
        }
        if (!updateTag) {
            list.add(requestVO);
        }
        redisTemple.opsForValue().set(key,JSON.toJSONString(list));
    }

    /**
     * 围栏数据删除
     * @param terminalId
     * @return
     */
    public void fenceDelete(Integer terminalId, Integer fenceId){
        String key = "trip_fence_" + terminalId;
        if (fenceId == null) {
            redisTemple.delete(key);
        } else {
            String value = (String) redisTemple.opsForValue().get(key);
            List<FenceReqVO> list = new ArrayList<>();
            List<FenceReqVO> copyList = new ArrayList<>();
            Boolean updateTag = false;
            if (!StringUtils.isEmpty(value)) {
                list = JSON.parseArray(value).toJavaList(FenceReqVO.class);
                if (list != null && !list.isEmpty()) {
                    for (FenceReqVO fence : list) {
                        if (fence == null) {
                            continue;
                        }
                        if (fenceId.equals(fence.getId())) {
                            updateTag = true;
                        } else {
                            copyList.add(fence);
                        }
                    }
                }
            }
            if (updateTag) {
                if (copyList.isEmpty()) {
                    redisTemple.delete(key);
                } else {
                    redisTemple.opsForValue().set(key,JSON.toJSONString(copyList));
                }
            }
        }
    }

    public void distributeDelete () {
        BufferedReader reader = null;
        try {
            String path = "F:\\device_id.txt";
            File file = new File(path);

            reader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer("[");
            String tempString = null;
            String time;
            Date date = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                System.out.println("line: " + line + " : " + tempString);
                line++;

                redisTemple.delete("distribute_" + tempString);
            }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
    }

    public static void main(String[] args) {
//        produceAnalysis();
//        distributeDelete();

    }

    public static void testAnalysis() {
        BufferedReader reader = null;
        try {
            String path = "F:\\eoc.eocGps-4535390.json";
            File file = new File(path);

            reader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer("[");
            String tempString = null;
            String time;
            Date date = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
//                System.out.println("line: " + line + " : " + tempString);
                line ++;
                if (tempString.contains("_id")) {
                    continue;
                }
                if (tempString.contains("NumberInt")) {
                    tempString = tempString.replace("NumberInt(","");
                    tempString = tempString.replace(")","");
                }
                if (tempString.contains("ISODate(")) {
                    tempString = tempString.replace("ISODate(","");
                    tempString = tempString.replace("T", " ");
                    tempString = tempString.replace(".000+08:00\")","\"");
                    time = (tempString.split(":")[1] + tempString.split(":")[2] + tempString.split(":")[3]).replace("\"","");

                    date = DateUtil.parse(time, "yyyy-MM-dd HHmmss");
                    if (date != null) {
                        tempString = "\"time\" : " + date.getTime();
                    }
                }
                System.out.println("转换后的：" + tempString);
                buffer.append(tempString);
            }
            buffer.append("]");
            reader.close();

            List<EocGps> list = JSON.parseArray(buffer.toString()).toJavaList(EocGps.class);
            List<EocGps> newList = new ArrayList<>();
            //过滤重复gps
            Long tempTime = 0L;
            for (EocGps gps : list) {
                if (gps.getTime().getTime() != tempTime) {
                    newList.add(gps);
                    tempTime = gps.getTime().getTime();
                }
            }
            List<EocGps> newList2 = new ArrayList<>();
            List<EocGps> newList3 = new ArrayList<>();
            for (EocGps eocGps : newList) {
                if (1575441368000L <= eocGps.getTime().getTime() && eocGps.getTime().getTime() <= 1575443540000L) {
                    newList2.add(eocGps);
                    if (eocGps.getSpeed() != null && eocGps.getSpeed() > 6) {
                        System.out.println(eocGps.getSpeed());
                        newList3.add(eocGps);
                    }
                }
            }

            //获取间隔超过1秒的GPS
            List<EocGps> filterList1 = new ArrayList<>();
            List<AnalysisResult> resultList = new ArrayList<>();
            for (int i = 1; i < newList.size(); i++) {
                if (newList.get(i).getTime().getTime() / 1000 - newList.get(i - 1).getTime().getTime() / 1000 > 1) {
                    filterList1.add(newList.get(i - 1));
                    filterList1.add(newList.get(i));
//                    System.out.println("StartTime: " + DateUtil.formatDate(newList.get(i-1).getTime(),"yyyy-MM-dd HH:mm:ss"));
//                    System.out.println("EndTime: " + DateUtil.formatDate(newList.get(i).getTime(),"yyyy-MM-dd HH:mm:ss"));
//                    System.out.println("------------------------------------------");

                    AnalysisResult analysisResult = new AnalysisResult();
                    analysisResult.setIntervalTime(newList.get(i).getTime().getTime() / 1000 - newList.get(i - 1).getTime().getTime() / 1000);
                    analysisResult.setPreGpsTime(newList.get(i - 1).getTime());
                    analysisResult.setNextGpsTime(newList.get(i).getTime());
                    resultList.add(analysisResult);
                }
            }

            resultList.sort(new Comparator<AnalysisResult>() {
                @Override
                public int compare(AnalysisResult o1, AnalysisResult o2) {
                    if (o1.getIntervalTime() > o2.getIntervalTime()) {
                        return -1;
                    } else if (o1.getIntervalTime() < o2.getIntervalTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            System.out.println(resultList.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void produceAnalysis() {
        BufferedReader reader = null;
        try {
            String path = "F:\\eocGps_20191205.json";
            File file = new File(path);

            reader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer("[");
            String tempString = null;
            String time;
            Date date = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                line ++;
                if (tempString.contains("_id")) {
                    String str1 = tempString.substring(1, 43);
                    tempString = tempString.replace(str1,"");
                }
                if (tempString.contains("$date")) {
                    tempString = tempString.replace("{\"$date\":","");
                    tempString = tempString.replace("T", " ");
                    tempString = tempString.replace(".000Z\"}","\"");
                    System.out.println("tempString; " + tempString);
                    String timeStr = tempString;
                    String timeStr2;
                    timeStr = timeStr.split(",")[1];
                    System.out.println("line:" + line + ", tempString: " + tempString);
                    time = (timeStr.split(":")[1] + timeStr.split(":")[2] + timeStr.split(":")[3]).replace("\"","");

                    date = DateUtil.parse(time, "yyyy-MM-dd HHmmss");
                    if (date != null) {
                        timeStr2 = "\"time\" : " + (date.getTime() + 8 * 3600 * 1000);
                        tempString = tempString.replace(timeStr, timeStr2);
                    }
                }
                buffer.append(tempString);
            }
            buffer.append("]");
            reader.close();

            List<EocGps> list = JSON.parseArray(buffer.toString()).toJavaList(EocGps.class);
            List<EocGps> newList = new ArrayList<>();
            //过滤重复gps
            Long tempTime = 0L;
            for (EocGps gps : list) {
                if (gps.getTime().getTime() != tempTime) {
                    newList.add(gps);
                    tempTime = gps.getTime().getTime();
                }
            }
            List<EocGps> newList2 = new ArrayList<>();
            List<EocGps> newList3 = new ArrayList<>();
            for (EocGps eocGps : newList) {
                if (1575441368000L <= eocGps.getTime().getTime() && eocGps.getTime().getTime() <= 1575443540000L) {
                    newList2.add(eocGps);
                    if (eocGps.getSpeed() != null && eocGps.getSpeed() > 6) {
                        System.out.println(eocGps.getSpeed());
                        newList3.add(eocGps);
                    }
                }
            }

            //获取间隔超过1秒的GPS
            List<EocGps> filterList1 = new ArrayList<>();
            List<AnalysisResult> resultList = new ArrayList<>();
            for (int i = 1; i < newList.size(); i++) {
                if (newList.get(i).getTime().getTime() / 1000 - newList.get(i - 1).getTime().getTime() / 1000 > 1) {
                    filterList1.add(newList.get(i - 1));
                    filterList1.add(newList.get(i));
//                    System.out.println("StartTime: " + DateUtil.formatDate(newList.get(i-1).getTime(),"yyyy-MM-dd HH:mm:ss"));
//                    System.out.println("EndTime: " + DateUtil.formatDate(newList.get(i).getTime(),"yyyy-MM-dd HH:mm:ss"));
//                    System.out.println("------------------------------------------");

                    AnalysisResult analysisResult = new AnalysisResult();
                    analysisResult.setIntervalTime(newList.get(i).getTime().getTime() / 1000 - newList.get(i - 1).getTime().getTime() / 1000);
                    analysisResult.setPreGpsTime(newList.get(i - 1).getTime());
                    analysisResult.setNextGpsTime(newList.get(i).getTime());
                    resultList.add(analysisResult);
                }
            }

            resultList.sort(new Comparator<AnalysisResult>() {
                @Override
                public int compare(AnalysisResult o1, AnalysisResult o2) {
                    if (o1.getIntervalTime() > o2.getIntervalTime()) {
                        return -1;
                    } else if (o1.getIntervalTime() < o2.getIntervalTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            for (AnalysisResult result : resultList) {
                String preTime = DateUtil.formatDate(result.getPreGpsTime(), "yyyy-MM-dd HH:mm:ss");
                String nextTime = DateUtil.formatDate(result.getNextGpsTime(), "yyyy-MM-dd HH:mm:ss");
                System.out.println("开始时间：" + preTime + ",结束时间：" + nextTime + ",缺失个数：" + result.getIntervalTime());
            }

            System.out.println(resultList.size());

            test1(resultList);

            System.out.println(resultList.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void test1(List<AnalysisResult> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i % 3 == 0) {
                list.remove(i);
            }
        }
    }
}

class AnalysisResult {

    private Date preGpsTime; //前一个gps时间

    private Date nextGpsTime;//后一个gps时间

    private Long intervalTime;//间隔时间

    public Date getPreGpsTime() {
        return preGpsTime;
    }

    public void setPreGpsTime(Date preGpsTime) {
        this.preGpsTime = preGpsTime;
    }

    public Date getNextGpsTime() {
        return nextGpsTime;
    }

    public void setNextGpsTime(Date nextGpsTime) {
        this.nextGpsTime = nextGpsTime;
    }

    public Long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Long intervalTime) {
        this.intervalTime = intervalTime;
    }
}
