package com.schoolguard.messages.gateway.sms.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * SmsHelper
 * @author Roma
 * @datetime 2015/8/26 - 22:25
 */
public class SmsHelper {

    /**
     * Hex编码字符组
     */
    private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 将 短信下行 请求响应字符串解析到一个HashMap中
     * @param resStr
     * @return
     */
    public static HashMap parseResStr(String resStr) {
        HashMap pp = new HashMap();
        try {
            String[] ps = resStr.split("&");
            for (int i = 0; i < ps.length; i++) {
                int ix = ps[i].indexOf("=");
                if (ix != -1) {
                    pp.put(ps[i].substring(0, ix), ps[i].substring(ix + 1));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return pp;
    }

    /**
     * 将普通字符串转换成Hex编码字符串
     * @param dataCoding 编码格式，15表示GBK编码，8表示UnicodeBigUnmarked编码，0表示ISO8859-1编码
     * @param realStr 普通字符串
     * @return Hex编码字符串
     */
    public static String encodeHexStr(int dataCoding, String realStr) {
        String hexStr = null;

        if (realStr != null) {
            byte[] data = null;
            try {
                if (dataCoding == 15) {
                    data = realStr.getBytes("GBK");
                } else if ((dataCoding & 0x0C) == 0x08) {
                    data = realStr.getBytes("UnicodeBigUnmarked");
                } else {
                    data = realStr.getBytes("ISO8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }

            if (data != null) {
                int len = data.length;
                char[] out = new char[len << 1];
                // two characters form the hex value.
                for (int i = 0, j = 0; i < len; i++) {
                    out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
                    out[j++] = DIGITS[0x0F & data[i]];
                }
                hexStr = new String(out);
            }
        }
        return hexStr;
    }

    /**
     * 将Hex编码字符串还原成普通字符串
     * @param dataCoding 反编码格式，15表示GBK编码，8表示UnicodeBigUnmarked编码，0表示ISO8859-1编码
     * @param hexStr Hex编码字符串
     * @return 普通字符串
     */
    public static String decodeHexStr(int dataCoding, String hexStr) {
        String realStr = null;

        if (hexStr != null) {
            char[] data = hexStr.toCharArray();

            int len = data.length;

            if ((len & 0x01) != 0) {
                throw new RuntimeException("Odd number of characters.");
            }

            byte[] out = new byte[len >> 1];

            // two characters form the hex value.
            for (int i = 0, j = 0; j < len; i++) {
                int f = Character.digit(data[j], 16) << 4;
                if(f==-1){
                    throw new RuntimeException("Illegal hexadecimal charcter " + data[j] + " at index " + j);
                }
                j++;
                f = f | Character.digit(data[j], 16);
                if(f==-1){
                    throw new RuntimeException("Illegal hexadecimal charcter " + data[j] + " at index " + j);
                }
                j++;
                out[i] = (byte) (f & 0xFF);
            }
            try {
                if (dataCoding == 15) {
                    realStr = new String(out, "GBK");
                } else if ((dataCoding & 0x0C) == 0x08) {
                    realStr = new String(out, "UnicodeBigUnmarked");
                } else {
                    realStr = new String(out, "ISO8859-1");
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }
        }

        return realStr;
    }

    /**
     * 判断字符串是否为空或空字符串
     * @param content
     * @return
     *     1）为空或空字符串返回true
     *     2）否则返回false
     */
    public static boolean isNull(String content) {
        boolean flag = false;
        if (content == null || "".equals(content.trim())) {
            flag = true;
        }
        return flag;
    }

    /**
     * Main Test
     * @param args
     */
    public static void main(String[] args) {

        String content_text = "测测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        String content_letter = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String content_punctuation = "::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::";
        String content_mix = "aaaaaaaaaa测试测试测试测试测试测测试测试测试测试测试测试测试测试测试测";

//        if (content_letter.getBytes().length == content_letter.length()) {
//            System.out.println(content_letter.getBytes().length);
//            System.out.println("字节长度跟文本长度相等");
//        } else {
//            System.out.println(content_letter.getBytes().length + ":" + content_letter.length());
//            System.out.println("不等");
//        }

        System.out.println("文本内容的长度：" + content_text.length());
        System.out.println("文本内容字节长度：" + content_text.getBytes().length);
        System.out.println("加密后内容的长度：" + encodeHexStr(15, content_text).length());
    }

}
