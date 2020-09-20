package com.ecar.eoc.entity.vo.response.conf;

import java.io.Serializable;

public class OffUploadConf implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer keepCount;

	public Integer getKeepCount() {
		return keepCount;
	}

	public void setKeepCount(Integer keepCount) {
		this.keepCount = keepCount;
	}

}
