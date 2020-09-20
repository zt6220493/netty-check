package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class TripUrlRequestVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String openId;
	private Integer terminalId;
	private String sign;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
