package com.ecar.eoc.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 多线程下，创建解析日期，每个线程一个副本
 */
public class DateUtil {

    private static ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();

    /**
     * 时间格式 format yyyyMMdd,yyyy-MM-dd HH:mm:ss
     * @param format
     * @return
     */
    private static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat dateFormat = local.get();
        dateFormat = new SimpleDateFormat(format);
        local.set(dateFormat);
        return dateFormat;
    }

    public static Date parse(String dateStr,String format) throws Exception {
        return getDateFormat(format).parse(dateStr);
    }

    public static String format(Date date,String format) {
        return getDateFormat(format).format(date);
    }


    /**
     * 自定义格式时间为 yyyyMMdd，用于每天创建一张 mongo 表
     *
     * @param date
     * @return
     */
    public static String formatMongo(Date date,String format) {
        return getDateFormat(format).format(date);
    }
    
    

//    /**
//     * 返回指定日期，比如 i = 1，则返回第二天，i = 2，则返回第三天
//     *
//     * @param i 指定天数
//     * @return
//     */
//    public static String everyDayMongo(int i) {
//        Date today = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//        calendar.add(Calendar.DAY_OF_MONTH, i);
//
//        return formatMongo(calendar.getTime());
//    }
    
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 获取当天 24 点时间
     */
    public static Date getTimesNight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

