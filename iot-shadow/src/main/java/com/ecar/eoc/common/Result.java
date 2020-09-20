/**
 * Project Name:eoc-sim
 * File Name:Result2.java
 * Package Name:com.ecar.eoc.common
 * Date:2018年8月14日上午10:12:01
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.common;

import java.io.Serializable;

/**
 * ClassName:Result2 <br/>
 * Function: 业务层统一返回对象，不要使用Exception做流程控制. <br/>
 * Date: 2018年8月14日 上午10:12:01 <br/>
 * 
 * @author zhongying
 */
public class Result<T> implements Serializable {

	private static int DEFAULT_CODE = 0;
	
	private static String DEFAULT_MSG = "操作成功";
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4492760085255202943L;

	private int code;

	private String errorMsg;

	private T data;

	public Result() {
		this.code = DEFAULT_CODE;
		this.errorMsg = DEFAULT_MSG;
	}
	

	public Result(T data) {
		this.code = DEFAULT_CODE;
		this.errorMsg = DEFAULT_MSG;
		this.data = data;
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
