package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.schoolguard.core.ws.client.RestApiException;
import com.schoolguard.core.ws.client.SchoolClassAPI;
import com.schoolguard.core.ws.client.StudentAPI;
import com.schoolguard.core.ws.client.domain.GradeClass;
import com.schoolguard.core.ws.client.domain.Guardian;
import com.schoolguard.core.ws.client.domain.Student;
import com.schoolguard.message.storage.shared.model.MediaType;
import com.schoolguard.message.storage.shared.model.Notice;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.NoticeMessenger;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Rogers on 15-5-5.
 */
public class NoticeSplitterBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(NoticeSplitterBolt.class);
    private OutputCollector collector;

    private SchoolClassAPI schoolClassAPI;
    private StudentAPI studentAPI;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        this.schoolClassAPI = injector.getInstance(SchoolClassAPI.class);
        this.studentAPI = injector.getInstance(StudentAPI.class);
    }

    @Override
    public void execute(Tuple tuple) {
        byte[] bytes = tuple.getBinary(0);
        NoticeMessenger messenger =(NoticeMessenger) NoticeMessenger.fromBytes(bytes);
        switch (messenger.getReceiverType()) {
            case CLASS:
                try {
                    for (Integer classId : messenger.getReceiverIds()) {
                        GradeClass schoolClass = schoolClassAPI.getSchoolGradeClass(classId);
                        List<Student> students = schoolClassAPI.getClassStudents(classId);
                        if (students == null || students.size() == 0) {
                            continue;
                        }
                        for (Student student : students) {
                            List<Guardian> guardians = studentAPI.getStudentGuardians(student.getId());
                            for (Guardian g : guardians) {
                                if(Strings.isNullOrEmpty(messenger.getMediaUrl())) {
                                    // 普通文本消息
                                    Notice notice = new Notice(messenger.getMessageId(),
                                            messenger.getSchoolId(), messenger.getSchoolName(),
                                            messenger.getSenderId(), messenger.getSenderName(),
                                            messenger.getSenderAvatar(),
                                            classId, schoolClass.getName(), student.getId(), student.getName(),
                                            g.getId(), g.getMobile(), g.getWechat() == null ? null : g.getWechat().getOpenid(),
                                            messenger.getSignature(),
                                            messenger.getCreateTime(), messenger.getScheduleTime(),
                                            messenger.getContent()
                                    );
                                    collector.emit(tuple, new Values(notice));

                                } else {
                                    // 多媒体消息
                                    com.schoolguard.message.storage.shared.model.MediaType noticeMediaType = null;
                                    switch (messenger.getMediaType()){
                                        case AUDIO:
                                           noticeMediaType = MediaType.AUDIO; break;
                                        case PICTURE:
                                            noticeMediaType = MediaType.PICTURE; break;
                                    }

                                    Notice notice = new Notice(messenger.getMessageId(),
                                            messenger.getSchoolId(), messenger.getSchoolName(),
                                            messenger.getSenderId(), messenger.getSenderName(),
                                            messenger.getSenderAvatar(),
                                            classId, schoolClass.getName(), student.getId(), student.getName(),
                                            g.getId(), g.getMobile(), g.getWechat() == null ? null : g.getWechat().getOpenid(),
                                            messenger.getSignature(),
                                            messenger.getCreateTime(), messenger.getScheduleTime(),
                                            noticeMediaType, messenger.getMediaUrl()
                                    );

                                    collector.emit(tuple, new Values(notice));
                                }
                            } // guardian loop
                        }   // student loop
                    }   // class loop
                }catch (RestApiException e){
                    logger.error("API请求出错：", e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("发现运行时异常,系统将跳过异常的消息：", e);
                    collector.ack(tuple);
                    return;
                }
                break;

            case STUDENT:
                try {
                    for (Integer studentId : messenger.getReceiverIds()) {
                        Student student = studentAPI.getStudent(studentId);
                        GradeClass schoolClass = schoolClassAPI.getSchoolGradeClass(student.getClassId());
                        List<Guardian> guardians = studentAPI.getStudentGuardians(student.getId());
                        for (Guardian g : guardians) {
                            if(Strings.isNullOrEmpty(messenger.getMediaUrl())) {
                                // 普通文本消息
                                Notice notice = new Notice(messenger.getMessageId(),
                                        messenger.getSchoolId(), messenger.getSchoolName(),
                                        messenger.getSenderId(), messenger.getSenderName(),
                                        messenger.getSenderAvatar(),
                                        schoolClass.getId(), schoolClass.getName(), studentId, student.getName(),
                                        g.getId(), g.getMobile(), g.getWechat() == null ? null : g.getWechat().getOpenid(),
                                        messenger.getSignature(),
                                        messenger.getCreateTime(), messenger.getScheduleTime(),
                                        messenger.getContent()
                                );

                                collector.emit(tuple, new Values(notice));
                            }else {
                                // 多媒体消息
                                com.schoolguard.message.storage.shared.model.MediaType noticeMediaType = null;
                                switch (messenger.getMediaType()){
                                    case AUDIO:
                                        noticeMediaType = MediaType.AUDIO; break;
                                    case PICTURE:
                                        noticeMediaType = MediaType.PICTURE; break;
                                }

                                Notice notice = new Notice(messenger.getMessageId(),
                                        messenger.getSchoolId(), messenger.getSchoolName(),
                                        messenger.getSenderId(), messenger.getSenderName(),
                                        messenger.getSenderAvatar(),
                                        schoolClass.getId(), schoolClass.getName(), studentId, student.getName(),
                                        g.getId(), g.getMobile(), g.getWechat() == null ? null : g.getWechat().getOpenid(),
                                        messenger.getSignature(),
                                        messenger.getCreateTime(), messenger.getScheduleTime(),
                                        noticeMediaType, messenger.getMediaUrl()
                                );

                                collector.emit(tuple, new Values(notice));
                            }
                        }
                    }
                } catch (RestApiException e){
                    logger.error("API请求出错：", e);
                    collector.fail(tuple);
                    return;
                } catch (Exception e){
                    logger.error("发现运行时异常,系统将跳过异常的消息：", e);
                    collector.ack(tuple);
                    return;
                }
                break;

            default:
                throw new RuntimeException("Unknown notice receiver type");
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("notice"));
    }
}
