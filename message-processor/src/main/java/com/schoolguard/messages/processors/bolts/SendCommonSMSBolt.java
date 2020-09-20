package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.schoolguard.messages.gateway.sms.service.impl.MTClientImpl;
import com.schoolguard.messages.producers.messenger.SmsMessenger;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2 on 2015/9/2.
 */
public class SendCommonSMSBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(SendCommonSMSBolt.class);
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;

    }

    @Override
    public void execute(Tuple tuple) {
        byte[] bytes = tuple.getBinary(0);
        SmsMessenger messenger =(SmsMessenger) SmsMessenger.fromBytes(bytes);

        MTClientImpl smsSender = new MTClientImpl();

        try {
            smsSender.singleMT(messenger.getMobile(), messenger.getContent(), null, null, null);
            this.collector.ack(tuple);
        }catch (Exception e) {
            // 只尝试发送一次，失败后不尝试
            logger.error("短信发送失败", e);
            this.collector.ack(tuple);
        }


    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
