package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
	public static final String DEFAULTFORMAT = "yy-MM-dd HH:mm:ss";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULTFORMAT);
	private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
	/**
	 * 
	 * @return long
	 */
	public static long TimeMillis() {
		return System.currentTimeMillis();
	}
	/**
	 * 
	 * @return int 
	 */
	public static int currentTime() {
		return (int) (TimeMillis()/1000);
	}
	/**
	 * 
	 * @param format 
	 * @return
	 */
	public static String getDate(String format) {
		dateFormat.applyPattern(format);
		String s = dateFormat.format(new Date(TimeMillis()));
		return s;
	}
	/**
	 * 
	 * @param format
	 * @param time
	 * @return
	 */
	public static String getDate(String format,int time) {
		dateFormat.applyPattern(format);
		Date date = new Date(Integer.valueOf(time).longValue()*1000);
		String s = dateFormat.format(date);
		return s;
	}
	/**
	 * @param date 
	 * @param format 
	 * @return 
	 * @throws ParseException
	 */
	public static int getTime(String date,String format) throws ParseException {
		dateFormat.applyPattern(format);
		return (int) (dateFormat.parse(date).getTime()/1000);
	}
	/**
	 * 转化 时间戳为时间字符串
	 * @param time
	 */
	public static String formatTime(int time) {
		dateFormat.applyPattern(DEFAULTFORMAT);
		String format = dateFormat.format(new Date(new Long(time)*1000));
		return format;
	}
	/**
	 * 转化 
	 * @param time
	 */
	public static String formatTime(int time,String format) {
		dateFormat.applyPattern(format);
		String s = dateFormat.format(new Date(new Long(time)*1000));
		return s;
	}
	/**
	 * 当前日期字符串
	 * @return
	 */
	public static String currentDate() {
		dateFormat.applyPattern(DEFAULTFORMAT);
		return dateFormat.format(new Date(TimeMillis()));
	}
	/**
	 * 当前日期字符串
	 * @return
	 */
	public static String currentDate(String format) {
		dateFormat.applyPattern(format);
		return dateFormat.format(new Date(TimeMillis()));
	}
	
	/**
	 * 获取一天的开始时间 0时0分0秒
	 */
	public static int getZeroTime(int time) {
		long a = time;
		calendar.setTimeInMillis(a*1000);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return (int) (calendar.getTimeInMillis()/1000);
	}
	/**
	 * 两天时间间隔天数 
	 * @param earlyTime
	 * @param laterTime
	 * @return  earlyTime>laterTime 返回负数
	 */
	public static int getDayBetween(int earlyTime,int laterTime) {
		int earlyZeroTime = getZeroTime(earlyTime);
		int laterZeroTime = getZeroTime(laterTime);
		return (laterZeroTime - earlyZeroTime)/86400;
	}
	
}
