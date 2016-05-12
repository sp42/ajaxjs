package com.ajaxjs.javatools;

import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Algorithm {
	/**
	 * CRC循环冗余校验。生成的散列值在传输或储存之前计算出来并且附加到数据后面。在使用数据之前, 对数据的完整性做校验。
	 * 测试代码
		String crc32Data = CRC32Cipher.crc32Hex("CRC32编码".getBytes());
        System.out.println(crc32Data);
	 * @param data
	 * @return
	 */
	public static Long crc32Encode(byte[] data) {
		CRC32 crc = new CRC32();
		crc.update(data);

		return crc.getValue();
	}

	public static String crc32Hex(byte[] data) {
		return Long.toHexString(crc32Encode(data));
	}
	
	/**
	 * 二分查找  
	 * http://mn960mn.blog.163.com/blog/static/114103084201091012953991/
	 * @param data
	 * @param startIndex
	 * @param endIndex
	 * @param find
	 * @return
	 */
	public static int find(int[] data, int startIndex, int endIndex, int find) {
		int midIndex = (endIndex - startIndex) / 2 + startIndex;

		if (find == data[midIndex]) return midIndex;

		if (startIndex >= endIndex) {
			return -1;
		} else if (find > data[midIndex]) {
			return find(data, midIndex + 1, endIndex, find);
		} else if (find < data[midIndex]) {
			return find(data, startIndex, midIndex - 1, find);
		}

		return -1;
	}

	public static int find(int[] data, int find) {
		int startIndex = 0;
		int endIndex = data.length - 1;

		while (startIndex <= endIndex) {
			int midIndex = (endIndex - startIndex) / 2 + startIndex;

			if (find == data[midIndex]) {
				return midIndex;
			} else if (find > data[midIndex]) {
				startIndex = startIndex + 1;
			} else if (find < data[midIndex]) {
				endIndex = endIndex - 1;
			}
		}

		return -1;
	}

	public static void main(String[] args) {
		int[] data = { 1, 3, 5, 7, 9, 11, 13, 15, 17, 19 };
		int index = find(data, 0, data.length - 1, 19);

		System.err.println(index);
		System.out.println(find(data, 19));
	}

	/**
	 * 瀵圭О鍔犲瘑绠楁硶AES 鍔犲瘑
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null || sKey.length() != 16) 
			return null;
		
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
	
		return new BASE64Encoder().encode(encrypted);
	}

	/**
	 * 瀵圭О鍔犲瘑绠楁硶AES 瑙ｅ瘑
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String Decrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null || sKey.length() != 16) {
			return null;
		}
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
}
