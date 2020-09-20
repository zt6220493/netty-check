/**
 * Project Name:eoc-sim
 * File Name:LoggerManage.java
 * Package Name:com.ecar.eoc.config
 * Date:2018年8月16日下午2:56:15
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:LoggerManage <br/>
 * Function: 日志注解. <br/>
 * Date:     2018年8月16日 下午2:56:15 <br/>
 * @author   zhongying	 
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggerManage {
	
	String value() default "";

}

