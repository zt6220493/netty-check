package com.ecar.eoc.entity.vo.response;

public class WeekReportVO {
	private Integer terminalId;
	private String startDate;
	private String timeStr;
	private String score;
	private String mileage;
	private String duration;
	private String avgSpeed;
	private String maxSpeed;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
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
}
