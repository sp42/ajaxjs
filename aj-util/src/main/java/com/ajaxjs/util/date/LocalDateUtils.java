package com.ajaxjs.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * LocalDate 相关公共方法
 * 
 * @author Frank Cheung
 *
 */
public class LocalDateUtils {
	/**
	 * 中国时区
	 */
	private static final ZoneOffset CHINA_ZONE = ZoneOffset.ofHours(8);

	// ------------------------------------------------------------------------------------------

	/**
	 * 返回当前精确到秒的时间戳
	 * 
	 * @param zoneOffset 时区
	 * @return
	 */
	public static long nowLong(ZoneOffset zoneOffset) {
		return LocalDateTime.now().toEpochSecond(zoneOffset);
	}

	/**
	 * 返回当前精确到秒的时间戳。默认为+8
	 * 
	 * @return
	 */
	public static long nowLong() {
		return nowLong(CHINA_ZONE);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * 将时间戳转换为 LocalDateTime
	 * 
	 * @param second     long 类型的时间戳
	 * @param zoneOffset 时区
	 * @return
	 */
	public static LocalDateTime ofEpochSecond(long second, ZoneOffset zoneOffset) {
		return LocalDateTime.ofEpochSecond(second, 0, zoneOffset);
	}

	/**
	 * 将时间戳转换为 LocalDateTime
	 * 
	 * @param second long 类型的时间戳
	 * @return
	 */
	public static LocalDateTime ofEpochSecond(long second) {
		return ofEpochSecond(second, CHINA_ZONE);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * 格式化 LocalDateTime
	 * 
	 * @param dateTime 指定时间格式化表达式
	 * @param pattern
	 * @return
	 */
	public static String toDateTimeStr(LocalDateTime dateTime, String pattern) {
		return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
	}

	/**
	 * 格式化 LocalDateTime（格式化表达式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String toDateTimeStr(LocalDateTime dateTime) {
		return toDateTimeStr(dateTime, DateUtil.DATE_FORMAT);
	}

	/**
	 * 返回当前时间字符串
	 * 
	 * @param pattern 指定时间格式化表达式
	 * @return
	 */
	public static String nowStr(String pattern) {
		return toDateTimeStr(LocalDateTime.now(), pattern);
	}

	/**
	 * 返回当前时间字符串（格式化表达式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @return
	 */
	public static String nowStr() {
		return nowStr(DateUtil.DATE_FORMAT);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * 将时间字符串转化为 LocalDateTime
	 * 
	 * @param dateTimeStr 时间字符串
	 * @param pattern     指定时间格式化表达式
	 * @return
	 */
	public static LocalDateTime toDateTime(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
	}

	/**
	 * 将时间字符串转化为 LocalDateTime
	 * 
	 * @param dateTimeStr 时间字符串
	 * @return
	 */
	public static LocalDateTime toDateTime(String dateTimeStr) {
		return toDateTime(dateTimeStr, DateUtil.DATE_FORMAT);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * 格式化 LocalDate
	 * 
	 * @param date    日期
	 * @param pattern 指定日期格式化表达式
	 * @return
	 */
	public static String toDateStr(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
	}

	/**
	 * 格式 化LocalDate
	 * 
	 * @param date 日期
	 * @return
	 */
	public static String toDateStr(LocalDate date) {
		return toDateStr(date, DateUtil.DATE_FORMAT_SHORTER);
	}

	/**
	 * 返回当前日期字符串（格式化表达式：yyyy-MM-dd）
	 * 
	 * @return
	 */
	public static String currentDateStr() {
		return toDateStr(LocalDate.now());
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * 将日期字符串转化为LocalDate
	 * 
	 * @param dateStr 日期字符串
	 * @param pattern 指定日期格式化表达式
	 * @return
	 */
	public static LocalDate toDate(String dateStr, String pattern) {
		return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
	}

	/**
	 * 将日期字符串转化为 LocalDate
	 * 
	 * @param dateStr 日期字符串
	 * @return
	 */
	public static LocalDate toDate(String dateStr) {
		return toDate(dateStr, DateUtil.DATE_FORMAT_SHORTER);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * 返回几天之后的时间
	 * 
	 * @param days 天数
	 * @return
	 */
	public static LocalDateTime nextDays(long days) {
		return LocalDateTime.now().plusDays(days);
	}

	/**
	 * 返回几天之后的时间
	 * 
	 * @param days 天数
	 * @return
	 */
	public static LocalDateTime nextDays(Integer days) {
		return nextDays(days.longValue());
	}

	/**
	 * 返回几天之后的时间（精确到秒的时间戳）
	 * 
	 * @param days       天数
	 * @param zoneOffset 时区
	 * @return
	 */
	public static Long nextDaysSecond(long days, ZoneOffset zoneOffset) {
		LocalDateTime dateTime = nextDays(days);

		if (zoneOffset == null) {
			return dateTime.toEpochSecond(ZoneOffset.ofHours(8));
		} else {
			return dateTime.toEpochSecond(zoneOffset);
		}
	}

	public static Long nextDaysSecond(Integer days, ZoneOffset zoneOffset) {
		return nextDaysSecond(days.longValue(), zoneOffset);
	}

	/**
	 * 将天数转化为秒数
	 * 
	 * @param days 天数
	 * @return 秒数
	 */
	public static Long dayToSecond(Long days) {
		return days * 86400;
	}

	public static Long dayToSecond(Integer days) {
		return dayToSecond(days.longValue());
	}

	/**
	 * 和当前日期比较，是否超时
	 * 
	 * @param expiresDateTime true 表示超时
	 */
	public static boolean isTimeout(LocalDateTime expiresDateTime) {
		return LocalDateTime.now().isAfter(expiresDateTime);
	}

}
