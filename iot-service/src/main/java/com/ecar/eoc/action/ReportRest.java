package com.ecar.eoc.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.common.Constant;
import com.ecar.eoc.entity.vo.request.CommonReqVO;
import com.ecar.eoc.entity.vo.request.LimitReqVO;
import com.ecar.eoc.entity.vo.request.ReportListReqVO;
import com.ecar.eoc.entity.vo.request.TripReportReqVO;
import com.ecar.eoc.entity.vo.request.WeekReportReqVO;
import com.ecar.eoc.entity.vo.request.WeekWechatReqVO;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.service.ReportService;
import com.ecar.eoc.service.TerminalService;
import com.ecar.eoc.utils.Md5Util;

/**
 * 报告接口
 * wechat_qsyz_notice_设备Id_3_5  --生成驾驶报告
 * wechat_qsyz_notice_设备Id_3_6  --里程报告提醒
 * wechat_qsyz_notice_设备Id_3_7  --行车日报提醒
 * wechat_qsyz_notice_设备Id_3_8  --行车周报提醒
 * wechat_qsyz_notice_设备Id_3_9  --行车月报提醒
 * 示例:wechat_qsyz_notice_460010584307961_3_5
 * redis有数据开关关闭   没有(默认)则打开
 * @author pom
 *
 */
@RestController
@RequestMapping("/wechat/report")
public class ReportRest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ReportService reportService;
	@Autowired
	private TerminalService terminalService;
	
	/**
	 * 里程报告列表
	 * @return
	 */
	@RequestMapping("/tripReportlist")
	public BaseInfo<Object> tripReportlist(ReportListReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei())
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			if(null==requestVO.getPageIndex()) {
				requestVO.setPageIndex(0); 	//不设置初始页，默认为第一页数据。即最新数据
			}
			if(null==requestVO.getPageSize()) {
				requestVO.setPageSize(5); // 默认pageSize为5
			}else if(requestVO.getPageSize().intValue()>20 ||
					requestVO.getPageSize().intValue()<=0) {
				requestVO.setPageSize(5); // 页面分页不在规定范围之内，强制转化为分页大小5
			}
			result =  reportService.tripReportlist(requestVO);
			logger.info("tripReportlist success,request:{}",requestStr);
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("tripReportlist error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("tripReportlist error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	/**
	 * 周报列表查询
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/weekReportlist")
	public BaseInfo<Object> weekReportlist(ReportListReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei())
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			if(null==requestVO.getPageIndex()) {
				requestVO.setPageIndex(0); 	//不设置初始页，默认为第一页数据。即最新数据
			}
			if(null==requestVO.getPageSize()) {
				requestVO.setPageSize(5); // 默认pageSize为5
			}else if(requestVO.getPageSize().intValue()>20 ||
					requestVO.getPageSize().intValue()<=0) {
				requestVO.setPageSize(5); // 页面分页不在规定范围之内，强制转化为分页大小5
			}
			result =  reportService.weekReportlist(requestVO);
			logger.info("findCar success,request:{}",requestStr);
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("findCar error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("findCar error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 删除里程报告
	 * @return
	 */
	@RequestMapping("/deleteTripReport")
	public BaseInfo<Object> deleteTripReport(TripReportReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==requestVO) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getOpenId()) ||
					StringUtils.isEmpty(requestVO.getTripCode())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  reportService.deleteTripReport(requestVO);
			logger.info("deleteTripReport success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("deleteTripReport error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("deleteTripReport error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 删除周报
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/deleteWeekReport")
	public BaseInfo<Object> deleteWeekReport(WeekReportReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==requestVO) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getOpenId()) ||
					null==requestVO.getTerminalId() ||
					StringUtils.isEmpty(requestVO.getStartDate())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  reportService.deleteWeekReport(requestVO);
			logger.info("deleteWeekReport success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("deleteWeekReport error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("deleteWeekReport error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 里程报告详情查询
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/queryTripReport")
	public BaseInfo<Object> queryTripReport(TripReportReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==requestVO) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getOpenId()) ||
					StringUtils.isEmpty(requestVO.getTripCode())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  reportService.queryTripReport(requestVO);
			logger.info("queryTripReport success,request:{}",requestStr);
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("queryTripReport error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("queryTripReport error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 里程报告GPS查询
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/tripReportGps")
	public BaseInfo<Object> tripReportGps(TripReportReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==requestVO) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getOpenId()) ||
					requestVO.getTerminalId()==null ||
					StringUtils.isEmpty(requestVO.getStartTime()) ||
					StringUtils.isEmpty(requestVO.getEndTime())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  reportService.tripReportGps(requestVO);
			logger.info("queryTripReport success,request:{}",requestStr);
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("queryTripReport error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("queryTripReport error,request:{},result:{}",requestStr,e);
		}
		return result;
	}

	/**
	 * 里程报告GPS查询
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/testGps")
	public BaseInfo<Object> testGps(String time){
		String requestStr = JSON.toJSONString(time);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			reportService.test(time);
			logger.info("queryTripReport success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("queryTripReport error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("queryTripReport error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 周报详情
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/weekReportDtl")
	public BaseInfo<Object> weekReportDtl(WeekReportReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(null==requestVO) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getOpenId()) ||
					requestVO.getTerminalId()==null ||
					StringUtils.isEmpty(requestVO.getStartDate()) ||
					StringUtils.isEmpty(requestVO.getIccid())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  reportService.weekReportDtl(requestVO);
			logger.info("weekReportDtl success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		} catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("weekReportDtl error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("weekReportDtl error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 驾驶报告上周评分查询
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/lastWeekReportScore")
	public BaseInfo<Object> lastWeekReportScore(CommonReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei())
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  reportService.lastWeekReportScore(requestVO);
			logger.info("lastWeekReportScore success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("lastWeekReportScore error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("lastWeekReportScore error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 3.1.15 里程报告提醒阈值设置
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/tripReport/saveLimit")
	public BaseInfo<Object> saveLimit(LimitReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei())
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())
					|| StringUtils.isEmpty(requestVO.getLimit())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  terminalService.saveLimit(requestVO);
			logger.info("saveLimit success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("saveLimit error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("saveLimit error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 3.1.16 里程报告提醒阈值查询
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/tripReport/queryLimit")
	public BaseInfo<Object> queryLimit(CommonReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei())
					|| StringUtils.isEmpty(requestVO.getPublicId())
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  terminalService.queryLimit(requestVO);
			logger.info("queryLimit success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("queryLimit error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("queryLimit error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 查询周报微信模板推送信息
	 * @param requestVO
	 * @author pom
	 * @return
	 */
	@RequestMapping("/getUbiWeekTripReport")
	public BaseInfo<Object> getUbiWeekTripReport(WeekWechatReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(null==requestVO.getTerminalId()) {
				throw new BizException("330011", "终端Id不能为空");
			}
			result =  reportService.getUbiWeekTripReport(requestVO);
			logger.info("getUbiWeekTripReport success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("getUbiWeekTripReport error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("getUbiWeekTripReport error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 查询周报微信模板推送信息【新版】
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/getWeekData")
	public BaseInfo<Object> getWeekData(WeekWechatReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(null==requestVO.getTerminalId()) {
				throw new BizException("330011", "终端Id不能为空");
			}
			result =  reportService.getWeekData(requestVO);
			logger.info("getWeekData success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("getWeekData error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("getWeekData error,request:{},result:{}",requestStr,e);
		}
		return result;
	} 
}
