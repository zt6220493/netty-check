package com.ecar.eoc.entity.iot;

import java.io.Serializable;
import java.util.Date;

public class OffLineGpsFailRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer terminalId;
	private String iccid;
	private String imei;
	private String errorCode;
	private String errorMsg;
	private String bkName;
	private String region;
	private Date createTime;
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getBkName() {
		return bkName;
	}
	public void setBkName(String bkName) {
		this.bkName = bkName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
