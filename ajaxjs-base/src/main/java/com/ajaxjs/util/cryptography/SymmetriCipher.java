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

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.ajaxjs.util.Encode;

/**
 * AES 对称算法 SymmetricCipher
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SymmetriCipher {
	private final static CipherInfo AES = new CipherInfo("AES", 128);

	private final static CipherInfo DES = new CipherInfo("DES", 56);

	/**
	 * AES 加密
	 * 
	 * @param str 要加密的内容
	 * @param key 密钥
	 * @return 密文，加密后的内容
	 */
	public static String AES_Encrypt(String str, String key) {
		// (这里要设置为 utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
		return Encode.base64Encode(CipherInfo.doCipher(AES, Cipher.ENCRYPT_MODE, key, str.getBytes(StandardCharsets.UTF_8)));

	}

	/**
	 * AES 解密
	 * 
	 * @param str 密文，要解密的内容
	 * @param key 密钥
	 * @return 解密后的内容
	 */
	public static String AES_Decrypt(String str, String key) {
		byte[] b = CipherInfo.doCipher(AES, Cipher.DECRYPT_MODE, key, Encode.base64DecodeAsByte(str));
		if (b == null || b.length == 0)
			return null;

		return Encode.byte2String(b);
	}

	/**
	 * DES 加密
	 * 
	 * @param str 要加密的内容
	 * @param key 密钥
	 * @return 密文，加密后的内容
	 */
	public static String DES_Encrypt(String str, String key) {
		return Encode.base64Encode(CipherInfo.doCipher(DES, Cipher.ENCRYPT_MODE, key, str.getBytes(StandardCharsets.UTF_8)));

	}

	/**
	 * DES 解密
	 * 
	 * @param str 密文，要解密的内容
	 * @param key 密钥
	 * @return 解密后的内容
	 */
	public static String DES_Decrypt(String str, String key) {
		return Encode.byte2String(CipherInfo.doCipher(DES, Cipher.DECRYPT_MODE, key, Encode.base64DecodeAsByte(str)));
	}

	public static byte[] HMACSHA256(String data, String key) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256"));

			return mac.doFinal(data.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}
}
