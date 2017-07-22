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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import com.ajaxjs.util.map.MapHelper;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 字符串相关的工具类
 * 
 * @author frank
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
	 * 将一个字符串分隔为字符串数组，分隔符 可以是,、|、; 作为分隔符。
	 * 常在读取配置的时候使用。
	 * @param str
	 * @return
	 */
	public static String[] split(String str) {
		return str.split(",|\\||;");
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
		if (!CollectionUtil.isNotNull(arr))
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
		if (!CollectionUtil.isNotNull(arr))
			return null;
		return stringJoin(arr.toArray(new String[arr.size()]), join);
	}

	/**
	 * 也是 join，不过输入的参数不是数组而是 hash
	 * 
	 * @param map
	 *            输入的 map
	 * @param div
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String HashJoin(Map<String, String> map, String div) {
		return MapHelper.join(map, div);
	}

	/**
	 * 重载版本
	 * 
	 * @param map
	 *            输入的 map
	 * @param div
	 *            分隔符
	 * @return 连接后的字符串
	 */
	public static String HashJoin(Map<String, String> map, char div) {
		return MapHelper.join(map, div + "");
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
	 * 输入 a，看是否包含另一个字符串 b，忽略大小写。
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
	 * @return
	 */
	public static String regMatch(String regexp, String str, int groupIndex) {
		Matcher m = Pattern.compile(regexp).matcher(str);
		return m.find() ? m.group(groupIndex) : null;
	}
	
	/**
	 * 将 URL 编码的字符还原，默认 UTF-8 编码
	 * 
	 * @param str
	 *            已 URL 编码的字符串
	 * @return 正常的 Java 字符串
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 将字符进行 URL 编码，默认 UTF-8 编码
	 * 
	 * @param str
	 *            正常的 Java 字符串
	 * 
	 * @return 已 URL 编码的字符串
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 字节转为 UTF-8 字符串
	 * 
	 * @param bytes
	 *            输入字符串的字节数组
	 * @return UTF-8 字符串
	 */
	public static String byte2String(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * 字节转为 UTF-8 字符串
	 * 
	 * @param str
	 *            输入的字符串
	 * @return UTF-8 字符串
	 */
	public static String byte2String(String str) {
		return byte2String(str.getBytes());
	}

	/**
	 * url 网址中文乱码处理。
	 * 如果 Tomcat 过滤器设置了 utf-8 那么这里就不用重复转码了
	 * 
	 * @param str
	 *            通常是 url Query String 参数
	 * @return 中文
	 */
	public static String urlChinese(String str) {
		return byte2String(str.getBytes(StandardCharsets.ISO_8859_1));
	}

	/**
	 * BASE64 编码
	 * 
	 * @param str
	 *            待编码的字符串
	 * @return 已编码的字符串
	 */
	public static String base64Encode(String str) {
		return new BASE64Encoder().encode(str.getBytes());
	}

	/**
	 * BASE64 解码 这里需要强制捕获异常。
	 * 中文乱码：http://s.yanghao.org/program/viewdetail.php?i=54806
	 * 
	 * @param str
	 *            已解码的字符串
	 * @return 已解码的字符串
	 */
	public static String base64Decode(String str) {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = null;

		try {
			bytes = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return byte2String(bytes);
	}
	
	/**
	 * 获取 字符串 md5 哈希值
	 * @param str
	 *            输入的字符串
	 * @return MD5 摘要，返回32位大写的字符串
	 */
	public static String md5(String str) {
		byte[] b = str.getBytes(StandardCharsets.UTF_8);
		
		try {
			b = MessageDigest.getInstance("MD5").digest(b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		return DatatypeConverter.printHexBinary(b);
	}
	
	/**
	 * 获取一个唯一的主键 id 的代码
	 * 
	 * @return UUID
	 */
	public static String getUUID() {
		return java.util.UUID.randomUUID().toString();
	}

	/**
	 * 去掉“-”符号
	 * 
	 * @param uuid
	 *            UUID 字符串
	 * @return 字符串
	 */
	public static String remove(String uuid) {
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}

	/**
	 * 是否 uuid 的正则表达式
	 */
	private static final String uuid_regExp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

	/**
	 * 判断输入的字符串是否包含 uuid 特征。
	 * 
	 * @param uuid
	 *            输入的字符串
	 * @return true 为 UUID
	 */
	public static boolean isUUID(String uuid) {
		return regMatch(uuid_regExp, uuid) != null;
	}
}