/**
 * Project Name:eoc-sim
 * File Name:Result.java
 * Package Name:com.ecar.eoc.common
 * Date:2018年5月31日下午3:57:27
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;


/**
 * ClassName:Result <br/>
 * Function: 返回结果进行封装. <br/>
 * Date:     2018年5月31日 下午3:57:27 <br/>
 * @author   zhongying	 
 */
public class Result extends HashMap<String,Object> implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6001373562319330629L;
	
	public Result() {
		put("code", 0);
	}
	
	public static Result error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
	}
	
	public static Result error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
	}
	
	public static Result error(int code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Result ok(String msg) {
		Result r = new Result();
		r.put("msg", msg);
		return r;
	}
	
	public static Result ok(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}
	
	public static Result ok() {
		return new Result();
	}

	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	public Result data(Object value) {
		super.put("data", value);
		return this;
	}

	public static Result apiError(String msg) {
		return error(1, msg);
	}

}

