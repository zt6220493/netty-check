package com.ecar.eoc.entity.report;

import java.io.Serializable;

public class UbiSendLimit implements Serializable{
	private static final long serialVersionUID = 1L;
	private String openId;
	private String publicId;
	private Integer terminalId;
	private Integer limit;
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
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
