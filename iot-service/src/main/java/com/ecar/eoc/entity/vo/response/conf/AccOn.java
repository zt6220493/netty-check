package com.ecar.eoc.entity.vo.response.conf;

import java.io.Serializable;

public class AccOn implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer mode;
	private Integer period;
	
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}

}
