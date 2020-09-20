package com.ecar.eoc.action;

import com.ecar.eoc.entity.vo.response.StatusResVO;
import com.ecar.eoc.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.common.Constant;
import com.ecar.eoc.entity.vo.request.CommonReqVO;
import com.ecar.eoc.entity.vo.request.TripUrlRequestVO;
import com.ecar.eoc.entity.vo.response.OnlineResVO;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.service.TerminalService;
import com.ecar.eoc.utils.Md5Util;

@RestController
@RequestMapping("/wechat/device")
public class WechatRest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TerminalService terminalService;
    @Autowired
    private TripService tripService;

	/**
	 * 包含在线、离线（该接口无休眠状态）
	 * 0：离线，1：在线
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/onlineStatus")
	public BaseInfo<OnlineResVO> onlineStatus(CommonReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<OnlineResVO> result = new BaseInfo<OnlineResVO>();
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
			result =  terminalService.onlineStatus(requestVO);
			logger.info("priventThieveryStatus success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("priventThieveryStatus error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("priventThieveryStatus error,request:{},result:{}",requestStr,e);
		}
		return result;
	}

	/**
	 * 包含在线、离线（内部调用，无签名）
	 * 0：离线，1：在线
	 * @param terminalId
	 * @return
	 */
	@RequestMapping("/status")
	public BaseInfo<StatusResVO> status(Integer terminalId){
		BaseInfo<StatusResVO> result = new BaseInfo<StatusResVO>();
		try {
			if(terminalId==null) {
				throw new BizException("330011", "参数不能为空");
			}
			result =  terminalService.status(terminalId);
			logger.info("status success,request:{},result:{}",terminalId,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("status error,request:{},result:{}",terminalId,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("status error,request:{},result:{}",terminalId,e);
		}
		return result;
	}
	
	/**
	 * 3.1.17 设备运行状态【在线、离线、休眠】
	 * 0：离线，1：在线，2：休眠
	 * @param requestVO
	 * @return
	 */
	@RequestMapping("/runStatus")
	public BaseInfo<OnlineResVO> runStatus(CommonReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<OnlineResVO> result = new BaseInfo<OnlineResVO>();
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
			result =  terminalService.runStatus(requestVO);
			logger.info("priventThieveryStatus success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("priventThieveryStatus error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("priventThieveryStatus error,request:{},result:{}",requestStr,e);
		}
		return result;
	}
	
	/**
	 * 在线查车
	 * @param requestVO
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/findCar")
	public BaseInfo<Object> findCar(CommonReqVO requestVO){
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
			result =  terminalService.findCar(requestVO);
			logger.info("findCar success,request:{},result:{}",requestStr,JSON.toJSONString(result));
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
	 * 在线查车
	 * @param requestVO
	 * @return
	 */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/findCarByImei")
	public BaseInfo<Object> findCarByImei(CommonReqVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getImei()) || StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getImei() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  terminalService.findCar(requestVO);
			logger.info("findCar success,request:{},result:{}",requestStr,JSON.toJSONString(result));
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

	public static void main(String[] args) {
//		String json = "{\"data\":{\"direct\":\"287\",\"height\":\"0\",\"lat\":\"22.759472\",\"lon\":\"113.823036\",\"speed\":\"4.29\",\"terminalId\":3887657,\"time\":\"2019-09-09 17:28:27\"},\"success\":\"true\"}";
//		JSONObject jsonObject = JSON.parseObject(json);
//		JSONObject jsonArray = (JSONObject) jsonObject.get("data");

		System.out.println(Md5Util.stringToMD5("oZClWv2n1Et4LRm9QyuKcWrLI8sA" + Constant.WECHAT_KEY));
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/getTraTripline")
	public BaseInfo<Object> getTraTripline(TripUrlRequestVO requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			if(requestVO==null) {
				throw new BizException("330011", "参数不能为空");
			}
			if(StringUtils.isEmpty(requestVO.getOpenId())
					|| null==requestVO.getTerminalId()
					|| StringUtils.isEmpty(requestVO.getSign())) {
				throw new BizException("330011", "参数不能为空");
			}
			if (!Md5Util.stringToMD5(requestVO.getOpenId() + Constant.WECHAT_KEY).equals(requestVO.getSign())){
				throw new BizException("102101", "非法访问!");
			}
			result =  terminalService.getTraTripline(requestVO);
			logger.info("getTraTripline success,request:{},result:{}",requestStr,JSON.toJSONString(result));
		}catch(BizException e1) {
			result.setSuccess("false");
			result.setErrorCode(e1.getCode());
			result.setErrorMessage(e1.getMessage());
			logger.error("getTraTripline error,request:{},result:{}",requestStr,e1);
		}catch (Exception e) {
			result.setSuccess("false");
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部错误");
			logger.error("getTraTripline error,request:{},result:{}",requestStr,e);
		}
		return result;
	}

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET }, value = "/getMileage")
    public BaseInfo<Object> getMileage(Integer terminalId, String startDate, String endDate, String sign){
        BaseInfo<Object> result = new BaseInfo<Object>();
        String requestStr = "terminalId:" + terminalId + ",startDate:" + startDate + ",endDate:" + endDate;
        try {
            if(terminalId == null) {
                throw new BizException("330011", "参数不能为空");
            }
            if (!Md5Util.stringToMD5(terminalId + Constant.WECHAT_KEY).equals(sign)){
                throw new BizException("102101", "非法访问!");
            }
            result = tripService.getMileage(terminalId,startDate,endDate);
            logger.info("getMileage success,request:{},result:{}",requestStr,JSON.toJSONString(result));
        }catch(BizException e1) {
            result.setSuccess("false");
            result.setErrorCode(e1.getCode());
            result.setErrorMessage(e1.getMessage());
            logger.error("getMileage error,request:{},result:{}",requestStr,e1);
        }catch (Exception e) {
            result.setSuccess("false");
            result.setErrorCode("500");
            result.setErrorMessage("服务器内部错误");
            logger.error("getMileage error,request:{},result:{}",requestStr,e);
        }
        return result;
    }
}
