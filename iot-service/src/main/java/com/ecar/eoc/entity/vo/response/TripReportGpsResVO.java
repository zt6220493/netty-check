package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;
import java.util.List;

/**
 * 里程报告GPS查询
 * @author pom
 *
 */
public class TripReportGpsResVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
	private String startTime;
	private String endTime;
	private List<TripReportGpsVO> gpsList;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public List<TripReportGpsVO> getGpsList() {
		return gpsList;
	}
	public void setGpsList(List<TripReportGpsVO> gpsList) {
		this.gpsList = gpsList;
	}
}
