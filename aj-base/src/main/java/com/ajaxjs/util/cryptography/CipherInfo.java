/**
 * Copyright 2015 sp42 frank@ajaxjs.com
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

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 简单封装了 Java Cipher 类提供了加密和解密的功能。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CipherInfo {
	private static final LogHelper LOGGER = LogHelper.getLog(CipherInfo.class);

	/**
	 * 加密算法
	 */
	private String cipherAlgorithm;

	/**
	 * 密钥长度
	 */
	private int keySize;

	/**
	 * 创建一个 CipherInfo 实例
	 * 
	 * @param cipherAlgorithm 加密算法
	 * @param keySize         密钥长度
	 */
	public CipherInfo(String cipherAlgorithm, int keySize) {
		this.cipherAlgorithm = cipherAlgorithm;
		this.keySize = keySize;
	}

	/**
	 * 进行加密或解密，三步走
	 * 
	 * @param algorithm 选择的算法
	 * @param mode      是解密模式还是加密模式？
	 * @param key       密钥
	 * @param s         输入的内容
	 * @return 结果
	 */
	static byte[] doCipher(String algorithm, int mode, Key key, byte[] s) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(mode, key);

			/*
			 * 为了防止解密时报 javax.crypto.IllegalBlockSizeException: Input length must be
			 * multiple of 8 when decrypting with padded cipher 异常， 不能把加密后的字节数组直接转换成字符串
			 */
			return cipher.doFinal(s);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * AES/DES 专用
	 * 
	 * @param ci    密码信息
	 * @param mode  是解密模式还是加密模式？
	 * @param key   密钥
	 * @param bytes 输入的内容，可以是字符串转换 byte[]
	 * @return 转换后的内容
	 */
	static byte[] doCipher(CipherInfo ci, int mode, String key, byte[] bytes) {
		SecretKey _key;// 获得密钥对象

		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(key.getBytes());
			KeyGenerator kg = KeyGenerator.getInstance(ci.getCipherAlgorithm());
			kg.init(ci.getKeySize(), sr);

			_key = kg.generateKey();// 生成密钥
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}

		return doCipher(ci.getCipherAlgorithm(), mode, _key, bytes);
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}

	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	public void setCipherAlgorithm(String cipherAlgorithm) {
		this.cipherAlgorithm = cipherAlgorithm;
	}

}
