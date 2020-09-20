package com.ecar.eoc.entity.iot;

import java.io.Serializable;
import java.util.Date;

public class OffLineGpsRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer terminalId;
	private String iccid;
	private String imei;
	private String url;
	private String bkName;
	private String region;
	private Integer status; //状态：0 未处理，1 已处理
	private Date updateTime;
	private Date createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
