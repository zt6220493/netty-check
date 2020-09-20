package com.ecar.eoc.vo.request;

import java.io.Serializable;

public class DeviceLogReq implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
	private String imei;
	private String sessionId;
	private String urls;
	private String sign;
	
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
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUrls() {
		return urls;
	}
	public void setUrls(String urls) {
		this.urls = urls;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
