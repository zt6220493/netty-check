package com.ecar.eoc.entity.eoc;

import java.io.Serializable;
import java.util.Date;

public class WechatBind implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 微信ID
	 */
	private String openId;

	/**
	 * 微信公众号
	 */
	private String publicId;

	/**
	 * 设备物理编号
	 */
	private String imei;

	/**
	 * 用户手机号
	 */
	private String mobile;

	/**
	 * 创建时间
	 */
	private Date createDate;

	private String isRegisterUser;

	/**
	 * 更新时间
	 */
	private Date updateDate;

	private Integer terminalId;

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getIsRegisterUser() {
		return isRegisterUser;
	}

	public void setIsRegisterUser(String isRegisterUser) {
		this.isRegisterUser = isRegisterUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
