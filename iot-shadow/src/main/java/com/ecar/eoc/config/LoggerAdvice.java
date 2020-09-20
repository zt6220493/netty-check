/**
 * Project Name:eoc-sim
 * File Name:LoggerAdvice.java
 * Package Name:com.ecar.eoc.config
 * Date:2018年8月16日下午2:54:27
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * ClassName:LoggerAdvice <br/>
 * Function: 利用aop切面打印接口日志. <br/>
 * Date: 2018年8月16日 下午2:54:27 <br/>
 * 
 * @author zhongying
 */
@Aspect
@Component
public class LoggerAdvice {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before("within(com.ecar.eoc..*) && @annotation(loggerManage)")
	public void addBeforeLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
		logger.info("执行[{}]开始", loggerManage.value());
		logger.info(joinPoint.getSignature().toString());
		logger.info(parseParames(joinPoint.getArgs()));
	}

	@AfterReturning("within(com.ecar.eoc..*) && @annotation(loggerManage)")
	public void addAfterReturningLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
		logger.info("执行[{}]结束", loggerManage.value());
	}

	@AfterThrowing(pointcut = "within(com.ecar.eoc..*) && @annotation(loggerManage)", throwing = "ex")
	public void addAfterThrowingLogger(JoinPoint joinPoint, LoggerManage loggerManage, Exception ex) {
		logger.error("执行[{}]异常", loggerManage.value(), ex);
	}

	private String parseParames(Object[] parames) {
		if (null == parames || parames.length <= 0 || parames.length > 1024) {
			return "";
		}
		StringBuffer param = new StringBuffer("传入参数[{}] ");
		for (Object obj : parames) {
			param.append(JSONObject.toJSON(obj)).append("  ");
		}
		return param.toString();
	}

}
