package com.ecar.eoc.entity.log;

import java.io.Serializable;
import java.util.Date;

public class DeviceLogFile implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer terminalId;
	private String deviceId;
	private String sessionId;
	private String url;
	private Date createTime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "DeviceLogFile{" +
				"id=" + id +
				", terminalId=" + terminalId +
				", deviceId='" + deviceId + '\'' +
				", sessionId='" + sessionId + '\'' +
				", url='" + url + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
