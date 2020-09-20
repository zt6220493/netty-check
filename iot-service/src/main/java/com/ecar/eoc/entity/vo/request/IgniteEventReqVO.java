package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

/**
 * 终端点火事件上传请求类
 * @author pom
 *
 */
public class IgniteEventReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;	//终端ID
	private String imei;		//设备IMEI
	private String iccid;		//SIM卡iccid
	private Integer acc;		//acc点火熄火 1 点火，0熄火
	private Integer type;		//类型（0车机ACC指令；1 终端软件模拟指令）
	private Long gpsTime;		//gps时间
	private String lat;			//gps纬度
	private String lon;			//gps经度
	private Integer satelliteNum;//卫星颗数
	private Float speed;		//gps速度
	private Long timestamp;		//产生事件时间（linux时间戳毫秒）
	private String gpsgroup;	//上传5个gps点，做过滤之用
	private String sign;		//签名字段
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public Integer getAcc() {
		return acc;
	}
	public void setAcc(Integer acc) {
		this.acc = acc;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getGpsTime() {
		return gpsTime;
	}
	public void setGpsTime(Long gpsTime) {
		this.gpsTime = gpsTime;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public Integer getSatelliteNum() {
		return satelliteNum;
	}
	public void setSatelliteNum(Integer satelliteNum) {
		this.satelliteNum = satelliteNum;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getGpsgroup() {
		return gpsgroup;
	}
	public void setGpsgroup(String gpsgroup) {
		this.gpsgroup = gpsgroup;
	}

}
