/**
 * Copyright Sp42 frank@ajaxjs.com
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
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class CommonUtil {
	/**
	 * 是否空字符串
	 * 
	 * @param str 要判断的字符串
	 * @return true 表示为为空字符串，否则不为空
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.isEmpty() || str.trim().isEmpty();
	}

	/**
	 * 将一个字符串分隔为字符串数组，分隔符 可以是,、/、-、\（注意转义）、|、; 作为分隔符。 常在读取配置的时候使用。
	 * 
	 * @param str 输入的字符串
	 * @return 分隔后的数组
	 */
	public static String[] split(String str) {
		return str.split(",|/|-|\\\\|\\||;");
	}

	/**
	 * 重复字符串 repeat 次并以 div 分隔
	 * 
	 * @param str    要重复的字符串
	 * @param div    字符串之间的分隔符
	 * @param repeat 重复次数
	 * @return 结果
	 */
	public static String repeatStr(String str, String div, int repeat) {
		StringBuilder s = new StringBuilder();
		int i = 0;

		while (i++ < repeat) {
			s.append(str);
			if (i != repeat)
				s.append(div);
		}

		return s.toString();
	}

	/**
	 * 输入 a，看 a 里面是否包含另一个字符串 b，忽略大小写。
	 * 
	 * @param a 输入字符串 a
	 * @param b 另一个字符串 b
	 * @return true 表示包含
	 */
	public static boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}

	/**
	 * 使用正则的快捷方式
	 * 
	 * @param regexp 正则
	 * @param str    测试的字符串
	 * @return 匹配结果，只有匹配第一个
	 */
	public static String regMatch(String regexp, String str) {
		return regMatch(regexp, str, 0);
	}

	/**
	 * 使用正则的快捷方式。可指定分组
	 * 
	 * @param regexp     正则
	 * @param str        测试的字符串
	 * @param groupIndex 分组 id
	 * @return 匹配结果
	 */
	public static String regMatch(String regexp, String str, int groupIndex) {
		Matcher m = Pattern.compile(regexp).matcher(str);
		return m.find() ? m.group(groupIndex) : null;
	}

	/**
	 * 判断数组是否为空
	 * 
	 * @param arr 输入的数组
	 * @return true 表示为素组不是为空，是有内容的，false 表示为数组为空数组，length = 0
	 */
	public static boolean isNull(Object[] arr) {
		return arr == null || arr.length == 0;
	}

	/**
	 * 判断 collection 是否为空
	 * 
	 * @param collection Map输入的集合
	 * @return true 表示为集合不是为空，是有内容的，false 表示为空集合
	 */
	public static boolean isNull(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断 map 是否有意义
	 * 
	 * @param map 输入的
	 * @return true 表示为 map 不是为空，是有内容的，false 表示为空 map
	 */
	public static boolean isNull(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * 常见的日期格式
	 */
	public static final String commonDateFormat = "yyyy-MM-dd HH:mm:ss";
	public static final String commonDateFormat_shorter = "yyyy-MM-dd HH:mm";
	public static final String commonDateFormat_shortest = "yyyy-MM-dd";

	/**
	 * 对输入的时间进行格式化，采用格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date 输入的时间
	 * @return 转换到 yyyy-MM-dd HH:mm:ss 格式的时间
	 */
	public static String formatDate(Date date) {
		return SimpleDateFormatFactory(commonDateFormat).format(date);
	}

	/**
	 * 对输入的时间进行格式化，采用格式 YYYY-MM-dd HH:MM
	 * 
	 * @param date 输入的时间
	 * @return 转换到 YYYY-MM-dd HH:MM 格式的时间
	 */
	public static String formatDateShorter(Date date) {
		return SimpleDateFormatFactory(commonDateFormat_shorter).format(date);
	}

	/**
	 * 返回当前时间，并对当前时间进行格式化
	 * 
	 * @param format 期望的格式
	 * @return 转换到期望格式的当前时间
	 */
	public static String now(String format) {
		return SimpleDateFormatFactory(format).format(new Date());
	}

	/**
	 * 返回当前时间的 YYYY-MM-dd HH:MM:ss 字符串类型
	 * 
	 * @return 当前时间
	 */
	public static String now() {
		return now(commonDateFormat);
	}

	private final static String dateReg = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

	private final static Pattern pattern = Pattern.compile(dateReg + " ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");

	private final static Pattern pattern2 = Pattern.compile(dateReg); // 可能是这种格式 2016-08-18

	/**
	 * 支持任意对象转换为日期类型
	 * 
	 * @param obj 任意对象
	 * @return 日期类型对象，null 表示为转换失败
	 */
	public static Date Objet2Date(Object obj) {
		if (obj == null)
			return null;
		else if (obj instanceof Date)
			return (Date) obj;
		else if (obj instanceof Long)
			return new Date((Long) obj);
		else if (obj instanceof Integer)
			return Objet2Date(Long.parseLong(obj + "000")); /* 10 位长 int，后面补充三个零为13位 long 时间戳 */
		else if (obj instanceof String) {
			String str = obj.toString();

			try {
				if (pattern.matcher(str).matches()) {
					return SimpleDateFormatFactory(commonDateFormat).parse(str);
				} else if (pattern2.matcher(str).matches()) {
					return SimpleDateFormatFactory(commonDateFormat_shortest).parse(str);
				} else 
					return SimpleDateFormatFactory(commonDateFormat_shorter).parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 输入日期不合法，不能转为日期类型。请重新输入日期字符串格式类型，或考虑其他方法。
		}

		return null;
	} 

	/**
	 * SimpleDateFormat caches
	 */
	private final static Map<String, SimpleDateFormat> formaters = new ConcurrentHashMap<>();

	/**
	 * 对输入的时间进行格式化 有 SimpleDateFormat 缓存 格式化的另外一种方法 new
	 * SimpleDateFormat(format).format(System.currentTimeMillis());返回
	 * SimpleDateFormat 的工厂函数
	 * 
	 * @param format 日期格式
	 * @return 格式日期的对象
	 */
	public static SimpleDateFormat SimpleDateFormatFactory(String format) {
		if (!formaters.containsKey(format))
			formaters.put(format, new SimpleDateFormat(format));

		return formaters.get(format);
	}

}