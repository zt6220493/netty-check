package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class LimitResVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer terminalId;
	private Integer limit;
	
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
