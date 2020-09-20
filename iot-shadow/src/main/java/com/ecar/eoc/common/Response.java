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
 * Function: Controller层返回结果进行封装. <br/>
 * Date:     2018年5月31日 下午3:57:27 <br/>
 * @author   zhongying	 
 */
public class Response extends HashMap<String,Object> implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6001373562319330629L;
	
	public Response() {
		put("code", 0);
	}
	
	public static Response error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");
	}
	
	public static Response error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
	}
	
	public static Response error(int code, String msg) {
		Response r = new Response();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Response ok(String msg) {
		Response r = new Response();
		r.put("msg", msg);
		return r;
	}
	
	public static Response ok(Map<String, Object> map) {
		Response r = new Response();
		r.putAll(map);
		return r;
	}
	
	public static Response ok() {
		return new Response();
	}

	public Response put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	public Response data(Object value) {
		super.put("data", value);
		return this;
	}

	public static Response apiError(String msg) {
		return error(1, msg);
	}

}

