package com.schoolguard.messages.gateway.sms.service;

import java.util.Map;

/**
 * 短信下行
 * @author Roma
 * @datetime 2015/8/26 - 22:41
 */
public interface MTClient {

    /**
     * 单条下行
     */
    public abstract boolean singleMT(String phone, String content, String pt, String at, String vt);

    /**
     * 相同内容不同目的群发
     */
    public abstract void multiMTWithSameContent();

    /**
     * 不同内容不同目的群发
     */
    public abstract boolean multiMTWithDiffContent(String[] phone, String[] contents, String pt, String at, String vt);

    /**
     * 文件群发
     */
    public abstract void fileBatchMT();
}
