package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.common.base.Strings;
import com.schoolguard.message.storage.shared.model.*;
import com.schoolguard.messages.producers.messenger.JobType;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Rogers on 15-5-4.
 */
public class PermissionCheckBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(PermissionCheckBolt.class);
    private OutputCollector collector;

    private static final String TIPS_0 = "已绑定微信";
    private static final String TIPS_1 = "未绑定微信和手机";
    private static final String TIPS_2 = "未找到手机号";
    private static final String TIPS_3 = "待发送";

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        switch (tuple.getSourceComponent()) {
            case BoltId.NOTICE_SPLITTER:
                Notice notice = (Notice) tuple.getValue(0);
                if ( !Strings.isNullOrEmpty(notice.getGuardianWeixin())) {
                    // 发送微信, 不发短信
                    notice.setMsgType(MsgType.WEIXIN);
                    notice.setStatus(new Status(false, TIPS_3));
                    this.collector.emit(tuple, new Values(notice, JobType.NOTICE, true));
                } else {
                    if (null == notice.getGuardianMobile() || notice.getGuardianMobile().isEmpty()) {
                        notice.setStatus(new Status(false, TIPS_1));
                        this.collector.emit(tuple, new Values(notice, JobType.NOTICE, false));
                    } else {
                        // 发短信
//                        notice.setMsgType(MsgType.SMS);
//                        notice.setStatus(new Status(false, TIPS_3));
//                        this.collector.emit(tuple, new Values(notice, JobType.NOTICE, true));
                    }
                }
                break;
            case BoltId.ASSIGNMENT_SPLITTER:
                Assignment assignment = (Assignment) tuple.getValue(0);
                if ( !Strings.isNullOrEmpty(assignment.getGuardianWeixin())) {
                    assignment.setMsgType(MsgType.WEIXIN);
                    assignment.setStatus(new Status(false, TIPS_3));
                    this.collector.emit(tuple, new Values(assignment, JobType.ASSIGNMENT, true));
                } else {
                    // 发短信
                    if (null == assignment.getGuardianMobile() ||  assignment.getGuardianMobile().isEmpty()) {
                        assignment.setStatus(new Status(false, TIPS_1));
                        this.collector.emit(tuple, new Values(assignment, JobType.ASSIGNMENT, false));
                    } else {
//                        assignment.setMsgType(MsgType.SMS);
//                        assignment.setStatus(new Status(false, TIPS_3));
//                        this.collector.emit(tuple, new Values(assignment, JobType.ASSIGNMENT, true));
                    }
                }
                break;

            case BoltId.EXAM_SPLITTER:
                Exam exam = (Exam) tuple.getValue(0);
                if ( !Strings.isNullOrEmpty(exam.getGuardianWeixin())) {
                    exam.setMsgType(MsgType.WEIXIN);
                    exam.setStatus(new Status(false, TIPS_3));
                    this.collector.emit(tuple, new Values(exam, JobType.EXAM, true));
                } else {
                    // 发短信
                    if (null == exam.getGuardianMobile() ||  exam.getGuardianMobile().isEmpty()) {
                        exam.setStatus(new Status(false, TIPS_1));
                        this.collector.emit(tuple, new Values(exam, JobType.EXAM, false));
                    } else {
//                        exam.setMsgType(MsgType.SMS);
//                        exam.setStatus(new Status(false, TIPS_3));
//                        this.collector.emit(tuple, new Values(exam, JobType.EXAM, true));
                    }
                }
                break;

            case BoltId.OA_MSG_SPLITTER:
                OAMessage oaMessage = (OAMessage) tuple.getValue(0);
                // 教师OA消息如有绑定微信，则发微信模板消息，否则发短信
                if (oaMessage.getReceiverWeixin() != null && !oaMessage.getReceiverWeixin().isEmpty()) {
                    logger.info("OA消息接收者绑定了微信：【" + oaMessage.getReceiverWeixin() + "】，直接发送微信模板消息");
                    oaMessage.setMsgType(MsgType.WEIXIN);
                    oaMessage.setStatus(new Status(false, TIPS_0));
                    this.collector.emit(tuple, new Values(oaMessage, JobType.OA_MSG, true));
                } else {
                    if (null == oaMessage.getReceiverMobile() ||  oaMessage.getReceiverMobile().isEmpty()) {
                        oaMessage.setStatus(new Status(false, TIPS_2));
                        this.collector.emit(tuple, new Values(oaMessage, JobType.OA_MSG, false));
                    } else {
//                        oaMessage.setMsgType(MsgType.SMS);
//                        oaMessage.setStatus(new Status(false, TIPS_3));
//                        this.collector.emit(tuple, new Values(oaMessage, JobType.OA_MSG, true));
                    }
                }
                break;

            case BoltId.ATTENDANCE_SPLITTER:
                Attendance attendance = (Attendance) tuple.getValue(0);
                if( !Strings.isNullOrEmpty(attendance.getGuardianWeixin())){
                    attendance.setMsgType(MsgType.WEIXIN);
                    attendance.setStatus(new Status(false, TIPS_3));
                    this.collector.emit(tuple, new Values(attendance, JobType.ATTENDANCE, true));
                } else {
                    if (null == attendance.getGuardianMobile() ||  attendance.getGuardianMobile().isEmpty()) {
                        attendance.setStatus(new Status(false, TIPS_1));
                        this.collector.emit(tuple, new Values(attendance, JobType.ATTENDANCE, false));
                    } else {
//                        attendance.setMsgType(MsgType.SMS);
//                        attendance.setStatus(new Status(false, TIPS_3));
//                        this.collector.emit(tuple, new Values(attendance, JobType.ATTENDANCE, true));
                    }

                }
                break;

            default:
                throw new RuntimeException("Unknown component source!");

        }

        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // result: boolean, 是否发送
        declarer.declare(new Fields("data", "type", "result"));
    }
}

