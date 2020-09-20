/**
 * Project Name:eoc-sim
 * File Name:RestTemplateConfig.java
 * Package Name:com.ecar.eoc.config
 * Date:2018年6月19日下午3:31:54
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * ClassName:RestTemplateConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date:     2018年6月19日 下午3:31:54 <br/>
 * @author   zhongying	 
 */
@Configuration
public class RestTemplateConfig {
	
	@Bean  
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) { 
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
        
    }  
  
    @Bean  
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();  
        factory.setReadTimeout(5000);//ms  
        factory.setConnectTimeout(15000);//ms  
        return factory;  
    }
}

