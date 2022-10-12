package com.ajaxjs.util.binrary;

import java.nio.charset.StandardCharsets;

public class BytesUtil {
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
}
