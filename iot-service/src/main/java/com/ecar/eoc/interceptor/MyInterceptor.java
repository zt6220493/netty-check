/**
 * Project Name:eoc-sim
 * File Name:MyInterceptor.java
 * Package Name:com.ecar.eoc.interceptor
 * Date:2018年5月31日下午4:09:22
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
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

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long startTime = System.currentTimeMillis();
		request.setAttribute("requestStartTime", startTime);

		// 进入action前 日志打印
		String uri = request.getRequestURI();
		Map<String, String[]> paramMap = request.getParameterMap();

		log.info("ip={}|uri={} start", request.getRemoteAddr(), uri);

		StringBuffer params = new StringBuffer();
		for (String key : paramMap.keySet()) {
			params.append(key + "=" + paramMap.get(key)[0] + "&");
		}
		int len = params.length();
		if (len > 0) {
			params.delete(len - 1, len);
		}
		log.info("params={" + params + "}");

		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		long startTime = (Long) request.getAttribute("requestStartTime");
		long endTime = System.currentTimeMillis();
		long executeTime = endTime - startTime;
		// 打印方法执行时间
		if (executeTime > 1000) {
			log.info("[{}] 长执行耗时：{}ms", method.getDeclaringClass().getName(), executeTime);
		} else {
			log.info("[{}.{}] 执行耗时：{}ms", method.getDeclaringClass().getSimpleName(), method.getName(), executeTime);
		}

		log.info(request.getRequestURI() + " end");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
