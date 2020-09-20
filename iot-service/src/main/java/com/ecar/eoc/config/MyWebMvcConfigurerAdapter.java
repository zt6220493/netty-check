/**
 * Project Name:eoc-sim
 * File Name:MyWebMvcConfigurerAdapter.java
 * Package Name:com.ecar.eoc.config
 * Date:2018年5月31日下午4:43:06
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ecar.eoc.interceptor.MyInterceptor;

/**
 * ClassName:MyWebMvcConfigurerAdapter <br/>
 * Function: 配置器. <br/>
 * Date: 2018年5月31日 下午4:43:06 <br/>
 * 
 * @author zhongying
 */
@Configuration
public class MyWebMvcConfigurerAdapter implements WebMvcConfigurer {

	/**
	 * 拦截器
	 * 
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/**/*.css", "/**/*.js",
				"/**/*.png", "/**/*.jpg", "/**/*.gif", "/**/*.html", "/**/*.jpeg", "/**/fonts/*");
	}

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		// 2、添加fastjson的配置信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setDateFormat("yyyy-MM-dd hh:mm:ss");

		// 3、解决可能的中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);

		// 4、在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);

		return new HttpMessageConverters((HttpMessageConverter<Object>) fastConverter);
	}
}