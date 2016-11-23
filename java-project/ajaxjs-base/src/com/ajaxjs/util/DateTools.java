/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具类 <% request.setAttribute("fn", new Util()); %>
 * ${fn.shortDate_YYYY_MM_DD(current.createDate)}
 * 
 * @author frank
 *
 */
public class DateTools {
	/**
	 * 常见的日期格式
	 */
	public static final String commonDateFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String commonDateFormat_shorter = "yyyy-MM-dd HH:mm";
	public static final String commonDateFormat_shortest = "yyyy-MM-dd";

	/**
	 * 返回当前时间的 YYYY-MM-dd HH:MM:ss 字符串类型
	 * 
	 * @return 当前时间
	 */
	public static String now() {
		return now(commonDateFormat);
	}

	/**
	 * 返回当前时间，并对当前时间进行格式化
	 * 
	 * @param format
	 *            期望的格式
	 * @return 转换到期望格式的当前时间
	 */
	public static String now(String format) {
		return formatDate(new Date(), format);
	}

	/**
	 * 对输入的时间进行格式化，采用格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            输入的时间
	 * @return 转换到 yyyy-MM-dd HH:mm:ss 格式的时间
	 */
	public static String formatDate(Date date) {
		return formatDate(date, commonDateFormat);
	}

	/**
	 * 对输入的时间进行格式化，采用格式 YYYY-MM-dd HH:MM
	 * 
	 * @param date
	 *            输入的时间
	 * @return 转换到 YYYY-MM-dd HH:MM 格式的时间
	 */
	public static String formatDateShorter(Date date) {
		return formatDate(date, commonDateFormat_shorter);
	}

	/**
	 * 对输入的时间进行格式化，采用格式 YYYY-MM-dd
	 * 
	 * @param date
	 *            输入的时间
	 * @return 转换到 YYYY-MM-dd 格式的时间
	 */
	public static String formatDateShortest(Date date) {
		return formatDate(date, commonDateFormat_shortest);
	}

	/**
	 * 对输入的时间进行格式化 有 SimpleDateFormat 缓存 格式化的另外一种方法 new
	 * SimpleDateFormat(format).format(System.currentTimeMillis());
	 * 
	 * @param date
	 *            输入的时间
	 * @param format
	 *            期望的格式
	 * @return 转换到期望格式的时间
	 */
	public static String formatDate(Date date, String format) {
		if (date == null || format == null)
			return null;

		return SimpleDateFormatFactory(format).format(date);
	}

	/**
	 * SimpleDateFormat caches
	 */
	private final static Map<String, SimpleDateFormat> formaters = new ConcurrentHashMap<>();
	
	/**
	 * 返回 SimpleDateFormat 的工厂函数
	 * @param format
	 * @return
	 */
	private static SimpleDateFormat SimpleDateFormatFactory(String format) {
		if (!formaters.containsValue(format)) {
			formaters.put(format, new SimpleDateFormat(format));
		}
		return formaters.get(format);
	}

	/**
	 * 转换字符串类型的日期到 Date 类型
	 * 
	 * @param date
	 *            字符串类型的日期
	 * @return Date 日期
	 */
	public static Date string2date(String date) {
		try {
			return SimpleDateFormatFactory(commonDateFormat).parse(date);
		} catch (ParseException e) {
			try {
				return SimpleDateFormatFactory("yyyy-MM-dd").parse(date);
			} catch (ParseException e1) {
				System.err.println("输入日期不合法，请重新输入日期字符串格式类型，或考虑其他方法。");
				e1.printStackTrace();
				return null;
			}
		}
	} 
	
	private final static Pattern pattern = Pattern.compile("((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
			+ "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");

	private final static Pattern pattern2 = Pattern.compile("((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])");

	/**
	 * 支持任意对象转换为日期类型
	 * @param obj 任意对象
	 * @return 日期类型对象，null 表示为转换失败
	 */
	public static Date Objet2Date(Object obj) {
		Date date = null;
		if (obj == null)
			return null;
		
		if (obj instanceof Date)
			return (Date) obj;
		else if (obj instanceof Number) {
			if (obj instanceof Integer) {
				long times = Long.parseLong(obj + "000"); // 10 位长 int，后面补充三个零为 13位 long 时间戳
				date = new Date(times);
			} else {
				date = new Date((long) obj);
			}
		} else if (obj instanceof String) {
			Matcher matcher = pattern.matcher(obj.toString());
			if (matcher.matches()) {
				date = string2date(obj.toString());
			} else {
				if (pattern2.matcher(obj.toString()).matches()) {// 可能是这种格式 2016-08-18
					date = string2date(obj.toString());
				} else
					System.err.println("非法字符串日期" + obj);
			}
		} else {
			System.err.println("不能识别类型，不能转为日期。传入参数为：" + obj);
		}
		
		return date;
	}
}
