package com.ecar.eoc.vo.response;

import java.io.Serializable;

public class DeviceCountRes implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
