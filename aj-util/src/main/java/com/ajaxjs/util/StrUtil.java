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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import com.ajaxjs.util.binrary.BytesUtil;

/**
 * 普通未分类的工具类
 *
 * @author sp42 frank@ajaxjs.com
 */
public class StrUtil {
	/**
	 * URL 网址的中文乱码处理。 如果 Tomcat 过滤器设置了UTF-8 那么这里就不用重复转码了
	 *
	 * @param str 通常是 URL 的 Query String 参数
	 * @return 中文
	 */
	public static String urlChinese(String str) {
		return BytesUtil.byte2String(str.getBytes(StandardCharsets.ISO_8859_1));
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
		return Base64Utils.encodeToString(BytesUtil.getUTF8_Bytes(str));
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
		return b == null ? null : BytesUtil.byte2String(b);
	}

	/**
	 * 随机字符串
	 */
	private static final String STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * 生成指定长度的随机字符，可能包含数字
	 * 另外一个方法https://blog.csdn.net/qq_41995919/article/details/115299461
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
		else if (last != '/' && first == '/')
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

			if (index > 0)
				count++;
			else
				break;
		}

		return count;
	}

	/**
	 * 字符串补齐
	 * <p>
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

	private static final Pattern TPL_PATTERN = Pattern.compile("\\$\\{\\w+\\}");

	/**
	 * @param template
	 * @param params
	 * @return
	 */
	public static String simpleTpl(String template, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		Matcher m = TPL_PATTERN.matcher(template);

		while (m.find()) {
			String param = m.group();
			Object value = params.get(param.substring(2, param.length() - 1));
			m.appendReplacement(sb, value == null ? "" : value.toString());
		}

		m.appendTail(sb);

		return sb.toString();

	}

	/**
	 * 简单模板替换方法。这个根据 Map 数据
	 *
	 * @param template
	 * @param data
	 * @return
	 */
	public static String simpleTpl2(String template, Map<String, Object> data) {
		String result = template;

		for (Map.Entry<String, Object> entry : data.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value == null)
				value = "null";
			String placeholder = "#{" + key + "}";
			result = result.replace(placeholder, value.toString());
		}

		return result;
	}

	/**
	 * 简单模板替换方法。这个根据 JavaBean 数据
	 *
	 * @param template
	 * @param data
	 * @return
	 */
	public static String simpleTpl(String template, Object data) {
		String result = template;

		try {
			for (PropertyDescriptor descriptor : Introspector.getBeanInfo(data.getClass()).getPropertyDescriptors()) {
				String name = descriptor.getName();
				Object value = descriptor.getReadMethod().invoke(data);

				if (value == null)
					value = "null";

				String placeholder = "#{" + name + "}";
				result = result.replace(placeholder, value.toString());
			}
		} catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 将列表中的元素使用指定的分隔符连接成一个字符串，并返回连接后的字符串
	 * 
	 * @param <T>
	 * @param list	任何类型的列表
	 * @param str	字符串类型的分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String joinAnyList(List<T> list, String str) {
		Object[] objectArray = list.toArray();
		@SuppressWarnings("unchecked")
		T[] array = Arrays.copyOf(objectArray, objectArray.length, (Class<? extends T[]>) objectArray.getClass());

		return join(array, str);
	}

	/**
	 * 将数组中的元素使用指定的分隔符连接成一个字符串，并返回连接后的字符串
	 * 
	 * @param <T>
	 * @param arr 任何类型的数组
	 * @param str 字符串类型的分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(T[] arr, String str) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0, len = arr.length; i < len; i++) {
			String s = arr[i].toString();

			if (i != (len - 1))
				sb.append(s + str);
			else
				sb.append(s);
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param arr
	 * @param tpl
	 * @param str
	 * @return
	 */
	public static String join(String[] arr, String tpl, String str) {
		return join(Arrays.asList(arr), tpl, str);
	}

	/**
	 * 
	 * @param list
	 * @param str
	 * @return
	 */
	public static String join(List<String> list, String str) {
		return join(list, null, str);
	}

	public static String join(List<String> list, String tpl, String str) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0, len = list.size(); i < len; i++) {
			String s = list.get(i);

			if (tpl != null)
				s = String.format(tpl, s);

			if (i != (len - 1))
				sb.append(s + str);
			else
				sb.append(s);
		}

		return sb.toString();
	}

	/**
	 * 对象深度克隆 https://blog.csdn.net/qq_41995919/article/details/114486615 1).
	 * 实现Cloneable接口并重写Object类中的clone()方法； 2). 实现Serializable接口，通过对象的序列化和反序列化实现克隆
	 *
	 * @param <T>
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T obj) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bout);
			oos.writeObject(obj);

			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));

			// 说明：调用 ByteArrayInputStream 或 ByteArrayOutputStream 对象的 close 方法没有任何意义
			// 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源，这一点不同于对外部资源（如文件流）的释放
			return (T) ois.readObject();
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * word 是否数组里面的其中一员
	 *
	 * @param word
	 * @param strs
	 * @return
	 */
	public static boolean isWordOneOfThem(String word, String[] strs) {
		for (String str : strs) {
			if (word.equals(str))
				return true;
		}

		return false;
	}

	/**
	 * word 是否数组里面的其中一员
	 *
	 * @param word
	 * @param strs
	 * @return
	 */
	public static boolean isWordOneOfThem(String word, List<String> strs) {
		return isWordOneOfThem(word, strs.toArray(new String[strs.size()]));
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}

	/**
	 * Spring 提供的算法性能远远高于 JDK 的
	 *
	 * @return
	 */
	public static String uuid(boolean isRemove) {
		String uuid = new AlternativeJdkIdGenerator().generateId().toString();

		return isRemove ? uuid.replace("-", "") : uuid;
	}

	public static String uuid() {
		return uuid(true);
	}
}