package com.ecar.eoc.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecar.eoc.common.BaseInfo;
import com.ecar.eoc.entity.log.DeviceLogFile;
import com.ecar.eoc.mapper.DeviceLogMapper;
import com.ecar.eoc.util.HttpUtil;
import com.ecar.eoc.vo.request.DeviceLogReq;

@Service
public class DeviceService {
	@Autowired
	private DeviceLogMapper deviceLogMapper;
	@Autowired
	private RedisTemplate<String,Object>  redisTemple;
	@Autowired
    private Environment env;	//配置文件
	
	/**
	 * 保存设备终端日志
	 * @param requestVO
	 */
	public void saveDeviceLog(DeviceLogReq requestVO) {
		DeviceLogFile deviceLogFile = new DeviceLogFile();
		deviceLogFile.setTerminalId(requestVO.getTerminalId());
		deviceLogFile.setDeviceId(requestVO.getImei());
		deviceLogFile.setSessionId(requestVO.getSessionId());
		deviceLogFile.setUrl(requestVO.getUrls());
		deviceLogFile.setCreateTime(new Date());
		deviceLogMapper.insert(deviceLogFile);
	}
	
	/**
	 * 控制终端上传位置信息
	 * @param terminalId
	 */
	public BaseInfo<Object> conTrolUploadGps(Integer terminalId) {
		BaseInfo<Object> result = new BaseInfo<Object>();
		Object str = redisTemple.opsForValue().get("terminalLogUp"+terminalId);
		if(null!=str) {
			result.setSuccess("false");
			result.setErrorCode("40020");
			result.setErrorMessage("一段时间内，只能控制终端设备上传一次");
			return result;
		}
		
		String url = env.getProperty("ecar.iot.tcp_sync_url");
		StringBuffer param  = new StringBuffer();
		param.append("terminalId="+terminalId);
		param.append("&cmdID=641");
		long currentMilSec = System.currentTimeMillis();
		param.append("&content="+currentMilSec+","+currentMilSec+""+Thread.currentThread().getId());
		String response = HttpUtil.sendGet(url, param.toString());
		if(StringUtils.isEmpty(response)) {
			result.setSuccess("false");
			result.setErrorCode("41000");
			result.setErrorMessage("TCP服务器返回结果为空");
			return result;
		}
		JSONObject json = JSON.parseObject(response);
		//请求成功，5秒之内不得重复请求位置
		if("0".equals(json.getString("isSucceed"))) {
			redisTemple.opsForValue().set("terminalLogUp"+terminalId, "1", 5, TimeUnit.SECONDS);
			Object data = json.get("data");
			if(null!=data) {
				result.setData(data);
			}
			return result;
		}
		result.setSuccess("false");
		result.setErrorCode(json.getString("isSucceed"));
		result.setErrorMessage(json.getString("message"));
		return result;
	}
}
