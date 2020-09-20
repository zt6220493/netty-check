package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.inject.Injector;
import com.schoolguard.message.storage.client.ESApiException;
import com.schoolguard.message.storage.client.PersistenceAPI;
import com.schoolguard.message.storage.shared.model.*;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.KafkaMsgProducer2;
import com.schoolguard.messages.producers.Topic;
import com.schoolguard.messages.producers.messenger.JobType;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Rogers on 15-5-4.
 */
public class PersistenceBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(PersistenceBolt.class);
    private OutputCollector collector;

    private PersistenceAPI persistence;
    private KafkaMsgProducer2 msgProducer;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        persistence = injector.getInstance(PersistenceAPI.class);
        msgProducer = injector.getInstance(KafkaMsgProducer2.class);
    }

    @Override
    public void execute(Tuple tuple) {
        JobType jobType = (JobType) tuple.getValueByField("type");
        boolean result = tuple.getBooleanByField("result");
        switch (jobType){
            case NOTICE:
                Notice notice =(Notice) tuple.getValueByField("data");
                try {
                    String id = persistence.saveNotice(notice);
                    notice.setId(id);
                    if (result) {
                        msgProducer.send(Topic.PRE_SEND_NOTICE, notice.toBytes());
                    }
                    collector.ack(tuple);
                } catch (ESApiException e){ //TODO: 不是所有的异常情况都需要replay
                    collector.reportError(e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("PersistenceBolt Error:", e);
                    collector.ack(tuple);
                }
                break;
            case ASSIGNMENT:
                Assignment assignment = (Assignment) tuple.getValueByField("data");
                try {
                    String id = persistence.saveAssignment(assignment);
                    assignment.setId(id);
                    if(result){
                        msgProducer.send(Topic.PRE_SEND_ASSIGNMENT, assignment.toBytes());
                    }
                    collector.ack(tuple);
                } catch (ESApiException e){
                    // replay
                    collector.reportError(e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("PersistenceBolt Error:", e);
                    collector.ack(tuple);
                }
                break;
            case EXAM:
                Exam exam = (Exam) tuple.getValueByField("data");
                try {
                    String id = persistence.saveExam(exam);
                    exam.setId(id);
                    if(result){
                        msgProducer.send(Topic.PRE_SEND_EXAM, exam.toBytes());
                    }
                    collector.ack(tuple);
                } catch (ESApiException e){
                    // replay
                    collector.reportError(e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("PersistenceBolt Error:", e);
                    collector.ack(tuple);
                }
                break;

            case OA_MSG:
                OAMessage oaMessage = (OAMessage) tuple.getValueByField("data");
                try {
                    String id = persistence.saveOAMessage(oaMessage);
                    oaMessage.setId(id);
                    if(result){
                        msgProducer.send(Topic.PRE_SEND_OA_MSG, oaMessage.toBytes());
                    }
                    collector.ack(tuple);
                } catch (ESApiException e){
                    // replay
                    collector.reportError(e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("PersistenceBolt Error:", e);
                    collector.ack(tuple);
                }
                break;

            case ATTENDANCE:
                Attendance attendance = (Attendance) tuple.getValueByField("data");
                try {
                    String id = persistence.saveAttendance(attendance);
                    attendance.setId(id);
                    if(result){
                        msgProducer.send(Topic.PRE_SEND_ATTENDANCE, attendance.toBytes());
                    }
                    collector.ack(tuple);
                } catch (ESApiException e) {
                    // replay
                    collector.reportError(e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("PersistenceBolt Error:", e);
                    collector.ack(tuple);
                }
                break;

            default:
                throw new RuntimeException("未知的JobType");
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}

