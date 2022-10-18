package com.ajaxjs.util.cryptography.encryption;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.springframework.util.Base64Utils;

import com.ajaxjs.util.binrary.BytesUtil;

/**
 * DES 加解密工具类
 *
 * <pre>
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
 * DES                  key size must be equal to 56
 * DESede(TripleDES)    key size must be equal to 112 or 168
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
 * RC2                  key size must be between 40 and 1024 bits
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
 * 具体内容 需要关注 JDK Document http://.../docs/technotes/guides/security/SunProviders.html
 * </pre>
 */
public class DES {
	/**
	 * 定义加密方式
	 */
	private final static String KEY_DES = "DES";

	@SuppressWarnings("unused")
	private final static String KEY_AES = "AES"; // 测试

	/**
	 * 初始化密钥
	 * 
	 * @param seed 初始化参数
	 * @return
	 */
	public static String init(String seed) {
		SecureRandom secure = null;
		String str = "";

		try {
//			if (null != secure)
//				// 带参数的初始化
//				secure = new SecureRandom(Encode.base64DecodeAsByte(seed));
//			else
//				// 不带参数的初始化
			secure = new SecureRandom();
			str = SymmetriCipherInfo.getSecretKey(KEY_DES, secure);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 转换密钥
	 * 
	 * @param key 密钥的字节数组
	 * @return
	 */
	private static Key byteToKey(byte[] key) {
		try {
			return SecretKeyFactory.getInstance(KEY_DES).generateSecret(new DESKeySpec(key));

			// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
//            secretKey = new SecretKeySpec(key, KEY_DES);
		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * DES 解密
	 * 
	 * @param data 需要解密的字符串
	 * @param key  密钥
	 * @return
	 */
	public static String decryptDES(String data, String key) {
		Key k = byteToKey(Base64Utils.decodeFromString(key));
		byte[] bytes = SymmetriCipherInfo.doCipher(KEY_DES, Cipher.DECRYPT_MODE, k, null, hexString2Bytes(data));

		return new String(bytes);// 将得到的字节数组变成字符串返回
	}

	/**
	 * DES 加密
	 * 
	 * @param data 需要加密的字符串
	 * @param key  密钥
	 * @return
	 */
	public static String encryptDES(String data, String key) {
		Key k = byteToKey(Base64Utils.decodeFromString(key));
		byte[] bytes = SymmetriCipherInfo.doCipher(KEY_DES, Cipher.ENCRYPT_MODE, k, null, data.getBytes());

		return BytesUtil.bytesToHexStr(bytes);// 将得到的字节数组变成字符串返回
	}

	/**
	 * 整数变成16进制字符串
	 * 
	 * @param a 整数
	 * @return
	 */
	public static String changeHex(int a) {
		String str = "", tmp = "";

		for (int i = 0; i < 4; i++) {
			tmp = Integer.toHexString(((a >> i * 8) % (1 << 8)) & 0xff);

			if (tmp.length() < 2)
				tmp = "0" + tmp;

			str += tmp;
		}

		return str;
	}

	/**
	 * 转换十六进制字符串为字节数组
	 * 
	 * @param hexstr 十六进制字符串
	 * @return
	 */
	public static byte[] hexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;

		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);

			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}

		return b;
	}

	/**
	 * 转换字符类型数据为整型数据
	 * 
	 * @param c 字符
	 * @return
	 */
	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;

		return (c - '0') & 0x0f;
	}

}