package com.ecar.eoc.kafka;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ecar.eoc.common.DateUtil;
import com.ecar.eoc.entity.kafka.KafkaMsgData;
import com.ecar.eoc.entity.mongo.DeviceStatus;
import com.ecar.eoc.service.IotLog01MongoService;
import com.ecar.eoc.util.KafkaDataProcessUtil;

@Component
public class KafkaConsumer {

    @Resource
    private IotLog01MongoService iotLog01MongoService;

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final static String topicIotLog01 = "iotLog02";

    private final static String topicTStatus01 = "isStatusLog";
    
    private final static String topicInspect = "cardInspect";

    @KafkaListener(topics = { topicIotLog01 }, containerFactory = "batchFactory")
    public void listenTopicIotLog01(List<ConsumerRecord<String, String>> records) {
//    	logger.info(MessageFormat.format("listenTopicIotLog01 kafka topic:{0} 收到消息条数：{1},data:", topicIotLog01, records.size()));
    	if(records!=null && records.size()>0) {
    		String collectionName = null;
    		List<DeviceStatus> list = new ArrayList<DeviceStatus>();
    		for(int i=0;i<records.size();i++) {
    			String recordValue = records.get(i).value().toString().split("@@@")[1];
    			//解析
    			KafkaMsgData kafkaMsgData = JSON.parseObject(recordValue, KafkaMsgData.class);
    			if(null==kafkaMsgData) {
    				continue;
    			}
    			// 业务逻辑处理，根据 cmdId 来判断
    			DeviceStatus deviceStatus = KafkaDataProcessUtil.processMsgData(kafkaMsgData);
    			if(null!=deviceStatus && null!=deviceStatus.getTime()) {
    				list.add(deviceStatus);
    			}else {
    				continue;
    			}
    			if(StringUtils.isEmpty(collectionName)) {
    				collectionName = "iotlog_" + DateUtil.formatMongo(deviceStatus.getTime(),"yyyyMMdd");
    			}
    		}
    		if(list!=null && list.size()>0) {
    			iotLog01MongoService.save(list, collectionName);
    		}
    	}
    }

    @KafkaListener(topics = { topicTStatus01 }, containerFactory = "batchFactory")
    public void listenTopicTStaus01(List<ConsumerRecord<String, String>> records) {
//    	logger.info(MessageFormat.format("listenTopicTStaus01 kafka topic:{0} 收到消息条数：{1}", topicIotLog01, records.size()));
    	if(records!=null && records.size()>0) {
    		String collectionName = null;
    		List<DeviceStatus> list = new ArrayList<DeviceStatus>();
    		for(int i=0;i<records.size();i++) {
    			String recordValue = records.get(i).value().toString().split("@@@")[1];
    			//解析
    			KafkaMsgData kafkaMsgData = JSON.parseObject(recordValue, KafkaMsgData.class);
    			if(null==kafkaMsgData) {
    				continue;
    			}
    			// 业务逻辑处理，根据 cmdId 来判断
    			DeviceStatus deviceStatus = KafkaDataProcessUtil.processMsgData(kafkaMsgData);
    			if(null!=deviceStatus && null!=deviceStatus.getTime()) {
    				list.add(deviceStatus);
    			}else {
    				continue;
    			}
    			if(StringUtils.isEmpty(collectionName)) {
    				collectionName = "iotlog_" + DateUtil.formatMongo(deviceStatus.getTime(),"yyyyMMdd");
    			}
    		}
    		if(list!=null && list.size()>0) {
    			iotLog01MongoService.save(list, collectionName);
    		}
    	}
    }
    
    @KafkaListener(topics = { topicInspect }, containerFactory = "batchFactory")
    public void listenTopicCardInspect(List<ConsumerRecord<String, String>> records) {
//    	logger.info(MessageFormat.format("listenTopicCardInspect kafka topic:{0} 收到消息条数：{1}", topicIotLog01, records.size()));
    	if(records!=null && records.size()>0) {
    		String collectionName = null;
    		List<DeviceStatus> list = new ArrayList<DeviceStatus>();
    		for(int i=0;i<records.size();i++) {
    			String recordValue = records.get(i).value().toString();
    			//解析
    			KafkaMsgData kafkaMsgData = JSON.parseObject(recordValue, KafkaMsgData.class);
    			if(null==kafkaMsgData) {
    				continue;
    			}
    			// 业务逻辑处理，根据 cmdId 来判断
    			DeviceStatus deviceStatus = KafkaDataProcessUtil.processMsgData(kafkaMsgData);
    			if(null!=deviceStatus && null!=deviceStatus.getTime()) {
    				list.add(deviceStatus);
    			}else {
    				continue;
    			}
    			if(StringUtils.isEmpty(collectionName)) {
    				collectionName = "iotlog_" + DateUtil.formatMongo(deviceStatus.getTime(),"yyyyMMdd");
    			}
    		}
    		if(list!=null && list.size()>0) {
    			iotLog01MongoService.save(list, collectionName);
    		}
    	}
    }
    
    

}

