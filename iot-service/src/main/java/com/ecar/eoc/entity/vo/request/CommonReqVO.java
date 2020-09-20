package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class CommonReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String openId;		//微信号
	private String publicId;	//公众号
	private String imei;		//设备IMEI
	private String iccid;		//sim卡iccid
	private String sign;		//签名规则
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}
