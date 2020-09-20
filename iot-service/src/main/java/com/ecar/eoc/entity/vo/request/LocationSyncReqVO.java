package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class LocationSyncReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String imei;
	private String iccid;
	private Integer terminalId;	//设备终端号
	private Double lat;
	private Double lon;
	private Long time;	//gps时间
	private Float speed;
	private Integer acc;
	private Double direct;	//方向
	private Double height;	//高度
	private Integer type;	//事件交互类型：1 拍照，2 熄火，3 震动
	
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
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public Integer getAcc() {
		return acc;
	}
	public void setAcc(Integer acc) {
		this.acc = acc;
	}
	public Double getDirect() {
		return direct;
	}
	public void setDirect(Double direct) {
		this.direct = direct;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
