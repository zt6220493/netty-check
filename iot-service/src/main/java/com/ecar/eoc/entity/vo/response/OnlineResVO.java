package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class OnlineResVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private String imei;
	private Integer status;
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
