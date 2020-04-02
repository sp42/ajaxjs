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

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import com.ajaxjs.util.logger.LogHelper;

/**
 * RSA 非对称加密/解密
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class RSA_Cipher {
	private static final LogHelper LOGGER = LogHelper.getLog(RSA_Cipher.class);

	/* 貌似 CIPHER_ALGORITHM 默认是RSA/NONE/PKCS1Padding，未验证 */
	/* RSA密钥长度 KEY_SIZE 必须是64的倍数，在512~65536之间。默认是1024 */
	private final static CipherInfo ci = new CipherInfo("RSA/ECB/PKCS1Padding", 2048);

	public static final String PUBLIC_KEY = "publicKey";

	public static final String PRIVATE_KEY = "privateKey";

	private static byte[] action(int mode, Key key, byte[] s) {
		return CipherInfo.doCipher(ci.getCipherAlgorithm(), mode, key, s);
	}

	/**
	 * 生成密钥对。注意这里是生成密钥对 KeyPair，再由密钥对获取公私钥
	 * 
	 * @return 密钥对
	 */
	public static Map<String, byte[]> generatePair() {
		KeyPairGenerator g;

		try {
			g = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}

		g.initialize(ci.getKeySize());

		KeyPair kp = g.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();

		Map<String, byte[]> map = new HashMap<>();
		map.put(PUBLIC_KEY, publicKey.getEncoded());
		map.put(PRIVATE_KEY, privateKey.getEncoded());

		return map;
	}

	/**
	 * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
	 * 
	 * @param key 公钥字节码
	 * @return 公钥
	 */
	public static PublicKey restorePublicKey(byte[] key) {
		X509EncodedKeySpec ks = new X509EncodedKeySpec(key);

		try {
			return KeyFactory.getInstance("RSA").generatePublic(ks);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
	 * 
	 * @param key 私钥字节码
	 * @return 私钥
	 */
	public static PrivateKey restorePrivateKey(byte[] key) {
		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(key);

		try {
			return KeyFactory.getInstance("RSA").generatePrivate(ks);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param key  公钥
	 * @param text 要加密的内容
	 * @return 加密的结果
	 */
	public static byte[] encrypt(PublicKey key, byte[] text) {
		return action(Cipher.ENCRYPT_MODE, key, text);
	}

	/**
	 * 解密
	 * 
	 * @param key     私钥
	 * @param encoded 已加密的内容
	 * @return 解密的结果
	 */
	public static String decrypt(PrivateKey key, byte[] encoded) {
		return new String(action(Cipher.DECRYPT_MODE, key, encoded));
	}
}