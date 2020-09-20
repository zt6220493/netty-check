package com.ecar.eoc.entity.kafka;

import java.io.Serializable;

/**
 * @author pom
 * 
 * cmdId 说明  message数据内容
 * 20030 状态同步 (连接流水号,gps点个数)
 * 20010 设备上线 (连接流水号,gps点个数)
 * 20020 设备下线 (连接流水号,gps点个数,事件类型)
 * 20040 机卡质检 (iccid,imei,status,success)
 * 注事件类型说明：0 离线(终端主动断开),1 离线(服务器踢出),2离线(连接异常服务器离线)
 * status注册状态说明 : 0 未注册，1 已注册
 * success质检成功失败说明：0 成功，1 失败
 */
public class KafkaMsgData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String cmdId;		//消息命令字
	private String terminalId;	//设备号
	private String appKey;		//设备appKey
	private String version;		//终端设备版本号
	private long serialNo;		//消息流水号
	private String ip;			//发送消息ip
	private long time;			//消息Unix时间戳
	private byte[] message;		//消息体
	
	public String getCmdId() {
		return cmdId;
	}
	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public long getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(long serialNo) {
		this.serialNo = serialNo;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public byte[] getMessage() {
		return message;
	}
	public void setMessage(byte[] message) {
		this.message = message;
	}
	
}
