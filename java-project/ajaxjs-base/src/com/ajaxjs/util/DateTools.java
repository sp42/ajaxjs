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
import java.util.HashMap;
import java.util.Map;

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
	 * 对当前时间进行格式化
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
		SimpleDateFormat formater;
		if (!formaters.containsValue(format)) {
			formaters.put(format, new SimpleDateFormat(format));
		}
		formater = formaters.get(format);

		return formater.format(date);
	}

	/**
	 * SimpleDateFormat caches
	 */
	private final static Map<String, SimpleDateFormat> formaters = new HashMap<>();

	@Deprecated
	public static String shortDate_YYYY_MM_DD(String dateStr) {
		return dateStr.split(" ")[0];
	}

	/**
	 * 转换字符串类型的日期到 Date 类型
	 * 
	 * @param date
	 *            字符串类型的日期
	 * @return Date 类型的日期
	 */
	public static Date string2date(String date) {
		try {
			return new SimpleDateFormat(commonDateFormat).parse(date);
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd").parse(date);
			} catch (ParseException e1) {
				System.err.println("输入日期不合法，请重新输入日期字符串格式类型，或考虑其他方法。");
				e1.printStackTrace();
				return null;
			}
		}
	}
}
