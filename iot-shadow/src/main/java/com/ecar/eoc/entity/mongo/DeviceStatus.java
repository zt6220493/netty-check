package com.ecar.eoc.entity.mongo;

import java.util.Date;

/**
 * 设备连接状态实体类
 * 
 * @author pom
 *
 */
public class DeviceStatus {
	
	private String terminalId;	// 设备号
	private String connNo; // 连接流水号
	private String type; // 事件类型，设备1 上线、2 下线、3 状态同步,4 机卡质检
	private Integer desc; //时间类型为离线时：0: 离线(终端主动断开) 1: 离线(服务器踢出) 2: 离线(连接异常服务器离线)
	private Date time; // 消息Unix时间戳
	private Integer gpsCount; // gps 个数
	private String sysTime; //系统时间
	private String iccid;	//设备SIM卡号
	private String imei;	//设备imei号
	private String version; //软件版本
	
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getConnNo() {
		return connNo;
	}
	public void setConnNo(String connNo) {
		this.connNo = connNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDesc() {
		return desc;
	}
	public void setDesc(Integer desc) {
		this.desc = desc;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Integer getGpsCount() {
		return gpsCount;
	}
	public void setGpsCount(Integer gpsCount) {
		this.gpsCount = gpsCount;
	}
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
