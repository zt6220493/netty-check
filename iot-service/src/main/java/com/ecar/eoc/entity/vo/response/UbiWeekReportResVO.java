package com.ecar.eoc.entity.vo.response;

import java.io.Serializable;

public class UbiWeekReportResVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer user;		//用户ID
	private String reportmsg;	//周报信息
	
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	public String getReportmsg() {
		return reportmsg;
	}
	public void setReportmsg(String reportmsg) {
		this.reportmsg = reportmsg;
	}

}
