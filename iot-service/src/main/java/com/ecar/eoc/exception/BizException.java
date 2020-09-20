/**
 * Project Name:eoc-sim
 * File Name:BizException.java
 * Package Name:com.ecar.eoc.exception
 * Date:2018年5月31日下午4:05:05
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.exception;

import java.io.Serializable;

/**
 * ClassName:BizException <br/>
 * Function: 业务异常定义. <br/>
 * Date: 2018年5月31日 下午4:05:05 <br/>
 * 
 * @author zhongying
 */
public class BizException extends RuntimeException implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 592893550098748438L;

	private String msg;
	private String code = "500";

	public BizException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public BizException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public BizException(String code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public BizException(String msg, String code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
