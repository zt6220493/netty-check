package com.schoolguard.messages.gateway.sms.service.impl;

import com.schoolguard.messages.gateway.GatewayConfig;
import com.schoolguard.messages.gateway.sms.config.SmsConstants;
import com.schoolguard.messages.gateway.sms.service.MTClient;
import com.schoolguard.messages.gateway.sms.utils.SmsHelper;
import com.schoolguard.messages.gateway.sms.utils.SmsRequest;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * 短信下行
 * @author Roma
 * @datetime 2015/8/26 - 22:44
 */
public class MTClientImpl implements MTClient {
    private static final Logger logger = Logger.getLogger(MTClientImpl.class);
    private static GatewayConfig gatewayConfig;

    static {
        gatewayConfig = new GatewayConfig();
    }

    /**
     * 单条下行请求：
     *   1）组装参数
     *   2）发送请求并获取响应消息
     *   3）解析响应消息，并返回
     * @param phone   目标号码
     * @param content 短信内容（已过滤,以HEX编码的字符串，编码前最长140个字符)
     * @param pt      优先级[1~10]-{数字越大，优先级越高}
     * @param at      定时发送时间-{yyyyMMddHHmmss --- 例子：20070301150000}
     * @param vt      有效时间-{yyyyMMddHHmmss --- 例子：20070301150000}
     * @return boolean
     */
    @Override
    public boolean singleMT(String phone, String content,
                         String pt, String at,
                         String vt) {
        phone = validateMobileFormat(phone);

        // 1 组装参数
        String command = SmsConstants.COMMAND_MT_REQUEST;   // 操作命令（必填）
        String spid = gatewayConfig.getSmsSpUsername();                    // SP编号（必填）
        String sppassword = gatewayConfig.getSmsSpPassword();        // SP密码（必填）
        int dc = SmsConstants.DC_GBK;                       // 编码格式（必填）
        String da = phone;                                  // 目标号码（必填）
        String sm = SmsHelper.encodeHexStr(dc, content);    // 下行内容进行Hex编码（必填）

        // sm长度超过限制处理 TODO: 先判断移通是否真的不支持超过140
        if (sm.length() > SmsConstants.SM_MAX_LENGTH) {
            // 将sm以最大140个字符切割成多条短信发送
            int SmsSize;
            if (content.length() % SmsConstants.SM_MAX_LENGTH == 0) {
                SmsSize = content.length() / SmsConstants.SM_MAX_LENGTH;
            } else {
                SmsSize = content.length() / SmsConstants.SM_MAX_LENGTH + 1;
            }
            String[] arr = new String[SmsSize];
            for (int i = 0; i < SmsSize; i++) {
                if (i == SmsSize - 1) {
                    arr[i] = content.substring(i * SmsConstants.SM_MAX_LENGTH , content.length());
                } else {
                    arr[i] = content.substring(i * SmsConstants.SM_MAX_LENGTH , (i + 1) * SmsConstants.SM_MAX_LENGTH);
                }
            }
            // 将一条短信切割成多条发送
            return this.multiMTWithDiffContent(new String[]{phone}, arr, null, null, null);
        }

        String spsc = "00";                                 // 默认情况下一个SP只需要一个业务，该参数可忽略
        String sa = "10657109053657";                       // 源号码（可不填）
        String priority = pt;                               // 优先级
        String attime = at;                                 // 定时发送时间
        String validtime = vt;                              // 有效时间

        StringBuffer sb = new StringBuffer();
        sb.append(gatewayConfig.getSmsSpServer());
        sb.append("?command=" + command);
        sb.append("&spid=" + spid);
        sb.append("&sppassword=" + sppassword);
        sb.append("&spsc=" + spsc);
        sb.append("&sa=" + sa);
        sb.append("&da=" + da);
        sb.append("&sm=" + sm);
        sb.append("&dc=" + dc);
        // 是否提供额外参数
        if (!SmsHelper.isNull(priority)) {
            sb.append("&priority=" + priority);
        }
        if (!SmsHelper.isNull(attime)) {
            sb.append("&attime=" + attime);
        }
        if (!SmsHelper.isNull(validtime)) {
            sb.append("&validtime=" + validtime);
        }
        logger.debug("单条下行请求消息：" + sb.toString());

        // 2 发送下行请求并获取响应消息
        String mtRespMessage = SmsRequest.doPostRequest(sb.toString());
        logger.info("单条下行请求响应消息：" + mtRespMessage);

        // 3 解析响应消息
        HashMap respMap = SmsHelper.parseResStr(mtRespMessage);

        return "ACCEPTD".equals(respMap.get("mtstat")) && "000".equals(respMap.get("mterrcode"));
    }

    /**
     * 下行群发（不同内容）
     *    1）组装参数
     *    2）发送请求并获取响应消息
     *    3）解析响应消息，并返回
     * @param phones    目标号码
     * @param contents  短信内容（已过滤,以HEX编码的字符串，编码前最长140个字符)
     * @param pt        优先级[1~10]-{数字越大，优先级越高}
     * @param at        定时发送时间-{yyyyMMddHHmmss --- 例子：20070301150000}
     * @param vt        有效时间-{yyyyMMddHHmmss --- 例子：20070301150000}
     * @return boolean
     */
    @Override
    public boolean multiMTWithDiffContent(String[] phones, String[] contents, String pt, String at, String vt) {
        for(int i=0; i<phones.length; i++){
            phones[i] = validateMobileFormat(phones[i]);
        }

        // 1 组装参数
        String command = SmsConstants.COMMAND_MULTIX_MT_REQUEST;
        String spid = gatewayConfig.getSmsSpUsername();
        String sppassword = gatewayConfig.getSmsSpPassword();
        String spsc = "00";
        String sa = "";
        int dc = SmsConstants.DC_GBK;
        //下行号码、内容列表
        StringBuffer dasms = new StringBuffer();
        int smsCount = contents.length;
        if (phones.length == 1) { // 相同用户，不同内容群发
            for (int i = 0; i <smsCount; i++) {
                if (i == 0) {
                    dasms.append(phones[0] + "/");
                    dasms.append(SmsHelper.encodeHexStr(dc, contents[i]));
                } else {
                    dasms.append("," + phones[0] + "/");
                    dasms.append(SmsHelper.encodeHexStr(dc, contents[i]));
                }
            }
        } else { // 不同用户，不同内容群发
            for (int i = 0; i < smsCount; i++) {
                if (i == 0) {
                    dasms.append(phones[i] + "/");
                    dasms.append(SmsHelper.encodeHexStr(dc, contents[i]));
                } else {
                    dasms.append("," + phones [i] + "/");
                    dasms.append(SmsHelper.encodeHexStr(dc, contents[i]));
                }
            }
        }

        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(gatewayConfig.getSmsSpServer());
        sb.append("?command=" + command);
        sb.append("&spid=" + spid);
        sb.append("&sppassword=" + sppassword);
        sb.append("&spsc=" + spsc);
        sb.append("&sa=" + sa);
        sb.append("&dasm=" + dasms.toString());
        sb.append("&dc=" + dc);
        // 是否提供额外参数
        if (!SmsHelper.isNull(pt)) {
            sb.append("&priority=" + pt);
        }
        if (!SmsHelper.isNull(at)) {
            sb.append("&attime=" + at);
        }
        if (!SmsHelper.isNull(vt)) {
            sb.append("&validtime=" + vt);
        }
        // 打印url
        logger.debug("批量下行请求url：" + sb.toString());

        // 2 发送请求并获取响应消息
        String mtRespMessage = SmsRequest.doPostRequest(sb.toString());
        logger.debug("单条下行请求响应消息：" + mtRespMessage);

        // 3 解析响应字符串
        HashMap respMap = SmsHelper.parseResStr(mtRespMessage);

        return "ACCEPTD".equals(respMap.get("mtstat")) && "000".equals(respMap.get("mterrcode"));
    }

    @Override
    public void multiMTWithSameContent() {

    }

    @Override
    public void fileBatchMT() {

    }

    private String validateMobileFormat(String mobile){
        if(mobile.startsWith("86")){
            return mobile;
        }

        return "86" + mobile;
    }

    /**
     * Main Test
     * @param args
     */
    public static void main(String[] args) {
        MTClientImpl client = new MTClientImpl();
        String content = "aaaaaasdfghaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa测测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        client.singleMT("13570295580", content, null, null ,null);
//        System.out.println(281%140);
    }
}
