package com.schoolguard.messages.processors.bolts;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.schoolguard.core.ws.client.AccessTokenAPI;
import com.schoolguard.core.ws.client.RestApiException;
import com.schoolguard.core.ws.client.domain.TokenData;
import com.schoolguard.message.storage.client.ESApiException;
import com.schoolguard.message.storage.client.PersistenceAPI;
import com.schoolguard.message.storage.shared.model.*;
import com.schoolguard.messages.gateway.weixin.MessageSender;
import com.schoolguard.messages.gateway.weixin.WeixinAPIException;
import com.schoolguard.messages.processors.guice.InjectorProvider;
import com.schoolguard.messages.producers.messenger.JobType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rogers on 15-5-4.
 */
public class SendWeixinBolt extends BaseRichBolt {
    private static Logger logger = LoggerFactory.getLogger(SendWeixinBolt.class);
    private OutputCollector collector;
    private AccessTokenAPI accessTokenAPI;
    private PersistenceAPI persistence;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String noticeUrl;
    private String assignmentUrl;
    private String examUrl;
    private String attendanceUrl;
    private String oaMessageUrl;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        Injector injector = InjectorProvider.get();
        accessTokenAPI = injector.getInstance(AccessTokenAPI.class);
        persistence = injector.getInstance(PersistenceAPI.class);
        noticeUrl = injector.getInstance(Key.get(String.class, Names.named("wx.guardian.noticeUrlTpl")));
        assignmentUrl = injector.getInstance(Key.get(String.class, Names.named("wx.guardian.assignmentUrlTpl")));
        examUrl = injector.getInstance(Key.get(String.class, Names.named("wx.guardian.examUrlTpl")));
        attendanceUrl = injector.getInstance(Key.get(String.class, Names.named("wx.guardian.attendanceUrlTpl")));
        oaMessageUrl = injector.getInstance(Key.get(String.class, Names.named("wx.teacher.oaMessageUrlTpl")));
    }

    @Override
    public void execute(Tuple tuple) {
        JobType jobType = (JobType) tuple.getValueByField("type");
        String accessToken = null;
        try {
            TokenData token = accessTokenAPI.getGuardianWxToken();
            accessToken = token.getToken();
        }catch (RestApiException e){
            logger.error("获取Access Token失败：", e);
            this.collector.fail(tuple);
            return;
        }

        MessageSender sender = new MessageSender(accessToken);
        Map<String, String> dataMap = new HashMap<>();
        Status status = null;

        switch (jobType){
            case NOTICE:
                Notice notice = (Notice) tuple.getValueByField("data");
                dataMap.clear();
                dataMap.put("first", String.format("校园卫士温馨提示：%s有一条新的通知。", notice.getStudentName()));
                dataMap.put("keyword1", notice.getSchoolName()+"-"+notice.getClassName());
                dataMap.put("keyword2", notice.getSignature());
                if (notice.getScheduleTime() != null) {
                    dataMap.put("keyword3", formatter.format(notice.getScheduleTime()));
                } else {
                    dataMap.put("keyword3", formatter.format(notice.getCreateTime()));
                }
                dataMap.put("keyword4", notice.getContent() != null ? notice.getContent() : "多媒体消息");
                dataMap.put("remark", "请点击查看通知详情");

                try {
                    // 发送
                    String url = String.format(noticeUrl, notice.getStudentId());
                    sender.sendNotice(notice.getGuardianWeixin(), url, dataMap);

                    // 更新状态
                    status = new Status(true, "发送成功");
                    status.setCounter(notice.getStatus().getCounter() + 1);
                } catch (WeixinAPIException e) {
                    if (-1 == e.getCode() || 41006 == e.getCode()) {
                        //微信接口繁忙或access token失效
                        this.collector.reportError(e);
                        this.collector.fail(tuple);
                        return;
                    } else {
                        //其他失败原因不再重试
                        logger.error("微信API异常：", e);
                        status = new Status(false, "推送失败！");
                        status.setCounter(notice.getStatus().getCounter() + 1);
                    }
                }

                // 更新状态失败不重试，否则会引起重复发送
                try {
                    persistence.updateStatus(Notice.class, notice.getId(), status);
                } catch (ESApiException e2) {
                    logger.error("更新状态失败", e2);
                }
                this.collector.ack(tuple);
                break;

            case ASSIGNMENT:
                Assignment assignment = (Assignment) tuple.getValueByField("data");
                dataMap.clear();
                dataMap.put("first", "校园卫士温馨提示:");
                dataMap.put("name", assignment.getStudentName());
                dataMap.put("subject", "家庭作业");
                dataMap.put("content", assignment.getContent());
                dataMap.put("remark", "请督促孩子完成.");

                try {
                    // 发送
                    String url = String.format(assignmentUrl, assignment.getStudentId());
                    sender.sendAssignment(assignment.getGuardianWeixin(), url, dataMap);

                    // 更新状态
                    status = new Status(true, "发送成功");
                    status.setCounter(assignment.getStatus().getCounter() + 1);
                } catch (WeixinAPIException e) {
                    if (-1 == e.getCode() || 41006 == e.getCode()) {
                        //微信接口繁忙或access token失效
                        this.collector.reportError(e);
                        this.collector.fail(tuple);
                        return;
                    } else {
                        //其他失败原因不再重试
                        logger.error("微信API异常：", e);
                        status = new Status(false, "推送失败！");
                        status.setCounter(assignment.getStatus().getCounter() + 1);
                    }
                }

                try {
                    persistence.updateStatus(Assignment.class, assignment.getId(), status);
                } catch (ESApiException e2) {
                    logger.error("更新状态失败", e2);
                }
                this.collector.ack(tuple);
                break;

            case EXAM:
                Exam exam = (Exam) tuple.getValueByField("data");
                dataMap.clear();
                dataMap.put("first", "校园卫士温馨提示:");
                dataMap.put("childName", exam.getStudentName());
                dataMap.put("courseName", exam.getContent().getExamName());
                dataMap.put("score", "请点击菜单查看详情");
                dataMap.put("remark", "发送人：" + exam.getTeacherName());

                try {
                    // 发送
                    String url = String.format(examUrl, exam.getStudentId());
                    sender.sendExam(exam.getGuardianWeixin(), url, dataMap);
                    // 更新状态
                    status = new Status(true, "发送成功");
                    status.setCounter(exam.getStatus().getCounter() + 1);
                } catch (WeixinAPIException e) {
                    if (-1 == e.getCode() || 41006 == e.getCode()) {
                        //微信接口繁忙或access token失效
                        this.collector.reportError(e);
                        this.collector.fail(tuple);
                        return;
                    } else {
                        //其他失败原因不再重试
                        logger.error("微信API异常：", e);
                        status = new Status(false, "推送失败！");
                        status.setCounter(exam.getStatus().getCounter() + 1);
                    }
                }

                try {
                    persistence.updateStatus(Exam.class, exam.getId(), status);
                } catch (ESApiException e2) {
                    logger.error("更新状态失败", e2);
                }
                this.collector.ack(tuple);
                break;

            case OA_MSG:
                OAMessage oaMessage= (OAMessage) tuple.getValueByField("data");
                dataMap.clear();

                StringBuffer oaContent = new StringBuffer();
                if (oaMessage.getContent().length() > 20) {
                    oaContent = oaContent.append(oaMessage.getContent().substring(0, 20)).append("..");
                } else {
                    oaContent = oaContent.append(oaMessage.getContent());
                }

                dataMap.put("first", "校园卫士温馨提示:您收到一条OA消息");
                dataMap.put("keyword1", oaMessage.getSignature());
                if (oaMessage.getScheduleTime() != null) {
                    dataMap.put("keyword2", formatter.format(oaMessage.getScheduleTime()));
                } else {
                    dataMap.put("keyword2", formatter.format(oaMessage.getCreateTime()));
                }
                dataMap.put("keyword3", oaContent.toString());
                dataMap.put("remark", "请点击菜单查看详情");

                try {

                    TokenData token = accessTokenAPI.getTeacherWxToken();
                    accessToken = token.getToken();

                    // 发送
                    MessageSender oaSender = new MessageSender(accessToken);
                    String url = oaMessageUrl;
                    oaSender.sendOaMessage(oaMessage.getReceiverWeixin(), url, dataMap);
                    // 更新状态
                    status = new Status(true, "发送成功");
                    status.setCounter(oaMessage.getStatus().getCounter() + 1);
                } catch (WeixinAPIException e) {
                    if (-1 == e.getCode() || 41006 == e.getCode()) {
                        //微信接口繁忙或access token失效
                        this.collector.reportError(e);
                        this.collector.fail(tuple);
                        return;
                    } else {
                        //其他失败原因不再重试
                        logger.error("微信API异常：", e);
                        status = new Status(false, "推送失败！");
                        status.setCounter(oaMessage.getStatus().getCounter() + 1);
                    }
                } catch (RestApiException e){
                    logger.error("获取Access Token失败：", e);
                    this.collector.fail(tuple);
                    return;
                }

                try {
                    persistence.updateStatus(OAMessage.class, oaMessage.getId(), status);
                } catch (ESApiException e2) {
                    logger.error("更新状态失败", e2);
                }
                this.collector.ack(tuple);
                break;

            case ATTENDANCE:
                Attendance attendance = (Attendance) tuple.getValueByField("data");
                dataMap.clear();
                dataMap.put("first", "校园卫士温馨提示:");
                dataMap.put("name", attendance.getStudentName());
                dataMap.put("time", formatter.format(attendance.getCheckinTime()));
                dataMap.put("location", attendance.getLocation());
                dataMap.put("remark", "点击查看详情");

                try {
                    // 发送
                    String url = String.format(attendanceUrl, attendance.getStudentId());
                    sender.sendAttendance(attendance.getGuardianWeixin(), url, dataMap);

                    // 更新状态
                    status = new Status(true, "发送成功");
                    status.setCounter(attendance.getStatus().getCounter() + 1);
                } catch (WeixinAPIException e) {
                    if (-1 == e.getCode() || 41006 == e.getCode()) {
                        //微信接口繁忙或access token失效
                        this.collector.reportError(e);
                        this.collector.fail(tuple);
                        return;
                    } else {
                        //其他失败原因不再重试
                        logger.error("微信API异常：", e);
                        status = new Status(false, "推送失败！");
                        status.setCounter(attendance.getStatus().getCounter() + 1);
                    }
                }

                try {
                    persistence.updateStatus(Attendance.class, attendance.getId(), status);
                } catch (ESApiException e2) {
                    logger.error("更新状态失败", e2);
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

