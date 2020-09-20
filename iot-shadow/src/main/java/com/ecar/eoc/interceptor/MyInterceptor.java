/**
 * Project Name:eoc-sim
 * File Name:MyInterceptor.java
 * Package Name:com.ecar.eoc.interceptor
 * Date:2018年5月31日下午4:09:22
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * ClassName:MyInterceptor <br/>
 * Function: 全局拦截器. <br/>
 * Date: 2018年5月31日 下午4:09:22 <br/>
 * 
 * @author zhongying
 */
public class MyInterceptor implements HandlerInterceptor {

	/**
	 * 
	 * 进入接口方法之前处理.
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;

	}

	/**
	 * 
	 * 接口方法结束之后处理
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
