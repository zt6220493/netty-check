package com.ecar.eoc.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * @ClassName: DateUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zlf
 * @date 2014-12-17 下午03:24:18
 * 
 */
public class DateUtil {

	private final static int SECONDTIME = 1000;
	private final static long MINUTETIME = 60 * 1000;
	private final static long HOURTIME = 60 * 60 * 1000;
	private final static long DAYTIME = 24 * 60 * 60 * 1000;

	/**
	 * 
	 * @Title: getCurrentTime
	 * @Description: 获取当天整点的时间
	 * @param tim
	 * @return
	 * @author zlf
	 * @date 2014-12-17 下午03:41:41
	 * @version V1.0
	 */
	public static Date getCurrentTime(Date date, String time) {
		String da = formatDate(date, "yyyy-MM-dd");
		if (StringUtils.isEmpty(da)) {
			return null;
		}

		return toDate(da + " " + time, "yyyy-MM-dd HH:mm:ss");
	}

	public static int getDateSeconds(Date date) {
		return (int) (date.getTime() / SECONDTIME);
	}

	/**
	 * 只精确到日
	 * @param date
	 * @return
	 * @author ZHUANG PU XINAG 2016年5月18日
	 */
	public static Integer getTodayInteger(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sfd = sdf.format(date);
		try {
			return getDateSeconds(sdf.parse(sfd));
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * @Title: toDate
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param string
	 * @param string2
	 * @return
	 * @author zlf
	 * @date 2014-12-17 下午03:46:30
	 * @version V1.0
	 */
	public static Date toDate(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date s = null;
		try {
			s = formatter.parse(date);
		} catch (ParseException e) {

		}
		return s;
	}

	/**
	 * @Title: formatDate
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param date
	 * @param string
	 * @return
	 * @author zlf
	 * @date 2014-12-17 下午03:26:47
	 * @version V1.0
	 */
	public static String formatDate(Date date, String format) {

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		return dateString;

	}

	/**
	 * @Title: getMorrowDate
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param date
	 * @param i
	 * @return
	 * @author zlf
	 * @date 2014-12-17 下午03:36:32
	 * @version V1.0
	 */
	public static String getMorrowDate(Date date, int amount, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, amount);
		return formatDate(calendar.getTime(), format);
	}

	/**
	 * 
	 * @Title: getPreDateTime
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param date
	 * @param amount
	 * @param format
	 * @return
	 * @author yuqidi
	 * @date 2015-7-9 下午02:16:01
	 * @version V1.0
	 */
	public static Date getPreDateTime(Date date, int amount, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, amount);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title: getDay
	 * @Description: TODO(获取星期几)
	 * @param date
	 * @return
	 * @author jinqiu.chen
	 * @date 2014-12-31 上午11:11:06
	 * @version V1.0
	 */
	public static String getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String week = "";
		switch (dayOfWeek) {
		case 1:
			week = "星期日";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
			break;

		}
		return week;
	}

	/**
	 * 
	 * @Title: checkDateEques
	 * @Description: TODO(检测两个日期是否相等)
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author yuqidi
	 * @date 2015-7-15 下午07:38:15
	 * @version V1.0
	 */
	public static boolean checkDateEques(Date startTime, Date endTime) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(startTime);
		c2.setTime(endTime);
		if (c1.compareTo(c2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @author zhouzheng
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDaysBetween(Date date1, Date date2) {
		long between_days = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(sdf.format(date1)));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(sdf.format(date2)));
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1) / (1000 * 3600 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parse(String strDate, String pattern) throws ParseException {
		return StringUtils.isEmpty(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
	}

	/**
	 * 使用参数Format格式化Date成字符串
	 */
	public static String format(Date date, String pattern) {
		return date == null ? " " : new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 去掉Date时间的时分秒
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date removeHMS(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date nDate = null;
		try {
			nDate = sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			System.out.println("removeHMS fail !");
		}
		return nDate;
	}

	public static void formatHour(Calendar cal) {
		// 设置当前时刻的时钟为0
		cal.set(Calendar.HOUR_OF_DAY, 0);
		// 设置当前时刻的分钟为0
		cal.set(Calendar.MINUTE, 0);
		// 设置当前时刻的秒钟为0
		cal.set(Calendar.SECOND, 0);
		// 设置当前的毫秒钟为0
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 获取某个时间num天之后的date,消除时分秒
	 * 
	 * @return
	 */
	public static Date getNumDateBefore(Date date, int num) {
		if (null == date) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + num);

		try {
			return sdf.parse(sdf.format(cal.getTime()));
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 根据yyyy-MM-dd时间字符串，给出当月起始时间字符串
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getMonthStartEndDateByDay(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date mDate = sdf.parse(date);
		List<String> list = new ArrayList<String>();
		Calendar cal_1 = Calendar.getInstance();
		cal_1.setTime(mDate);

		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		list.add(sdf.format(cal_1.getTime()));

		cal_1.setTime(mDate);
		cal_1.set(Calendar.DATE, cal_1.getActualMaximum(Calendar.DATE));// 设置为1号,当前日期既为下月第一天
		list.add(sdf.format(cal_1.getTime()));
		return list;
	}

	/**
	 * 获取一个日期的周时间段 周一至周日
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getWeekDate(String dateStr) throws ParseException {
		List<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateStr));
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayWeek == 1) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		String imptimeBegin = sdf.format(cal.getTime());
		result.add(imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		result.add(imptimeEnd);
		return result;
	}

	public static List<String> getWeekDate(String dateStr, SimpleDateFormat sdf) throws ParseException {
		List<String> result = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateStr));
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayWeek == 1) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		String imptimeBegin = sdf.format(cal.getTime());
		result.add(imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		result.add(imptimeEnd);
		return result;
	}

	/**
	 * 得到一周的日期周天至周六
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getWeekDate_US(String dateStr) throws ParseException {
		List<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateStr));
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day); // 得到每周的第一天。
		String imptimeBegin = sdf.format(cal.getTime());
		result.add(imptimeBegin);

		cal.add(Calendar.DATE, 6); // 得到每周的最后一天。
		String imptimeEnd = sdf.format(cal.getTime());
		result.add(imptimeEnd);
		return result;
	}

	/**
	 * 查询上一周的开始日期和结束日期
	 * 
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getWeekDate_US_UP(String dateStr) throws ParseException {
		List<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateStr));

		cal.add(Calendar.WEEK_OF_MONTH, -1); // 定位到上一周
		cal.setFirstDayOfWeek(Calendar.SUNDAY); // 确定周日作为一周的开始。
		int day = cal.get(Calendar.DAY_OF_WEEK); // 得到当前日期是同的第几天。
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day); // 得到每周的第一天。
		String imptimeBegin = sdf.format(cal.getTime());
		result.add(imptimeBegin);

		cal.add(Calendar.DATE, 6); // 得到每周的最后一天。
		String imptimeEnd = sdf.format(cal.getTime());
		result.add(imptimeEnd);
		return result;
	}

	public static int getWeekNum(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateStr);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int result = c.get(Calendar.DAY_OF_WEEK) - 1;
			if (result == 0) {
				result = 7;
			}
			return result;
		} catch (Exception e) {
			return -1;
		}
	}

	public static Integer getDateDiffDay(Date from, Date to) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer dateDiff = null;
		try {
			long timeFrom = sdf.parse(sdf.format(from)).getTime();
			long timeTo = sdf.parse(sdf.format(to)).getTime();
			dateDiff = (int) ((timeTo - timeFrom) / (1000 * 60 * 60 * 24));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateDiff;
	}

	/**
	 * start,end
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static Integer getTimeMinutes(Long start, Long end) {
		long during = (end - start) / 1000;
		return (int) ((during / 60) + (during % 60 == 0 ? 0 : 1));
	}

	/**
	 * 获取两个时间间距： 以xx年xx月xx日xx小时xx分
	 * 
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static String getTwoTimeDuringDesc(Date dateA, Date dateB) {
		if (dateA == null || dateB == null) {
			return null;
		}
		int count = 0;
		StringBuffer strBuf = new StringBuffer();
		long during = Math.abs(dateB.getTime() - dateA.getTime());
		if (during > DAYTIME) {
			strBuf.append((during / DAYTIME) + "天");
			count++;
			during = during % DAYTIME;
		}
		if (during >= HOURTIME) {
			strBuf.append((during / HOURTIME) + "小时");
			count++;
			during = during % HOURTIME;
		}

		if (count < 2 && during >= MINUTETIME) {
			strBuf.append((during / MINUTETIME) + "分");
			count++;
			during = during % MINUTETIME;
		} else if (count < 2 && during < MINUTETIME && during > SECONDTIME) {
			strBuf.append("1分");
		}
		return strBuf.toString();
	}

	/**
	 * 将时间段格式化为HH:mm:ss
	 * 
	 * @param seconds
	 * @return
	 */
	public static String formatDuring(long seconds) {
		if (0 == seconds) {
			return "0";
		} else if (seconds < 60) {
			return "00:00:" + (seconds >= 10 ? seconds : ("0" + seconds));
		} else if (seconds < 3600) {
			long minute = seconds / 60;
			long second = seconds % 60;
			return "00:" + (minute >= 10 ? minute : ("0" + minute)) + ":" + (second >= 10 ? second : ("0" + second));
		} else {
			long hour = seconds / 3600;
			long minute = (seconds % 3600) / 60;
			long second = seconds % 60;
			return (hour >= 10 ? hour : ("0" + hour)) + ":" + (minute >= 10 ? minute : ("0" + minute)) + ":"
					+ (second >= 10 ? second : ("0" + second));
		}
	}
	
	/**
     * 获取排名计算开始时间字符串
     * 将一天划分为留个时间段，分别计算4个时间段的排名时间
     * 00:00:00-05:59:59,06:00:00-11:59:59
     * 12:00:00-17:59:59,18:00:00-23:59:59
     * @param date (yyyy/MM/dd HH:mm:ss)
     * @return
     */
    public static String getStartRankTime(String date){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	ParsePosition pos = new ParsePosition(0);
    	Date strtodate = formatter.parse(date, pos);
    	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
    	ParsePosition pos2 = new ParsePosition(0);
    	Date strtodate2 = formatter2.parse(date, pos2);
    	long milSeconds = strtodate.getTime()-strtodate2.getTime();
    	//第一时空
    	if(milSeconds<=(5*3600+59*60+59)*1000){
    		return formatter.format(strtodate2);
    	}else if(milSeconds<=(11*3600+59*60+59)*1000){
    		return formatter.format(new Date(strtodate2.getTime()+6*3600*1000));
    	}else if(milSeconds<=(17*3600+59*60+59)*1000){
    		return formatter.format(new Date(strtodate2.getTime()+12*3600*1000));
    	}else{
    		return formatter.format(new Date(strtodate2.getTime()+18*3600*1000));
    	}
    }
    
    /**
     * 获取排名计算结束时间字符串
     * 将一天划分为留个时间段，分别计算4个时间段的排名时间
     * 00:00:00-05:59:59,06:00:00-11:59:59
     * 12:00:00-17:59:59,18:00:00-23:59:59
     * @param date (yyyy/MM/dd HH:mm:ss)
     * @return
     */
    public static String getEndRankTime(String date){
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	ParsePosition pos = new ParsePosition(0);
    	Date strtodate = formatter.parse(date, pos);
    	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
    	ParsePosition pos2 = new ParsePosition(0);
    	Date strtodate2 = formatter2.parse(date, pos2);
    	long milSeconds = strtodate.getTime()-strtodate2.getTime();
    	//第一时空
    	if(milSeconds<=(5*3600+59*60+59)*1000){
    		return formatter.format(new Date(strtodate2.getTime()+(5*3600+59*60+59)*1000));
    	}else if(milSeconds<=(11*3600+59*60+59)*1000){
    		return formatter.format(new Date(strtodate2.getTime()+(11*3600+59*60+59)*1000));
    	}else if(milSeconds<=(17*3600+59*60+59)*1000){
    		return formatter.format(new Date(strtodate2.getTime()+(17*3600+59*60+59)*1000));
    	}else{
    		return formatter.format(new Date(strtodate2.getTime()+(23*3600+59*60+59)*1000));
    	}
    }
    
    public static Date getWeekStartDate(Date date){
		if(null==date){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date fdate = cal.getTime();
		return fdate;
	}
    
    public static Date getWeekEndDate(Date date){
		if(null==date){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date fdate = cal.getTime();
		return fdate;
	}
    
    public static String getDateString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
    
	public static void main(String[] args) throws ParseException {
		System.out.println(getWeekStartDate(new Date()));
	}
	
	/**
	 * 获取当前时间在当前天的秒数
	 */
	public static int getNowLocalDaySeconds(){
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		return hour*3600+minute*60+second;
	}

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }


    /**
     * 补充日期的时分秒
     * @param date 年份
     * @return Date
     */
    public static Date dateCompletion(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        // 设置当前时刻的时钟为0
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        // 设置当前时刻的分钟为0
        calendar.set(Calendar.MINUTE, 59);
        // 设置当前时刻的秒钟为0
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
