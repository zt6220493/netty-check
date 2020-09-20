package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class WeekReportReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String openId;
	private String publicId;
	private Integer terminalId;
	private String startDate;
	private String endDate;
	private String iccid;
	private String sign;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
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
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
