package com.schoolguard.messages.producers.messenger;

import com.google.common.base.Strings;

import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rogers on 15-9-3.
 */
public class SmsMessenger extends Messenger {
    //最小内容长度
    static int MIN_CONTENT_LENGTH = 5;
    //最大长度限制(只做初步限制)
    static int MAX_CONTENT_LENGTH = 140;

    //简单的手机号码校验:以1开头，有数字组成，且长度为11个字符的字符串即可认为是合法手机号码
    Pattern mobileFormat = Pattern.compile("^1\\d{10}$");

    private String mobile;
    private String content;

    public SmsMessenger(String mobile, String content){

        this.mobile = validateMobileFormat(mobile);
        if(Strings.isNullOrEmpty(content) || content.length() <= MIN_CONTENT_LENGTH){
            throw new IllegalArgumentException("短信内容为空或长度未超过最小长度（" + MIN_CONTENT_LENGTH + "个字符）限制!");
        }

        if(content.length() > MAX_CONTENT_LENGTH){
            throw new IllegalArgumentException("短信内容超过最大长度（" + MAX_CONTENT_LENGTH+ "个字符）限制!");
        }
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public String getContent() {
        return content;
    }

    private String validateMobileFormat(String mobile){
        if(Strings.isNullOrEmpty(mobile)){
            throw new IllegalArgumentException("空的手机号码!");
        }
        Matcher matcher = mobileFormat.matcher(mobile);
        if( !matcher.matches()){
            throw new IllegalArgumentException("貌似手机号码格式不对: "+ mobile);
        }

        return mobile;

    }
}
