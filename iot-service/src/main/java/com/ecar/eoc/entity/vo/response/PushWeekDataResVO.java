package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class PushWeekDataResVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId; // 设备ID
	private String score; // 总的评分
	private String travels; // 行驶次数
	private String mileage; // 总里程
	private String avgSpeed; // 平均速度
	private String maxSpeed; // 最大速度
	private String date;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getTravels() {
		return travels;
	}
	public void setTravels(String travels) {
		this.travels = travels;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
