/**
 * Project Name:eoc-sim
 * File Name:User.java
 * Package Name:com.ecar.eoc.entity.user
 * Date:2018年5月31日下午2:42:30
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.entity.user;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年5月31日 下午2:42:30 <br/>
 * @author   zhongying	 
 */
public class User implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1976586758301109793L;
	
//	private int id;
	
	private String userName;
	
	private String loginName;
	
	private String passWord;
	
	private Date createTime;

//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", loginName=" + loginName + ", passWord=" + passWord
				+ ", createTime=" + createTime + "]";
	}
	

}

