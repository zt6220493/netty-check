/**
 * Project Name:eoc-sim
 * File Name:RedisConfig.java
 * Package Name:com.ecar.eoc.config
 * Date:2018年7月3日下午3:45:20
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.config;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

/**
 * ClassName:redis配置 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2018年7月3日 下午3:45:20 <br/>
 * 
 * @author zhongying
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	/**
	 * 默认实现，基于jedis.不推荐 redisTemplate:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhongying
	 * @param factory
	 * @return
	 */
	/*
	 * public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory
	 * factory) { RedisTemplate<String, Object> template = new RedisTemplate<String,
	 * Object>(); template.setConnectionFactory(factory);
	 * 
	 * FastJsonRedisSerializer<Object> fastjsonReisSerializer = new
	 * FastJsonRedisSerializer<>(Object.class);
	 * 
	 * FastJsonConfig fastJsonConfig = new FastJsonConfig();
	 * fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
	 * fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
	 * fastJsonConfig.setCharset(Charset.forName("UTF-8"));
	 * fastjsonReisSerializer.setFastJsonConfig(fastJsonConfig);
	 * 
	 * template.setKeySerializer(new StringRedisSerializer());
	 * template.setValueSerializer(fastjsonReisSerializer);
	 * template.afterPropertiesSet(); return template; }
	 */

	/**
	 * 基于Lettuce实现redis连接池，推荐使用，比jedis好 redisTemplate:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author zhongying
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(factory);

		FastJsonRedisSerializer<Object> fastjsonReisSerializer = new FastJsonRedisSerializer<>(Object.class);

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		fastJsonConfig.setCharset(Charset.forName("UTF-8"));
		fastjsonReisSerializer.setFastJsonConfig(fastJsonConfig);

		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(fastjsonReisSerializer);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

}
