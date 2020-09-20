package com.ecar.eoc.entity.eoc;

import java.util.Date;

public class SimInfo {
	private String iccid;
	private Date expireDate;
	private Date registerDate;
	private String comboName;
	
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getComboName() {
		return comboName;
	}
	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
}
