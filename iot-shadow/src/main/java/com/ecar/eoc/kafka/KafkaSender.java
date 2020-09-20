/**
 * Project Name:eoc-sim
 * File Name:KafkaSender.java
 * Package Name:com.ecar.eoc.kafka
 * Date:2018年7月25日下午4:00:57
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.kafka;
/**
 * ClassName:KafkaSender <br/>
 * Function: kafka发送消息. <br/>
 * Date:     2018年7月25日 下午4:00:57 <br/>
 * @author   zhongying	 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemple;
	
	public void send(String topic,String value) {
		kafkaTemple.send(topic, value);
	}
	

}

