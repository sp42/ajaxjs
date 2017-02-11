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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 工具类
 * @author frank
 *
 */
public class Util {
	/**
	 * 判断数组是否有意义
	 * 
	 * @param arr
	 *            输入的数组
	 * @return true 表示为素组不是为空，是有内容的，false 表示为数组为空数组，length = 0
	 */
	public static boolean isNotNull(Object[] arr) {
		return arr != null && arr.length > 0;
	}
	
	/**
	 * 判断 collection 是否有意义
	 * 
	 * @param collection
	 *            Map输入的集合
	 * @return true 表示为集合不是为空，是有内容的，false 表示为空集合
	 */
	public static boolean isNotNull(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	/**
	 * 判断 map 是否有意义
	 * 
	 * @param map
	 *            输入的
	 * @return true 表示为 map 不是为空，是有内容的，false 表示为空 map
	 */
	public static boolean isNotNull(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}
	
	/**
	 * 强类型转换，有 null 检测
	 * 
	 * @param obj
	 *            输入的对象
	 * @param clazz
	 *            目标类型
	 * @return T型结果
	 */
	@SuppressWarnings("unchecked")
	public static <T> T TypeConvert(Object obj, Class<T> clazz) {
		return obj == null ? null : (T) obj;
	}
	
	/**
	 * 强类型转换为布尔型
	 * 
	 * @param obj
	 *            输入的对象
	 * @return 布尔型结果
	 */
	public static boolean toBoolean(Object obj) {
		return TypeConvert(obj, Boolean.class);
	}

	/**
	 * 强类型转换为整型
	 * 
	 * @param obj
	 *            输入的对象
	 * @return 整型结果
	 */
	public static int toInt(Object obj) {
		return TypeConvert(obj, Integer.class);
	}

	/**
	 * 强类型转换为日期型
	 * 
	 * @param obj
	 *            输入的对象
	 * @return 日期型结果
	 */
	public static Date toDate(Object obj) {
		return TypeConvert(obj, Date.class);
	}

	/**
	 * 强类型转换为字符串型
	 * 
	 * @param obj
	 *            输入的对象
	 * @param isUserToString
	 *            是否使用 toString() 转换
	 * @return 字符串型
	 */
	public static String to_String(Object obj, boolean isUserToString) {
		if (obj != null) {
			return isUserToString ? obj.toString() : TypeConvert(obj, String.class);
		} else
			return null;
	}
	
	/**
	 * 强类型转换为字符串型（使用强类型转换而不是 toString()）
	 * 
	 * @param obj
	 *            任意对象
	 * @return 字符串型
	 */
	public static String to_String(Object obj) {
		return to_String(obj, false);
	}

	/**
	 * 获取当前类所在的目录下的一个资源
	 * 
	 * @param cls
	 *            类
	 * @param fileName
	 *            资源文件名
	 * @return 资源路径
	 */
	public static String getClassFolder_FilePath(Class<?> cls, String fileName) {
		return StringUtil.urlDecode(cls.getResource(fileName).getPath());
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

		return StringUtil.byte2String(bytes);
	}
	
	/**
	 * 获取 字符串 md5 hash
	 * 返回32位 大写
	 * @param str
	 *            输入的字符串
	 * @return MD5 摘要
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
	
	private static final String text = "0123456789abcdefghijklmnopqrstuvwxyz";  
    
	/**
	 * 生成随机密码。可以控制生成的密码长度， 密码由数字和字母组成。
	 * 
	 * @param length
	 *            字符串长度
	 * @return 随机密码
	 */
	public synchronized static String passwordGenerator(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();

		for (int i = 0; i < length; i++)
			sb.append(text.charAt(random.nextInt(text.length())));

		return sb.toString();
	}
    
    /**
	 * 看最后一个是不是 uuid
	 * 
	 * @return
	 */
//	public static boolean isUUID(String url) {
//		String js = "'%s'.match(/\\w{8}(?:-\\w{4}){3}-\\w{12}/) != null;";
//		js = String.format(js, url);
//		boolean isUUID = App.jsRuntime.eval_return_Boolean(js);
//		return isUUID;
//	}
	
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
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23)
				+ uuid.substring(24);
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
		return StringUtil.regMatch(uuid_regExp, uuid) != null;
	}
}
