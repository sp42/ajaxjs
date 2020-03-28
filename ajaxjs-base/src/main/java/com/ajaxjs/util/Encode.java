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
 * @author sp42 frank@ajaxjs.com
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
	 * URL 网址的中文乱码处理。 如果 Tomcat 过滤器设置了 utf-8 那么这里就不用重复转码了
	 * 
	 * @param str 通常是 URL 的 Query String 参数
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
		try {
			return Base64.getDecoder().decode(str);
		} catch (IllegalArgumentException e) {
			LOGGER.warning("BASE64 解码失败", e);
			return null;
		}
	}

	/**
	 * BASE64 解码 这里需要强制捕获异常。
	 * 中文乱码：http://s.yanghao.org/program/viewdetail.php?i=54806
	 * 
	 * @param str 待解码的字符串
	 * @return 已解码的字符串
	 */
	public static String base64Decode(String str) {
		byte[] b = base64DecodeAsByte(str);
		return b == null ? null : byte2String(b);
	}

	/**
	 * 获取 字符串 md5 哈希值
	 * 
	 * @param str 输入的字符串
	 * @return MD5 摘要，返回32位大写的字符串
	 */
	public static String md5(String str) {
		return hash	("MD5", str);
	}

	/**
	 * 生成字符串的 SHA1/SHA-256 哈希值
	 * 
	 * @param hash 哈希算法，可以是 SHA1/SHA-256
	 * @param str  输入的内容
	 * @return 已哈希过的字符串
	 */
	private static String hash(String hash, String str) {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance(hash);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}

		md.update(str.getBytes(StandardCharsets.UTF_8));

		// byte数组转化为16进制字符串输出。注意安卓环境下无此方法
		return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
	};

	/**
	 * 生成字符串的 SHA1 哈希值
	 * 
	 * @param str 输入的字符串
	 * @return 字符串的 SHA1 哈希值
	 */
	public static String getSHA1(String str) {
		return hash("SHA1", str);
	}

	/**
	 * 生成字符串的 SHA2 哈希值
	 * 
	 * @param str 输入的字符串
	 * @return 字符串的 SHA2 哈希值
	 */
	public static String getSHA256(String str) {
		return hash("SHA-256", str);
	}

}
