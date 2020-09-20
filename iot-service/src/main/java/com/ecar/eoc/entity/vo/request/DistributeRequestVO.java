package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class DistributeRequestVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String iccid;
	private String imei;
	private Integer terminalId;
	private Long timestamp;
	private String sign;
	
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
