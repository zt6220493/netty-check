package com.ecar.eoc.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.exception.BizException;
import com.ecar.eoc.vo.request.DeviceCountReq;
import com.ecar.eoc.vo.response.DeviceCountRes;

@RestController
@RequestMapping("/service")
public class ServiceRest {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping("/device/onlineCount")
	public BaseInfo<Object> onlineCount(DeviceCountReq requestVO){
		String requestStr = JSON.toJSONString(requestVO);
		BaseInfo<Object> result = new BaseInfo<Object>();
		try {
			DeviceCountRes data = new DeviceCountRes();
			data.setCount(120);
			result.setData(data);
			log.info("ServiceRest onlineCount success,request:{},response:{}",requestStr,"");
		} catch(BizException e1) {
			result.setErrorCode(e1.getCode()+"");
			result.setErrorMessage(e1.getMessage());
			log.error("ServiceRest onlineCount error,request:{},cause by:{}",requestStr,e1);
		} catch (Exception e) {
			result.setErrorCode("500");
			result.setErrorMessage("服务器内部异常");
			log.error("ServiceRest onlineCount error,request:{},cause by:{}",requestStr,e);
		}
		return result;
	}
}
