package com.schoolguard.messages.gateway.sms.config;

/**
 * 短信接口相关常量
 * @author Roma
 * @datetime 2015/8/26 - 22:32
 */
public class SmsConstants {

//    /** 短消息平台下行请求地址 */
////    public static final String mtUrl = "http://esms.etonenet.com/sms/mt";
//    public static final String mtUrl = "http://esms4.10690007.net/sms/mt";
//
//    /** sp账号密码 */
//    public static final String SPID = "100036";
//    public static final String SPPASSWORD = "gz0629dskj";

    /** 消息编码格式 */
    public static final int DC_GBK = 15;             // GBK编码
    public static final int DC_UNICODE = 8;          // Unicode(UTF_16BE)编码
    public static final int DC_ISO_8859_1 = 0;       // ISO_8859_1编码

    /** 操作命令 */
    public static final String COMMAND_MT_REQUEST = "MT_REQUEST";    // 下行请求
    public static final String COMMAND_MT_RESPONSE = "MT_RESPONSE";  // 下行响应

    public static final String COMMAND_MULTI_MT_REQUEST = "MULTI_MT_REQUEST"; // 批量下行请求消息(相同内容)
    public static final String COMMAND_MULTI_MT_RESPONSE = "MULTI_MT_RESPONSE"; // 批量下行响应消息(相同内容)

    public static final String COMMAND_MULTIX_MT_REQUEST = "MULTIX_MT_REQUEST"; // 批量下行请求消息(不同内容)
    public static final String COMMAND_MULTIX_MT_RESPONSE = "MULTIX_MT_RESPONSE"; // 批量下行响应消息(不同内容)

    public static final String COMMAND_BATCH_MT_REQUEST = "BATCH_MT_REQUEST"; // 文件群发下行请求消息
    public static final String COMMAND_BATCH_MT_RESPONSE = "BATCH_MT_RESPONSE"; // 文件群发下行响应消息

    public static final String COMMAND_MO_REQUEST = "MO_REQUEST"; // 上行请求消息
    public static final String COMMAND_MO_RESPONSE = "MO_RESPONSE"; // 上行响应消息

    public static final String COMMAND_RT_REQUEST = "RT_REQUEST"; // 状态报告请求消息
    public static final String COMMAND_RT_RESPONSE = "RT_RESPONSE"; // 状态报告响应消息

    public static final String COMMAND_ERROR_RESPONSE = "ERROR_RESPONSE"; // 错误响应消息


    /** 每条短信最大字符长度 */
    public static final int SM_MAX_LENGTH = 500;        // 编码后
    public static final int CONTENT_MAX_LENGTH = 140;   // 编码前（以中文字符来计算）
}
