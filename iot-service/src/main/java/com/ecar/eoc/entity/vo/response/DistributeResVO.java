package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class DistributeResVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;
	private String distValue;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getDistValue() {
		return distValue;
	}
	public void setDistValue(String distValue) {
		this.distValue = distValue;
	}

}
