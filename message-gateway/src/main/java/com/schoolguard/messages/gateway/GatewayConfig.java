package com.schoolguard.messages.gateway;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Rogers on 15/9/25.
 */
public class GatewayConfig {
    private static final String configFile = "gateway.properties";

    private String guardianWeixinNoticeMsgTemplateId;
    private String guardianWeixinAssignmentMsgTemplateId;
    private String guardianWeixinExamMsgTemplateId;
    private String guardianWeixinAttendanceMsgTemplateId;

    private String teacherWeixinOaMessageMsgTemplateId;

    private String smsSpServer;
    private String smsSpUsername;
    private String smsSpPassword;

    public GatewayConfig(){
        Properties config = loadProperties(configFile);
        this.guardianWeixinNoticeMsgTemplateId = config.getProperty("wx.guardian.noticeTemplateMsgId");
        this.guardianWeixinAssignmentMsgTemplateId = config.getProperty("wx.guardian.assignmentTemplateMsgId");
        this.guardianWeixinExamMsgTemplateId = config.getProperty("wx.guardian.examTemplateMsgId");
        this.guardianWeixinAttendanceMsgTemplateId = config.getProperty("wx.guardian.attendanceTemplateMsgId");
        this.teacherWeixinOaMessageMsgTemplateId = config.getProperty("wx.teacher.oaMessageTemplateMsgId");
        this.smsSpServer = config.getProperty("sms.sp.serverUrl");
        this.smsSpUsername = config.getProperty("sms.sp.username");
        this.smsSpPassword = config.getProperty("sms.sp.password");
    }

    public String getGuardianWeixinNoticeMsgTemplateId() {
        return guardianWeixinNoticeMsgTemplateId;
    }

    public String getGuardianWeixinAssignmentMsgTemplateId() {
        return guardianWeixinAssignmentMsgTemplateId;
    }

    public String getGuardianWeixinExamMsgTemplateId() {
        return guardianWeixinExamMsgTemplateId;
    }

    public String getGuardianWeixinAttendanceMsgTemplateId() {
        return guardianWeixinAttendanceMsgTemplateId;
    }

    public String getTeacherWeixinOaMessageMsgTemplateId() {
        return teacherWeixinOaMessageMsgTemplateId;
    }

    public String getSmsSpServer() {
        return smsSpServer;
    }

    public String getSmsSpUsername() {
        return smsSpUsername;
    }

    public String getSmsSpPassword() {
        return smsSpPassword;
    }

    private Properties loadProperties(String propertiesFile){
        Properties configProperties = new Properties();
        try{
            configProperties.load(GatewayConfig.class.getClassLoader().getResourceAsStream(propertiesFile));
        } catch (IOException exception){
            throw new RuntimeException("Load properties failed: " + exception.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return configProperties;
    }

}
