package com.ecar.eoc.entity.vo.request;

import java.io.Serializable;

/**
 * 上传离线GPS请求类
 * @author pom
 *
 */
public class UploadOffGpsReqVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer terminalId;	//终端ID
	private String imei;		//设备IMEI
	private String iccid;		//SIM卡iccid
	private String errorCode;	//错误码
	private String errorMsg;	//错误信息
	private String url;			//离线gps文件url
	private String bkName;		//文件所在COS桶名buckName
	private String region;		//文件所在桶所在的区域，如：ap-beijing-1
	private String sign;		//签名标识
	
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
