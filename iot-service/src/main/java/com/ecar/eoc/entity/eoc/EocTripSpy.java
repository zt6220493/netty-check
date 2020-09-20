package com.ecar.eoc.entity.eoc;

import java.util.Date;

/**
 * 行程简化对象【传输行程GPS数据之用】
 * @author ZHOUZHENG
 *
 */
public class EocTripSpy {
	private Long tripId;
	private Integer terminalId;
	private Date startTime;
	private Date endTime;
	private Double totalMileage;
	
	public Long getTripId() {
		return tripId;
	}
	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Double getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(Double totalMileage) {
		this.totalMileage = totalMileage;
	}
	
	@Override
	public String toString() {
		return "EocTripSpy [tripId=" + tripId + ", terminalId=" + terminalId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", totalMileage=" + totalMileage + "]";
	}
}
