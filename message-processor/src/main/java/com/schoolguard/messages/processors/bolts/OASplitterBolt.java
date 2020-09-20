package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.inject.Injector;
import com.schoolguard.core.ws.client.*;
import com.schoolguard.core.ws.client.domain.Teacher;
import com.schoolguard.message.storage.shared.model.OAMessage;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.OAMessenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rogers on 15-5-5.
 */
public class OASplitterBolt extends BaseRichBolt {
    private static Logger logger = LoggerFactory.getLogger(OASplitterBolt.class);
    private OutputCollector collector;

    private TeacherAPI teacherAPI;
    private SchoolApi schoolApi;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        this.teacherAPI = injector.getInstance(TeacherAPI.class);
        this.schoolApi = injector.getInstance(SchoolApi.class);
    }

    @Override
    public void execute(Tuple tuple){
        byte[] bytes = tuple.getBinary(0);
        OAMessenger messenger = (OAMessenger) OAMessenger.fromBytes(bytes);
        try {
            List<Teacher> teachers = schoolApi.getSchoolTeachers(messenger.getSchoolId());
            Map<Integer, Teacher> teacherMap = new HashMap<>();
            for(Teacher t: teachers){
                teacherMap.put(t.getId(), t);
            }

            for (Integer teacherId : messenger.getReceiverIds()) {
                Teacher teacher = teacherMap.get(teacherId);
                if(teacher == null){
                    // 出错了，此ID的老师不在该学校内
                    logger.error(String.format("出错了，ID为%d的老师不在学校%d内: ", teacherId, messenger.getSchoolId()));
                    continue;
                }
                OAMessage oaMessage = new OAMessage(messenger.getMessageId(),
                        messenger.getSchoolId(), messenger.getSchoolName(),
                        messenger.getSenderId(), messenger.getSenderName(),
                        messenger.getSenderAvatar(),
                        teacherId, teacher.getName(), teacher.getMobile(),
                        teacher.getWechat() != null ? teacher.getWechat().getOpenid() : null,
                        messenger.getSignature(),
                        messenger.getContent(),
                        messenger.getScheduleTime(),
                        messenger.getCreateTime());
                collector.emit(tuple, new Values(oaMessage));
            }
        } catch (RestApiException e){
            logger.error("Api Request failed", e);
            collector.fail(tuple);
            return;
        }
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("oa_message"));
    }
}
