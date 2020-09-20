/**
 * Project Name:eoc-sim
 * File Name:SchedulerTask.java
 * Package Name:com.ecar.eoc.task
 * Date:2018年7月6日下午1:54:25
 * Copyright (c) 2018, E-CAR All Rights Reserved.
 *
*/

package com.ecar.eoc.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecar.eoc.kafka.KafkaSender;

/**
 * ClassName:SchedulerTask <br/>
 * Function: 定时任务. <br/>
 * Date:     2018年7月6日 下午1:54:25 <br/>
 * @author   zhongying	 
 */
@Component
public class SchedulerTask {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private int count = 0;
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss");
	
	@Autowired
	private KafkaSender kafkaSender;
	
	@Scheduled(cron="*/30 * * * * ?")
	private void process() {
		log.info("定时任务执行：第{}次，时间:{}",count++,FORMAT.format(new Date()));
//		kafkaSender.send("test", "测试消息发送，第"+count+"次");
	} 

}

