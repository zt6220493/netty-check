package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

/**
 * 终端IOT配置信息获取请求类
 * @author pom
 *
 */
public class ConfReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;	//终端ID
	private String imei;		//设备imei
	private String iccid;		//sim卡iccid
	private String timestamp;	//请求时间戳
	private String sign;		//校验参数
	
	public Integer getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
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
