package com.schoolguard.messages.processors.spouts;

import backtype.storm.spout.RawScheme;
import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.*;

import java.util.UUID;

/**
 * Created by Rogers on 15-4-20.
 */
public class SpoutFactory {
    private BrokerHosts brokerHosts;

    public SpoutFactory(String brokerZkStr){
        this.brokerHosts = new ZkHosts(brokerZkStr);
    }

    public KafkaSpout buildSpout(String topicName){
        return getSpout(topicName);
    }

    private KafkaSpout getSpout(String topicName){
        SpoutConfig spoutConfig = new SpoutConfig(this.brokerHosts, topicName, "/kafkastorm", "Spout_" + topicName);
        spoutConfig.scheme = new SchemeAsMultiScheme(new RawScheme());
        spoutConfig.forceFromStart = false; // 勿改
        spoutConfig.startOffsetTime = -1;   // 强制kafka从最近的offset开始读
        return new KafkaSpout(spoutConfig);
    }

}
