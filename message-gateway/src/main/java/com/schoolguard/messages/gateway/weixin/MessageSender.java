package com.schoolguard.messages.gateway.weixin;


import com.schoolguard.messages.gateway.GatewayConfig;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Rogers on 15-5-17.
 */
public class MessageSender {
    private static GatewayConfig gatewayConfig;

    static {
        gatewayConfig = new GatewayConfig();
    }

    private String accessToken;

    public MessageSender(String accessToken) {
        this.accessToken = accessToken;
    }

    public void sendOaMessage(String openid, String msgUrl, Map<String, String> dataMap) throws WeixinAPIException {
        send(gatewayConfig.getTeacherWeixinOaMessageMsgTemplateId(), openid, msgUrl, dataMap);
    }

    public void sendNotice(String openid,
                                  String msgUrl,
                                  Map<String, String> dataMap) throws WeixinAPIException {
        send(gatewayConfig.getGuardianWeixinNoticeMsgTemplateId(), openid, msgUrl, dataMap);
    }

    public void sendAssignment(String openid,
                           String msgUrl,
                           Map<String, String> dataMap) throws WeixinAPIException {
        send(gatewayConfig.getGuardianWeixinAssignmentMsgTemplateId(), openid, msgUrl, dataMap);
    }


    public void sendExam(String openid,
                               String msgUrl,
                               Map<String, String> dataMap) throws WeixinAPIException {
        send(gatewayConfig.getGuardianWeixinExamMsgTemplateId(), openid, msgUrl, dataMap);
    }


    public void sendAttendance(String openid,
                         String msgUrl,
                         Map<String, String> dataMap) throws WeixinAPIException {
        send(gatewayConfig.getGuardianWeixinAttendanceMsgTemplateId(), openid, msgUrl, dataMap);
    }

    private void send(String templateId, String openid, String msgUrl, Map<String, String> dataMap)
            throws WeixinAPIException {
        TemplateMessageData data = new TemplateMessageData(openid, msgUrl, templateId, dataMap);
        TemplateMessage msg = new TemplateMessage(data, accessToken);
        try {
            msg.sendMessage();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
