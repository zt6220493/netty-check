package com.ecar.eoc.entity.vo.response;

public class TripReportVO {
	private Integer terminalId;
	private String tripCode;
	private String startAddr;
	private String startLat;
	private String startLon;
	private String startTime;
	private String endAddr;
	private String endLat;
	private String endLon;
	private String endTime;
	private String mileage;
	private String maxSpeed;
	private String avgSpeed;
	private String duration;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getTripCode() {
		return tripCode;
	}
	public void setTripCode(String tripCode) {
		this.tripCode = tripCode;
	}
	public String getStartAddr() {
		return startAddr;
	}
	public void setStartAddr(String startAddr) {
		this.startAddr = startAddr;
	}
	public String getStartLat() {
		return startLat;
	}
	public void setStartLat(String startLat) {
		this.startLat = startLat;
	}
	public String getStartLon() {
		return startLon;
	}
	public void setStartLon(String startLon) {
		this.startLon = startLon;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndAddr() {
		return endAddr;
	}
	public void setEndAddr(String endAddr) {
		this.endAddr = endAddr;
	}
	public String getEndLat() {
		return endLat;
	}
	public void setEndLat(String endLat) {
		this.endLat = endLat;
	}
	public String getEndLon() {
		return endLon;
	}
	public void setEndLon(String endLon) {
		this.endLon = endLon;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
}
