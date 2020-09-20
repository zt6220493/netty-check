package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class WeekWechatReqVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
    private String date;
    
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
}
