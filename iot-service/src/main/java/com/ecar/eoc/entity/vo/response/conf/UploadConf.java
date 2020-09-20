package com.ecar.eoc.entity.vo.response.conf;

import java.io.Serializable;

public class UploadConf implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer uploadPeriod;
	private Integer uploadCount;
	
	public Integer getUploadPeriod() {
		return uploadPeriod;
	}
	public void setUploadPeriod(Integer uploadPeriod) {
		this.uploadPeriod = uploadPeriod;
	}
	public Integer getUploadCount() {
		return uploadCount;
	}
	public void setUploadCount(Integer uploadCount) {
		this.uploadCount = uploadCount;
	}
	
}
