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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关的工具类
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class StringUtil {
	/**
	 * 是否空字符串
	 * 
	 * @param str
	 *            要判断的字符串
	 * @return true 表示为为空字符串，否则不为空
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.isEmpty() || str.trim().isEmpty();
	}

	/**
	 * 将一个字符串分隔为字符串数组，分隔符 可以是,、/、-、\（注意转义）、|、; 作为分隔符。 常在读取配置的时候使用。
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 分隔后的数组
	 */
	public static String[] split(String str) {
		return str.split(",|/|-|\\\\|\\||;");
	}

	/**
	 * Java String 有 split 却没有 join，这里实现一个
	 * 
	 * @param arr
	 *            输入的字符串数组
	 * @param join
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String stringJoin(String[] arr, String join) {
		if (CollectionUtil.isNull(arr))
			return null;

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < arr.length; i++) {
			if (i == (arr.length - 1))
				sb.append(arr[i]);
			else
				sb.append(arr[i]).append(join);
		}

		return new String(sb);
	}

	/**
	 * Java String 有 split 却没有 join，这里实现一个。默认用 , 分隔
	 * 
	 * @param arr
	 *            输入的字符串数组
	 * @return 连接后的字符串
	 */
	public static String stringJoin(String[] arr) {
		return stringJoin(arr, ", ");
	}

	/**
	 * Java String 有 split 却没有 join，这里实现一个
	 * 
	 * @param arr
	 *            输入的字符串列表
	 * @param join
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String stringJoin(List<String> arr, String join) {
		if (CollectionUtil.isNull(arr))
			return null;
		return stringJoin(arr.toArray(new String[arr.size()]), join);
	}

	/**
	 * 重复字符串 repeat 次并以 div 分隔
	 * 
	 * @param str
	 *            要重复的字符串
	 * @param div
	 *            字符串之间的分隔符
	 * @param repeat
	 *            重复次数
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
	 * @param a
	 *            输入字符串 a
	 * @param b
	 *            另一个字符串 b
	 * @return true 表示包含
	 */
	public static boolean containsIgnoreCase(String a, String b) {
		return a.toLowerCase().contains(b.toLowerCase());
	}

	/**
	 * 使用正则的快捷方式
	 * 
	 * @param regexp
	 *            正则
	 * @param str
	 *            测试的字符串
	 * @return 匹配结果，只有匹配第一个
	 */
	public static String regMatch(String regexp, String str) {
		return regMatch(regexp, str, 0);
	}

	/**
	 * 使用正则的快捷方式。可指定分组
	 * 
	 * @param regexp
	 *            正则
	 * @param str
	 *            测试的字符串
	 * @param groupIndex
	 *            分组 id
	 * @return 匹配结果
	 */
	public static String regMatch(String regexp, String str, int groupIndex) {
		Matcher m = Pattern.compile(regexp).matcher(str);
		return m.find() ? m.group(groupIndex) : null;
	}

}