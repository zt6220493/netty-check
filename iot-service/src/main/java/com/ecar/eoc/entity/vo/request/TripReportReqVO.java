package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

public class TripReportReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String openId;		//微信号
	private String publicId;	//公众号
	private String imei;		//设备IMEI
	private String iccid;		//SIM卡ICCID
	private String tripCode;	//行程记录id
	private String startTime;	//开始时间
	private String endTime;		//结束时间
	private Integer terminalId;	//终端ID
	private String sign;		//校验字符串
	
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
	public String getTripCode() {
		return tripCode;
	}
	public void setTripCode(String tripCode) {
		this.tripCode = tripCode;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	
	
}
