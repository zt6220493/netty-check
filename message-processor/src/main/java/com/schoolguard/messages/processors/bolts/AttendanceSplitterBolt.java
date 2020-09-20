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
import com.schoolguard.core.ws.client.SchoolApi;
import com.schoolguard.core.ws.client.StudentAPI;
import com.schoolguard.core.ws.client.domain.Guardian;
import com.schoolguard.message.storage.shared.model.Attendance;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.AttendanceMessenger;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Rogers on 15-5-5.
 */
public class AttendanceSplitterBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(AttendanceSplitterBolt.class);
    private OutputCollector collector;

    private StudentAPI studentAPI;
    private SchoolApi schoolApi;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        this.studentAPI = injector.getInstance(StudentAPI.class);
        this.schoolApi = injector.getInstance(SchoolApi.class);
    }

    @Override
    public void execute(Tuple tuple){
        byte[] bytes = tuple.getBinary(0);
        AttendanceMessenger messenger = AttendanceMessenger.fromBytes(bytes);
        try {
            List<Guardian> guardians = studentAPI.getStudentGuardians(messenger.getStudentId());
            for(Guardian g: guardians){
                Attendance attendance = new Attendance(messenger.getSchoolId(),
                        messenger.getSchoolName(),
                        messenger.getStudentId(),
                        messenger.getStudentName(),
                        g.getId(), g.getMobile(), g.getWechat() == null ? null :g.getWechat().getOpenid(),
                        messenger.getImageUrl(),
                        messenger.getLocation(),
                        new Date(messenger.getTimestamp()));
                collector.emit(tuple, new Values(attendance));
            }
        } catch (RestApiException e){
            collector.reportError(e);
            collector.fail(tuple);
            return;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("attendance"));
    }
}
