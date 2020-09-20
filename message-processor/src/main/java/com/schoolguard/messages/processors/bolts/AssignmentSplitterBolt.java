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
import com.schoolguard.message.storage.shared.model.Assignment;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.AssignmentMessenger;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Rogers on 15-5-5.
 */
public class AssignmentSplitterBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(AssignmentSplitterBolt.class);
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
        AssignmentMessenger messenger = (AssignmentMessenger) AssignmentMessenger.fromBytes(bytes);

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
                        Assignment assignment = new Assignment(messenger.getMessageId(),
                                messenger.getSchoolId(), messenger.getSchoolName(),
                                messenger.getSenderId(), messenger.getSenderName(),
                                messenger.getSenderAvatar(),
                                classId, schoolClass.getName(), student.getId(), student.getName(),
                                g.getId(), g.getMobile(), g.getWechat() == null ? null :g.getWechat().getOpenid(),
                                messenger.getSignature(),
                                messenger.getCreateTime(), messenger.getScheduleTime(),
                                messenger.getContent()
                        );
                        collector.emit(tuple, new Values(assignment));
                    }
                }
            }
        } catch (RestApiException e){
            logger.error("API request failed: ", e);
            collector.fail(tuple);
            return;
        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("assignment"));
    }
}
