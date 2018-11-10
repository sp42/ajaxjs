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
package com.ajaxjs.util.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.ajaxjs.util.Encode;

/**
 * 对称算法
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public class SymmetricCipher {
	public SymmetricCipher() {
	}

	/**
	 * 创建一个 SymmetricCipher
	 * 
	 * @param ALGORITHM 加密算法，可以 DES | AES
	 * @param keySize   DES = 56 | AES = 128
	 */
	public SymmetricCipher(String ALGORITHM, int keySize) {
		this.ALGORITHM = ALGORITHM;
		this.keySize = keySize;
	}

	/**
	 * DES = 56 | AES = 128
	 */
	private int keySize = 128;

	/**
	 * 加密算法，可以 DES | AES
	 */
	private String ALGORITHM = "AES";

	/**
	 * 加密
	 * 
	 * @param str 要加密的内容
	 * @param key 密钥
	 * @return 加密后的内容
	 */
	public String encrypt(String str, String key) {
		// (这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
		return doCipher(true, key, str.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 解密
	 * 
	 * @param str 要解密的内容
	 * @param key 密钥
	 * @return 解密后的内容
	 */
	public String decrypt(String str, String key) {
		return doCipher(false, key, Encode.base64DecodeAsByte(str));
	}

	/**
	 * 
	 * @param isENCRYPT_MODE true加密/false=解密
	 * @param key            密钥
	 * @param bytes          加密/解密的内容
	 * @return 结果
	 */
	private String doCipher(boolean isENCRYPT_MODE, String key, byte[] bytes) {
		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(isENCRYPT_MODE ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, generateKey(key));
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}

		byte[] buf;
		try {
			// 为了防止解密时报javax.crypto.IllegalBlockSizeException: Input length must
			// be multiple of 8 when decrypting with padded cipher异常，
			// 不能把加密后的字节数组直接转换成字符串
			buf = cipher.doFinal(bytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}

		return isENCRYPT_MODE ? Encode.base64Encode(buf) : Encode.byte2String(buf);
	}

	/**
	 * 获得密钥对象
	 * 
	 * @param key 密钥
	 * @return 密钥对象
	 */
	private SecretKey generateKey(String key) {
		SecureRandom secureRandom;
		KeyGenerator kg;

		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kg = KeyGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

		kg.init(keySize, secureRandom);
		return kg.generateKey();// 生成密钥
	}

	final static SymmetricCipher AES = new SymmetricCipher();
	final static SymmetricCipher DES = new SymmetricCipher("DES", 56);

	/**
	 * AES 加密
	 * 
	 * @param str 要加密的内容
	 * @param key 密钥
	 * @return 加密后的内容
	 */
	public static String AES_Encrypt(String str, String key) {
		return AES.encrypt(str, key);
	}

	/**
	 * AES 解密
	 * 
	 * @param str 要解密的内容
	 * @param key 密钥
	 * @return 加密后的内容
	 */
	public static String AES_Decrypt(String str, String key) {
		return AES.decrypt(str, key);
	}
}
