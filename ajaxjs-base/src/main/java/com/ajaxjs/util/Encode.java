/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 字符串的编码、解密 支持 MD5、SHA-1 和 SHA-2（SHA256）摘要算法
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class Encode {
	private static final LogHelper LOGGER = LogHelper.getLog(Encode.class);

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
	 * 将 URL 编码的字符还原，默认 UTF-8 编码
	 * 
	 * @param str 已 URL 编码的字符串
	 * @return 正常的 Java 字符串
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 将字符进行 URL 编码，默认 UTF-8 编码
	 * 
	 * @param str 正常的 Java 字符串
	 * 
	 * @return 已 URL 编码的字符串
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * url 网址中文乱码处理。 如果 Tomcat 过滤器设置了 utf-8 那么这里就不用重复转码了
	 * 
	 * @param str 通常是 url Query String 参数
	 * @return 中文
	 */
	public static String urlChinese(String str) {
		return byte2String(str.getBytes(StandardCharsets.ISO_8859_1));
	}

	/**
	 * BASE64 编码
	 * 
	 * @param bytes 输入的字节数组
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
		return base64Encode(str.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * BASE64 解码，但以 Byte 形式返回
	 * 
	 * @param str 待解码的字符串
	 * @return 已解码的 Byte
	 */
	public static byte[] base64DecodeAsByte(String str) {
		return Base64.getDecoder().decode(str);
	}

	/**
	 * BASE64 解码 这里需要强制捕获异常。
	 * 中文乱码：http://s.yanghao.org/program/viewdetail.php?i=54806
	 * 
	 * @param str 待解码的字符串
	 * @return 已解码的字符串
	 */
	public static String base64Decode(String str) {
		return byte2String(base64DecodeAsByte(str));
	}

	/**
	 * 获取 字符串 md5 哈希值
	 * 
	 * @param str 输入的字符串
	 * @return MD5 摘要，返回32位大写的字符串
	 */
	public static String md5(String str) {
		byte[] b = str.getBytes(StandardCharsets.UTF_8);

		try {
			b = MessageDigest.getInstance("MD5").digest(b);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}

		return DatatypeConverter.printHexBinary(b);
	}

	static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 生成字符串的 SHA1 哈希值
	 * 
	 * @param str 输入的字符串
	 * @return 字符串的 SHA1 哈希值
	 */
	public static String getSHA1(String str) {
		if (null == str || 0 == str.length())
			return null;

		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		md.update(str.getBytes(StandardCharsets.UTF_8));

		byte[] bytes = md.digest();
		int j = bytes.length;
		char[] buf = new char[j * 2];
		int k = 0;

		for (int i = 0; i < j; i++) {
			byte byte0 = bytes[i];
			buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
			buf[k++] = hexDigits[byte0 & 0xf];
		}

		return new String(buf);
	}

	/**
	 * 生成字符串的 SHA2 哈希值
	 * 
	 * @param str 输入的字符串
	 * @return 字符串的 SHA2 哈希值
	 */
	public static String getSHA256(String str) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(str.getBytes(StandardCharsets.UTF_8));
			return byte2Hex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 将 byte 转为 16 进制
	 * 
	 * @param bytes 字节数组
	 * @return Hex
	 */
	public static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;

		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);

			if (temp.length() == 1)
				stringBuffer.append("0");// 1得到一位的进行补0操作

			stringBuffer.append(temp);
		}

		return stringBuffer.toString();
	}
}
