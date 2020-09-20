package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.schoolguard.message.storage.shared.model.*;
import com.schoolguard.messages.processors.spouts.SpoutId;
import com.schoolguard.messages.producers.messenger.JobType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Created by Rogers on 15-5-4.
 *
 * 从spout下来后，分拣出短信和微信消息，转发到各自的bolt处理
 */
public class MsgSortOutBolt extends BaseRichBolt {
    private OutputCollector collector;

    public static final String SMS_STREAM = "sms_stream";
    public static final String WEIXIN_STREAM = "weixin_stream";

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        switch (tuple.getSourceComponent()){
            case SpoutId.NOTICE:
                Notice notice = (Notice) fromBytes(tuple.getBinary(0));
                if(MsgType.SMS.equals(notice.getMsgType())){
                    collector.emit(SMS_STREAM, tuple, new Values(JobType.NOTICE, notice));
                }else if(MsgType.WEIXIN.equals(notice.getMsgType())){
                    collector.emit(WEIXIN_STREAM, tuple, new Values(JobType.NOTICE, notice));
                }
                collector.ack(tuple);
                break;

            case SpoutId.ASSIGNMENT:
                Assignment assignment = (Assignment) fromBytes(tuple.getBinary(0));
                if(MsgType.SMS.equals(assignment.getMsgType())){
                    collector.emit(SMS_STREAM, tuple, new Values(JobType.ASSIGNMENT, assignment));
                }else if(MsgType.WEIXIN.equals(assignment.getMsgType())){
                    collector.emit(WEIXIN_STREAM, tuple, new Values(JobType.ASSIGNMENT, assignment));
                }
                collector.ack(tuple);
                break;

            case SpoutId.EXAM:
                Exam exam = (Exam) fromBytes(tuple.getBinary(0));
                if(MsgType.SMS.equals(exam.getMsgType())){
                    collector.emit(SMS_STREAM, tuple, new Values(JobType.EXAM, exam));
                }else  if(MsgType.WEIXIN.equals(exam.getMsgType())){
                    collector.emit(WEIXIN_STREAM, tuple, new Values(JobType.EXAM, exam));
                }
                collector.ack(tuple);
                break;

            case SpoutId.OA_MSG:
                OAMessage oaMessage = (OAMessage) fromBytes(tuple.getBinary(0));
                if(MsgType.SMS.equals(oaMessage.getMsgType())){
                    collector.emit(SMS_STREAM, tuple, new Values(JobType.OA_MSG, oaMessage));
                }else  if(MsgType.WEIXIN.equals(oaMessage.getMsgType())){
                    collector.emit(WEIXIN_STREAM, tuple, new Values(JobType.OA_MSG, oaMessage));
                }
                collector.ack(tuple);
                break;

            case SpoutId.ATTENDANCE:
                Attendance attendance = (Attendance) fromBytes(tuple.getBinary(0));
                if(MsgType.SMS.equals(attendance.getMsgType())){
                    collector.emit(SMS_STREAM, tuple, new Values(JobType.ATTENDANCE, attendance));
                }else  if(MsgType.WEIXIN.equals(attendance.getMsgType())){
                    collector.emit(WEIXIN_STREAM, tuple, new Values(JobType.ATTENDANCE, attendance));
                }
                collector.ack(tuple);
                break;
            default:
                throw new RuntimeException("不能处理的spout: " + tuple.getSourceComponent());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream(SMS_STREAM, new Fields("type", "data"));
        declarer.declareStream(WEIXIN_STREAM, new Fields("type", "data"));

    }

    public static Object fromBytes(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (ClassNotFoundException | IOException e){
            throw new RuntimeException(e);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }
}

