package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.schoolguard.messages.producers.messenger.*;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by Rogers on 15-5-4.
 */
public class DuplicateFilterBolt extends BaseRichBolt {
    private static Logger logger = LoggerFactory.getLogger(DuplicateFilterBolt.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple){
        JobType jobType = (JobType) tuple.getValueByField("type");
        switch (jobType){
            case NOTICE:
//                Notice notice =(Notice) tuple.getValueByField("data");
            case ASSIGNMENT:
            case EXAM:
            case OA_MSG:
            case ATTENDANCE:
                this.collector.emit(tuple, new Values(tuple.getValues().toArray()));
                break;

            default:
                throw new RuntimeException("未知的JobType");
        }
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 原封不动的转发
        declarer.declare(new Fields("data", "type", "result"));

    }

    private String hashOfBytes(byte[] bytes){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(bytes);
            return Hex.encodeHexString(hash);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 查询缓存，以确定是否有相同的hash的消息，如果有则表示已经发送过
     * TODO: 加入redis cache
     *
     * @param hashCode
     * @return
     */
    private boolean hasSent(String hashCode){
        return false;
    }
}
