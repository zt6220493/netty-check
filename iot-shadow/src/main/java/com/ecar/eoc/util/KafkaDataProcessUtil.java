package com.ecar.eoc.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.ecar.eoc.common.Constant;
import com.ecar.eoc.common.DateUtil;
import com.ecar.eoc.entity.kafka.KafkaMsgData;
import com.ecar.eoc.entity.mongo.DeviceStatus;

public class KafkaDataProcessUtil {
	private static final Logger logger = LoggerFactory.getLogger(KafkaDataProcessUtil.class);

	/**
	 * 解析报文
	 * @return
	 */
	public static DeviceStatus processMsgData(KafkaMsgData kafkaMsgData) {
		DeviceStatus mongoDevice = null;
		if (kafkaMsgData.getCmdId().equals("20010")) {
			// 设备上线
			mongoDevice = processOnline(kafkaMsgData);
		} else if (kafkaMsgData.getCmdId().equals("20020")) {
			// 设备下线
			mongoDevice = processOffline(kafkaMsgData);
		} else if (kafkaMsgData.getCmdId().equals("20030")) {
			// 同步状态
			mongoDevice = processSyncStatus(kafkaMsgData);
		} else if(kafkaMsgData.getCmdId().equals("20040")){
			//机卡质检
			mongoDevice = processSimCheck(kafkaMsgData);
		}else {
			logger.warn("cmdId 为 %s，无法识别", kafkaMsgData.getCmdId());
		}
		return mongoDevice;
	}

	/**
	 * 设备上线
	 * 
	 * @param kafkaMsgData
	 * @return
	 * @throws Exception
	 */
	private static DeviceStatus processOnline(KafkaMsgData kafkaMsgData) {
		String messageStr = new String(kafkaMsgData.getMessage());
		String[] messageArry = messageStr.split(",");
		DeviceStatus obj = new DeviceStatus();
		obj.setTerminalId(kafkaMsgData.getTerminalId());
		obj.setConnNo(messageArry[0]);
		obj.setGpsCount(Integer.valueOf(messageArry[1]));
		obj.setType(Constant.DEVICE_STATUS_ONLINE);
		obj.setTime(new Date(kafkaMsgData.getTime()));
		obj.setSysTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		if (!StringUtils.isEmpty(kafkaMsgData.getVersion())) {
			obj.setVersion(kafkaMsgData.getVersion());
		}
		return obj;
	}

	/**
	 * 设备下线
	 * 
	 * @param kafkaMsgData
	 * @return
	 * @throws Exception
	 */
	private static DeviceStatus processOffline(KafkaMsgData kafkaMsgData) {
//		logger.info("offline terminalId:{},message:{}",kafkaMsgData.getTerminalId(),new String(kafkaMsgData.getMessage()));
		String messageStr = new String(kafkaMsgData.getMessage());
		String[] messageArry = messageStr.split(",");
		DeviceStatus obj = new DeviceStatus();
		obj.setTerminalId(kafkaMsgData.getTerminalId());
		obj.setConnNo(messageArry[0]);
		String count = messageArry[1];
		if(StringUtils.isEmpty(count) || "null".equals(count)) {
			obj.setGpsCount(0);
		}else {
			obj.setGpsCount(Integer.valueOf(messageArry[1]));
		}
		obj.setType(Constant.DEVICE_STATUS_OFFLINE);
		obj.setTime(new Date(kafkaMsgData.getTime()));
		obj.setSysTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		if (!StringUtils.isEmpty(kafkaMsgData.getVersion())) {
			obj.setVersion(kafkaMsgData.getVersion());
		}
		// 事件类型说明，0: 离线(终端主动断开) 1: 离线(服务器踢出) 2: 离线(连接异常服务器离线)
		obj.setDesc(Integer.parseInt(messageArry[2]));
		return obj;
	}

	/**
	 * 状态同步
	 * 
	 * @param kafkaMsgData
	 * @return
	 * @throws Exception
	 */
	private static DeviceStatus processSyncStatus(KafkaMsgData kafkaMsgData) {
		String messageStr = new String(kafkaMsgData.getMessage());
		String[] messageArry = messageStr.split(",");
		DeviceStatus obj = new DeviceStatus();
		obj.setTerminalId(kafkaMsgData.getTerminalId());
		obj.setConnNo(messageArry[0]);
		obj.setGpsCount(Integer.valueOf(messageArry[1]));
		obj.setType(Constant.DEVICE_STATUS_SYNC);
		obj.setTime(new Date(kafkaMsgData.getTime()));
		obj.setSysTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		if (!StringUtils.isEmpty(kafkaMsgData.getVersion())) {
			obj.setVersion(kafkaMsgData.getVersion());
		}
		return obj;
	}
	
	/**
	 * 机卡质检
	 * @param kafkaMsgData
	 * @return
	 */
	private static DeviceStatus processSimCheck(KafkaMsgData kafkaMsgData) {
		if(StringUtils.isEmpty(kafkaMsgData.getTerminalId()) || 
				"null".equals(kafkaMsgData.getTerminalId())) {
			return null;
		}
		String messageStr = new String(kafkaMsgData.getMessage());
		String[] messageArry = messageStr.split(",");
		DeviceStatus obj = new DeviceStatus();
		obj.setTerminalId(kafkaMsgData.getTerminalId());
		obj.setType(Constant.DEVICE_STATUS_CHECK);
		obj.setTime(new Date(kafkaMsgData.getTime()));
		obj.setSysTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
		if (!StringUtils.isEmpty(kafkaMsgData.getVersion())) {
			obj.setVersion(kafkaMsgData.getVersion());
		}
		String iccid = messageArry[0];
		if(!StringUtils.isEmpty(iccid) && !("null".equals(iccid))) {
			obj.setIccid(messageArry[0]);
		}
		String imei = messageArry[1];
		if(!StringUtils.isEmpty(imei) && !("null".equals(imei))) {
			obj.setImei(messageArry[1]);
		}
		return obj;
	}
}
