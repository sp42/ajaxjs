package com.ajaxjs.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 将时间格式字符串转换为时间对象。 遍历所有格式进行替换，假如替换成功，则根据标记位表示是否终止，标记不终止是为了识别英文时间。
 * 
 * @author 颜佐光
 *
 */
public class AutoDate {
	/**
	 * 无符号正则表达式
	 */
	public static final String DATE_FORMAT_NO_SPLIT_REGEX = "^(\\d{4})(\\d{2})(\\d{2})$";

	/**
	 * 有符号正常日期格式
	 */
	public static final String DATE_FORMAT_COMMON_REGEX = "^(\\d{4,})[/-](\\d{1,2})[/-](\\d{1,2})$";

	/**
	 * 有符号正常日期格式替换
	 */
	public static final String DATE_FORMAT_COMMON_REPLACE = "$1-$2-$3 00:00:00.000";

	/**
	 * 倒序的日期格式
	 */
	public static final String DATE_FORMAT_REVERT_REGEX = "^(\\d{1,2})[/-](\\d{1,2})[/-](\\d{4,})$";

	/**
	 * 有符号正常日期格式替换
	 */
	public static final String DATE_FORMAT_REVERT_REPLACE = "$3-$2-$1 00:00:00.000";

	/**
	 * 正常时间格式
	 */
	public static final String DATETIME_HOUR_FORMAT_REGEX = "^(\\d{4,})[/-](\\d{1,2})[/-](\\d{1,2}).{1}(\\d{1,2}):(\\d{1,2})$";

	/**
	 * 正常时间格式替换
	 */
	public static final String DATETIME_HOUR_FORMAT_REPLACE = "$1-$2-$3 $4:$5:00.000";

	/**
	 * 正常时间格式
	 */
	public static final String DATETIME_FORMAT_REGEX = "^(\\d{4,})[/-](\\d{1,2})[/-](\\d{1,2}).{1}(\\d{1,2}):(\\d{1,2}):(\\d{1,2})$";

	/**
	 * 正常时间格式替换
	 */
	public static final String DATETIME_FORMAT_REPLACE = "$1-$2-$3 $4:$5:$6.000";

	/**
	 * 时间格式化字符串 yyyy-MM-dd HH🇲🇲ss.SSS
	 */
	public static final String DATETIME_FULL_FORMAT = "yyyy-MM-dd HH:MM:SS";

	/**
	 * 缓存的自动识别的格式正则表达式
	 */
	private static List<DateReplace> autoDateCache = new ArrayList<>();

	static {
		registerAutoFormat(DATE_FORMAT_NO_SPLIT_REGEX, DATE_FORMAT_COMMON_REPLACE);
		registerAutoFormat(DATE_FORMAT_COMMON_REGEX, DATE_FORMAT_COMMON_REPLACE);
		registerAutoFormat(DATE_FORMAT_REVERT_REGEX, DATE_FORMAT_REVERT_REPLACE);
		registerAutoFormat(DATETIME_HOUR_FORMAT_REGEX, DATETIME_HOUR_FORMAT_REPLACE);
		registerAutoFormat(DATETIME_FORMAT_REGEX, DATETIME_FORMAT_REPLACE);
	}

	/**
	 * 时间格式字符串
	 */
	private static class DateReplace {
		// 正则表达式
		public String regex;
		// 替换表达式
		public String replace;
		// 终止标志位
		public boolean end;
	}

	/**
	 * 注册正则表达式，将时间转换为正确格式的正则表达式，后注册的会优先执行
	 *
	 * @param regex   正则表达式
	 * @param replace 替换表达式
	 */
	public static void registerAutoFormat(String regex, String replace) {
		registerAutoFormat(regex, replace, true);
	}

	/**
	 * 注册正则表达式，将时间转换为正确格式的正则表达式，后注册的会优先执行
	 *
	 * @param regex   正则表达式
	 * @param replace 替换表达式
	 * @param end     是否需要结束
	 */
	public static void registerAutoFormat(String regex, String replace, boolean end) {
		DateReplace item = new DateReplace();
		item.regex = regex;
		item.replace = replace;
		item.end = end;
		autoDateCache.add(item);
	}

	/**
	 * 根据时间字符串自动识别时间
	 *
	 * @param date 时间字符串
	 * @return 时间
	 */
	public static Date getAutoDate(String date) throws ParseException {
		if (date == null)
			return null;

		int size = autoDateCache.size();

		for (int i = size - 1; i >= 0; i--) {
			// 遍历所有时间格式
			DateReplace item = autoDateCache.get(i);
			String dateTo = date.replaceAll(item.regex, item.replace);

			boolean isBreak = item.end && !dateTo.equals(date);// 如何替换成功，且终止标志位为 true 则终止执行
			date = dateTo;

			if (isBreak)
				break;
		}

		return new SimpleDateFormat(DATETIME_FULL_FORMAT).parse(String.valueOf(date));// 将正常格式的时间字符串转换为时间
	}
}
