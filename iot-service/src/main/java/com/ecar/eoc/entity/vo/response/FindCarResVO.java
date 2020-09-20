package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class FindCarResVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer terminalId;
	private String lon;
	private String lat;
	private String height;
	private String direct;
	private String speed;
	private String powerOnOff;
	private String time;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getDirect() {
		return direct;
	}
	public void setDirect(String direct) {
		this.direct = direct;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getPowerOnOff() {
		return powerOnOff;
	}
	public void setPowerOnOff(String powerOnOff) {
		this.powerOnOff = powerOnOff;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
