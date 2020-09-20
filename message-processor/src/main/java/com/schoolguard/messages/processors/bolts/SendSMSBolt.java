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
import com.schoolguard.messages.gateway.sms.service.impl.MTClientImpl;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.JobType;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by Rogers on 15-5-4.
 */
public class SendSMSBolt extends BaseRichBolt {
    private static Logger logger = Logger.getLogger(SendSMSBolt.class);
    private OutputCollector collector;

    /** 时间日期格式化工具 */
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    /** ES持久化 */
    private PersistenceAPI persistence;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        persistence = injector.getInstance(PersistenceAPI.class);
    }

    @Override
    public void execute(Tuple tuple) {

        JobType jobType = (JobType) tuple.getValueByField("type");
        MTClientImpl smsSender = new MTClientImpl();

        String smsContent = ""; // 短信内容
        Status status = null;

        switch (jobType) {
            // 通知
            case NOTICE:
                Notice notice = (Notice) tuple.getValueByField("data");
//                try {
//                    // 1 获取通知内容
//                    String textContent = notice.getContent();
//                    if (Strings.isNullOrEmpty(textContent)) {
//                        textContent = "您有新的图片消息，请关注微信号【校园卫士互动版】，绑定学生信息后查看，如已关注请忽略。";
//                    }
//                    // 2 获取短信内容
//                    smsContent = SmsTemplate.getTplOfNtcAsmExam(SmsTemplate.TPL_NOTICE, notice.getStudentName(), textContent, notice.getSignature(), notice.getSchoolName());
//                    // 3 发送
//                    boolean result = smsSender.singleMT(notice.getGuardianMobile(), smsContent, null, null, null);
//                    if (result) {
//                        // 接收成功，更新ES状态
//                        status = new Status(true, "短消息平台已接收请求");
//                        status.setCounter(notice.getStatus().getCounter() + 1);
//                    } else {
//                        // 接收失败，更新ES状态
//                        status = new Status(false, "消息有错，短消息平台不予以接收该请求。");
//                        status.setCounter(notice.getStatus().getCounter() + 1);
//                    }
//                } catch (Exception e) {
//                    logger.error("短信接口发送异常", e);
//                    this.collector.reportError(e);
//                    this.collector.fail(tuple);
//                    return;
//                }


                status = new Status(false, "校园卫士平台暂时不提供免费短信支持，请等待开放");

                // 4 无论发送成功与否，更新状态
                try {
                    persistence.updateStatus(Notice.class, notice.getId(), status);
                }catch (ESApiException e){
                    logger.error("更新状态失败", e);
                }
                this.collector.ack(tuple);
                break;

            // 作业
            case ASSIGNMENT:
                Assignment assignment = (Assignment) tuple.getValueByField("data");
//                try {
//                    // 2 获取短信内容
//                    smsContent = SmsTemplate.getTplOfNtcAsmExam(SmsTemplate.TPL_ASSIGNMENT, assignment.getStudentName(), assignment.getContent(), assignment.getSignature(), assignment.getSchoolName());
//                    // 3 发送
//                    boolean result = smsSender.singleMT(assignment.getGuardianMobile(), smsContent, null, null, null);
//                    if (result) {
//                        // 接收成功，更新ES状态
//                        status = new Status(true, "短消息平台已接收请求");
//                        status.setCounter(assignment.getStatus().getCounter() + 1);
//                    } else {
//                        // 接收失败，更新ES状态
//                        status = new Status(false, "消息有错，短消息平台不予以接收该请求。");
//                        status.setCounter(assignment.getStatus().getCounter() + 1);
//                    }
//
//                } catch (Exception e) {
//                    logger.error("短信接口发送异常", e);
//                    this.collector.reportError(e);
//                    this.collector.fail(tuple);
//                    return;
//                }

                status = new Status(false, "校园卫士平台暂时不提供免费短信支持，请等待开放");

                // 4 无论发送成功与否，更新状态
                try {
                    persistence.updateStatus(Assignment.class, assignment.getId(), status);
                }catch (ESApiException e){
                    logger.error("更新状态失败", e);
                }
                this.collector.ack(tuple);
                break;

            // 成绩
            case EXAM:
                Exam exam = (Exam) tuple.getValueByField("data");
//                try {
//                    // 1 获取成绩内容（涉及两个模板）
//                    Exam.ExamContent ec = exam.getContent();
//                    String examMessage = SmsTemplate.getTplOfExam(ec.getExamName(), ec.getScores(), ec.getTotalScore(), ec.getRank());
//                    smsContent = SmsTemplate.getTplOfNtcAsmExam(SmsTemplate.TPL_EXAM, exam.getStudentName(), examMessage, exam.getSignature(), exam.getSchoolName());
//                    // 2 发送
//                    boolean result = smsSender.singleMT(exam.getGuardianMobile(), smsContent, null, null, null);
//                    if (result) {
//                        // 接收成功，更新ES状态
//                        status = new Status(true, "短消息平台已接收请求");
//                        status.setCounter(exam.getStatus().getCounter() + 1);
//                    } else {
//                        // 接收失败，更新ES状态
//                        status = new Status(false, "消息有错，短消息平台不予以接收该请求。");
//                        status.setCounter(exam.getStatus().getCounter() + 1);
//                    }
//                } catch (Exception e) {
//                    logger.error("短信接口发送异常", e);
//                    this.collector.reportError(e);
//                    this.collector.fail(tuple);
//                    return;
//                }


                status = new Status(false, "校园卫士平台暂时不提供免费短信支持，请等待开放");

                // 4 无论发送成功与否，更新状态
                try {
                    persistence.updateStatus(Exam.class, exam.getId(), status);
                }catch (ESApiException e){
                    logger.error("更新状态失败", e);
                }
                this.collector.ack(tuple);
                break;

            // OA消息
            case OA_MSG:
                OAMessage oa = (OAMessage) tuple.getValueByField("data");
//                try {
//                    // 1 获取OA消息内容
//                    String oaMessage = oa.getContent();
//                    if (SmsHelper.isNull(oaMessage)) {
//                        throw new RuntimeException();
//                    }
//                    // 2 获取OA短信内容
//                    smsContent = SmsTemplate.getTplOfOAMessage(SmsTemplate.TPL_OA_MESSAGE, oa.getReceiverName(), oaMessage, oa.getSenderName(), oa.getSchoolName());
//                    // 3 发送
//                    boolean result = smsSender.singleMT(oa.getReceiverMobile(), smsContent, null, null, null);
//                    if (result) {
//                        // 接收成功，更新ES状态
//                        status = new Status(true, "短消息平台已接收请求");
//                        status.setCounter(oa.getStatus().getCounter() + 1);
//                    } else {
//                        // 接收失败，更新ES状态
//                        status = new Status(false, "消息有错，短消息平台不予以接收该请求。");
//                        status.setCounter(oa.getStatus().getCounter() + 1);
//                    }
//                } catch (Exception e) {
//                    logger.error("短信接口发送异常", e);
//                    this.collector.reportError(e);
//                    this.collector.fail(tuple);
//                    return;
//                }

                status = new Status(false, "校园卫士平台暂时不提供免费短信支持，请等待开放");

                // 4 无论发送成功与否，更新状态
                try {
                    persistence.updateStatus(OAMessage.class, oa.getId(), status);
                }catch (ESApiException e){
                    logger.error("更新状态失败", e);
                }
                this.collector.ack(tuple);
                break;

            // 考勤
            case ATTENDANCE:
                Attendance attendance = (Attendance) tuple.getValueByField("data");
//                try {
//                    // 1 获取考勤短信内容
//                    smsContent = SmsTemplate.getTplOfAttendance(attendance.getStudentName(), formatter.format(attendance.getCheckinTime()), attendance.getLocation());
//                    // 2 发送
//                    boolean result = smsSender.singleMT(attendance.getGuardianMobile(), smsContent, null, null, null);
//                    if (result) {
//                        // 接收成功，更新ES状态
//                        status = new Status(true, "短消息平台已接收请求");
//                        status.setCounter(attendance.getStatus().getCounter() + 1);
//                    } else {
//                        // 接收失败，更新ES状态
//                        status = new Status(false, "消息有错，短消息平台不予以接收该请求。");
//                        status.setCounter(attendance.getStatus().getCounter() + 1);
//                    }
//                } catch (Exception e) {
//                    logger.error("短信接口发送异常", e);
//                    this.collector.reportError(e);
//                    this.collector.fail(tuple);
//                    return;
//                }

                status = new Status(false, "校园卫士平台暂时不提供免费短信支持，请等待开放");

                // 4 无论发送成功与否，更新状态
                try {
                    persistence.updateStatus(Attendance.class, attendance.getId(), status);
                }catch (ESApiException e){
                    logger.error("更新状态失败", e);
                }
                this.collector.ack(tuple);
                break;

            default:
                throw new RuntimeException("未知的消息类型");
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}



