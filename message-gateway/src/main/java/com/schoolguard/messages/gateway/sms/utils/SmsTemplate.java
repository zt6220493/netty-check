package com.schoolguard.messages.gateway.sms.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信模板
 * [通知]：{student_name}家长您好,{content}{signature}【{school_name}】
 * [作业]：{student_name}家长您好,{content}{signature}【{school_name}】
 * [成绩]：{student_name}家长您好,{content}{signature}【{school_name}】
 * [OA短信]:  {to_name}：您好！{message}{send_name}【{school_name}】
 * [考勤]：温馨提醒：尊敬的家长，你的孩子${学生姓名studentname}于${打卡时间cardtime[format=MM月dd日HH时mm分]}在${打卡时段使用cardtypename}刷卡通过${通道位置}.查看刷卡图像,请关注微信公众号“校园卫士”
 *     ## {student_name}代表学生姓名、{content}代表信息内容、{signature}代表短信署名、{school_name}代表学校名字
 *     ## {to_name}代表老师名字、{message}代表短信内容、{send_name}、{signature}代表短信署名
 * @author Roma
 * @datetime 2015/8/30 - 21:23
 */
public class SmsTemplate {

    /** 短信模板类型 */
    public static final String TPL_ASSIGNMENT = "作业";
    public static final String TPL_EXAM = "成绩";
    public static final String TPL_OA_MESSAGE = "OA短信";
    public static final String TPL_NOTICE = "通知";

    /**
     * 获取 [通知]、[作业]、[成绩] 短信发送模板
     * @param type          短信类型
     * @param studentName   学生姓名
     * @param content       信息内容
     * @param signature     短信署名
     * @param schoolName    学校名字
     * @return  String      短信内容
     */
    public static String getTplOfNtcAsmExam(String type, String studentName, String content, String signature, String schoolName) {
        StringBuffer tpl = new StringBuffer();
//        tpl.append("【" + type + "】");
        tpl.append(studentName);
        tpl.append("家长您好，");
        tpl.append(content);
        tpl.append("—");
        tpl.append(signature);
        tpl.append("[" + schoolName + "]");
        return tpl.toString();
    }

    /**
     * 获取[OA消息]短信发送模板
     * @param type          短信类型
     * @param to            接收短信的老师名字
     * @param message       信息内容
     * @param from          发送短信的老师名字
     * @param schoolName    学校名字
     * @return String       短信内容
     */
    public static String getTplOfOAMessage(String type, String to, String message, String from, String schoolName) {
        StringBuffer tpl = new StringBuffer();
//        tpl.append("[" + type + "]");
        tpl.append(to);
        tpl.append("：您好！");
        tpl.append(message);
        tpl.append("—");
        tpl.append(from);
        tpl.append("[" + schoolName + "]");
        return tpl.toString();
    }

    /**
     * 获取[考勤]短信发送模板
     * @param studentName       学生姓名
     * @param cardTime          打卡时间
     * @param location          通道位置
     * @return String           短信内容
     */
    public static String getTplOfAttendance(String studentName, String cardTime, String location) {
        StringBuffer tpl = new StringBuffer();
        tpl.append("温馨提示：尊敬的家长，您的孩子");
        tpl.append(studentName);
        tpl.append("于");
        tpl.append(cardTime);
        tpl.append("在打卡时段");
        // tpl.append(cardTypeName);
        tpl.append("刷卡通过");
        tpl.append(location);
        tpl.append("。查看刷卡图像,请关注微信公众号“校园卫士互动版”");
        return tpl.toString();
    }

    /**
     * 获取考试成绩模板
     * @param examName      考试名称
     * @param scores        各科分数, 形如："数学:67"
     * @param totalScore    总分
     * @param rank          排名（该字段为空不排名）
     * @return String       考试成绩模板
     */
    public static String getTplOfExam(String examName, List<String> scores, BigDecimal totalScore, Integer rank) {

        // 解析各科成绩 (Map->String)
        StringBuffer scoresBuffer = new StringBuffer();
        for(String s: scores){
            scoresBuffer.append(s);
            scoresBuffer.append(",");
        }

        StringBuffer tpl = new StringBuffer();
        tpl.append("您的孩子在");
        tpl.append(examName);
        tpl.append("考试中，各科成绩如下：");
        tpl.append(scores);
        if (totalScore != null) {
            tpl.append("总分：");
            tpl.append(totalScore);
        }
        if (rank != null) {
            tpl.append("，排名：");
            tpl.append(rank);
        }
        tpl.append("。");
        return tpl.toString();
    }

    /**
     * Main Test
     * @param args
     */
    public static void main(String[] args) {
        String sms = SmsTemplate.getTplOfNtcAsmExam("通知", "刘志明", "中秋节放假5天", "郑老师", "校园卫士");
        String oaSms = SmsTemplate.getTplOfOAMessage("OA消息", "小明老师", "你今天有空吗？帮我带一下课好吗？", "Mr.Zheng", "校园卫士");
        String atdSms = SmsTemplate.getTplOfAttendance("小王", "8月30日09时20分", "校园卫士");
        List<String> scores = Arrays.asList("语文:86", "数学:67");
        String examSms = SmsTemplate.getTplOfExam("期中考试", scores, new BigDecimal(298), 1);
        System.out.println(oaSms);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String tem = formatter.format(new Date());
//        System.out.println(tem);

//        /**
//         * jdk8新特性java.util.Optional
//         */
//        Optional<String> a = Optional.ofNullable("哈哈哈 sdf");
//        System.out.println(a.toString());
//        System.out.println(a.equals("哈哈哈 sdf"));
//        System.out.println(a.get());
//        System.out.println(a.isPresent());

//        try {
//            throw new RuntimeException();
//        } catch (Exception e) {
//            System.out.println(123);
//        }

    }

}
