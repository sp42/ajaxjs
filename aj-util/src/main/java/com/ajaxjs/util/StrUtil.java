/**
 * Copyright sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Base64Utils;

/**
 * 普通未分类的工具类
 *
 * @author sp42 frank@ajaxjs.com
 */
public class StrUtil {
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] getUTF8_Bytes(String str) {
		return str.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * 字节转编码为 字符串（ UTF-8 编码）
	 *
	 * @param bytes 输入的字节数组
	 * @return 字符串
	 */
	public static String byte2String(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * 字符串转为 UTF-8 编码的字符串
	 *
	 * @param str 输入的字符串
	 * @return UTF-8 字符串
	 */
	public static String byte2String(String str) {
		return byte2String(str.getBytes());
	}

	/**
	 * URL 网址的中文乱码处理。 如果 Tomcat 过滤器设置了UTF-8 那么这里就不用重复转码了
	 *
	 * @param str 通常是 URL 的 Query String 参数
	 * @return 中文
	 */
	public static String urlChinese(String str) {
		return byte2String(str.getBytes(StandardCharsets.ISO_8859_1));
	}

	/**
	 * URL 编码。 适合 GET 请求时候用
	 * <p>
	 * https://www.cnblogs.com/del88/p/6496825.html
	 *
	 * @param str 输入的字符串
	 * @return 编码后的字符串
	 */
	public static String urlEncodeQuery(String str) {
//        str = str.replaceAll(" ", "%20");
		return urlEncode(str);
	}

	/**
	 * URL 编码
	 *
	 * @param str 输入的字符串
	 * @return URL 编码后的字符串
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * BASE64 编码
	 *
	 * @param bytes 待编码的字符串 bytes
	 * @return 已编码的字符串
	 */
	public static String base64Encode(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * BASE64 编码
	 *
	 * @param str 待编码的字符串
	 * @return 已编码的字符串
	 */
	public static String base64Encode(String str) {
		return Base64Utils.encodeToString(getUTF8_Bytes(str));
	}

	/**
	 * BASE64 解码 这里需要强制捕获异常。
	 * 中文乱码：http://s.yanghao.org/program/viewdetail.php?i=54806
	 *
	 * @param str 待解码的字符串
	 * @return 已解码的字符串
	 */
	public static String base64Decode(String str) {
		byte[] b = Base64Utils.decodeFromString(str);
		return b == null ? null : byte2String(b);
	}

	/**
	 * 返回 Matcher
	 *
	 * @param regexp 正则
	 * @param str    测试的字符串
	 * @return Matcher
	 */
	private static Matcher getMatcher(String regexp, String str) {
		return Pattern.compile(regexp).matcher(str);
	}

	/**
	 * 使用正则的快捷方式。可指定分组
	 *
	 * @param regexp     正则
	 * @param str        测试的字符串
	 * @param groupIndex 分组 id，若为 -1 则取最后一个分组
	 * @return 匹配结果
	 */
	public static String regMatch(String regexp, String str, int groupIndex) {
		Matcher m = getMatcher(regexp, str);

		if (groupIndex == -1)
			groupIndex = m.groupCount();

		return m.find() ? m.group(groupIndex) : null;
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
	 * 返回所有匹配项
	 *
	 * @param regexp 正则
	 * @param str    测试的字符串
	 * @return 匹配结果
	 */
	public static String[] regMatchAll(String regexp, String str) {
		Matcher m = getMatcher(regexp, str);
		List<String> list = new ArrayList<>();

		while (m.find())
			list.add(m.group());

		return list.toArray(new String[list.size()]);
	}

	/**
	 * 随机字符串
	 */
	private static final String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * 生成指定长度的随机字符，可能包含数字
	 *
	 * @param length 户要求产生字符串的长度
	 * @return 随机字符
	 */
	public static String getRandomString(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(STR.charAt(number));
		}

		return sb.toString();
	}

	private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

	/**
	 * byte[] 转化为16进制字符串输出
	 *
	 * @param bytes 字节数组
	 * @return
	 */
	public static String bytesToHexStr(byte[] bytes) {
		byte[] hexChars = new byte[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;

			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}

		return new String(hexChars, StandardCharsets.UTF_8);
	}

	/**
	 * 连接两个 url 目录字符串，如果没有 / 则加上；如果有则去掉一个
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static String concatUrl(String a, String b) {
		char last = a.charAt(a.length() - 1), first = b.charAt(0);

		String result = null;
		if (last == '/' && first == '/') // both has
			result = a + b.substring(1);
		else if (last != '/' && first != '/') // haven't at all
			result = a + "/" + b;
		else if (last == '/' && first != '/')
			result = a + b;

		return result;
	}

	/**
	 * 统计文本中某个字符串出现的次数
	 *
	 * @param str   输入的字符串
	 * @param _char 某个字符
	 * @return 出现次数
	 */
	public static int charCount(String str, String _char) {
		int count = 0, index = 0;

		while (true) {
			index = str.indexOf(_char, index + 1);
			if (index > 0) {
				count++;
			} else {
				break;
			}
		}

		return count;
	}
	
	/**
	 * 字符串补齐
	 * 
	 * System.out.println(leftPad("12345", 10, "@"));
	 * 
	 * @param str   输入字符串
	 * @param leng  字符串总长度
	 * @param _char 填充字符
	 * @return
	 */
	public static String leftPad(String str, int leng, String _char) {
		return String.format("%" + leng + "s", str).replaceAll("\\s", _char);
	}
}