package com.ecar.eoc.vo.request;

import java.io.Serializable;

public class DeviceCountReq implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String certificate;
	private String timestamp;
	private String sign;
	
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
