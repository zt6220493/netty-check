package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class PriventThieveryResVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
	private Integer status;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
