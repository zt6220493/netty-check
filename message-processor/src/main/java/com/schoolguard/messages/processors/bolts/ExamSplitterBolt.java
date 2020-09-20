package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.inject.Injector;
import com.schoolguard.core.ws.client.RestApiException;
import com.schoolguard.core.ws.client.SchoolClassAPI;
import com.schoolguard.core.ws.client.StudentAPI;
import com.schoolguard.core.ws.client.domain.GradeClass;
import com.schoolguard.core.ws.client.domain.Guardian;
import com.schoolguard.core.ws.client.domain.Student;
import com.schoolguard.message.storage.shared.model.Exam;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.ExamMessenger;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Rogers on 15-5-5.
 */
public class ExamSplitterBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(ExamSplitterBolt.class);
    private OutputCollector collector;

    private SchoolClassAPI schoolClassAPI;
    private StudentAPI studentAPI;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        this.schoolClassAPI = injector.getInstance(SchoolClassAPI.class);
        this.studentAPI = injector.getInstance(StudentAPI.class);
    }

    @Override
    public void execute(Tuple tuple){
        byte[] bytes = tuple.getBinary(0);
        ExamMessenger messenger = (ExamMessenger) ExamMessenger.fromBytes(bytes);
        try {
            for (ExamMessenger.ExamScore examScore : messenger.getExamScores()) {
                Student student = studentAPI.getStudent(examScore.getStudentId());
                GradeClass gradeClass = schoolClassAPI.getSchoolGradeClass(student.getClassId());
                List<Guardian> guardians = studentAPI.getStudentGuardians(student.getId());
                for (Guardian g : guardians) {
                    Exam exam = new Exam(messenger.getMessageId(),
                            messenger.getSchoolId(), messenger.getSchoolName(),
                            messenger.getSenderId(), messenger.getSenderName(), messenger.getSenderAvatar(),
                            gradeClass.getId(), gradeClass.getName(),
                            student.getId(), student.getName(),
                            g.getId(), g.getMobile(), g.getWechat() == null ? null :g.getWechat().getOpenid(),
                            messenger.getSignature(),
                            messenger.getCreateTime(), messenger.getScheduleTime());
                    exam.setContent(messenger.getMessageId(), messenger.getExamName(),
                            examScore.getTotalScore().orNull(), examScore.getRank().orNull(),
                            examScore.getScores(),examScore.getComment().orNull());
                    collector.emit(tuple, new Values(exam));
                }
            }
        } catch (RestApiException e){
            logger.error("Api Request failed: ", e);
            collector.fail(tuple);
            return;
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("exam"));
    }
}
