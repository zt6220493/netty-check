package com.schoolguard.messages.producers;

/**
 * Created by Rogers on 15-5-22.
 */
public enum Topic {
    NOTICE_TOPIC("topic_notices"),
    ASSIGNMENT_TOPIC("topic_assignments"),
    EXAM_TOPIC("topic_exams"),
    OA_TOPIC("topic_oa"),
    ATTENDANCE_TOPIC("topic_attendances"),
    // 短信
    COMMON_SMS_TOPIC("topic_sms"),

    //预处理后，待发送前入队列的消息
    PRE_SEND_NOTICE("topic_notice2"),
    PRE_SEND_ASSIGNMENT("topic_assignment2"),
    PRE_SEND_EXAM("topic_exam2"),
    PRE_SEND_OA_MSG("topic_oa2"),
    PRE_SEND_ATTENDANCE("topic_attendance2");

    private String topicName;

    private Topic(String topicName){
        this.topicName = topicName;
    }

    @Override
    public String toString(){
        return this.topicName;
    }
}
