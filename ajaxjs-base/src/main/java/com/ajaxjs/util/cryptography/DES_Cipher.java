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

import javax.crypto.Cipher;

import com.ajaxjs.util.Encode;

/**
 * DES 对称算法 SymmetricCipher
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DES_Cipher {
	private final static CipherInfo ci = new CipherInfo("DES", null, 56);

	/**
	 * 加密
	 * 
	 * @param str 要加密的内容
	 * @param key 密钥
	 * @return 加密后的内容
	 */
	public static String encrypt(String str, String key) {
		// (这里要设置为 utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
		return Encode
				.base64Encode(CipherInfo.doCipher(ci, Cipher.ENCRYPT_MODE, key, str.getBytes(StandardCharsets.UTF_8)));

	}

	/**
	 * 解密
	 * 
	 * @param str 要解密的内容
	 * @param key 密钥
	 * @return 解密后的内容
	 */
	public static String decrypt(String str, String key) {
		return Encode.byte2String(CipherInfo.doCipher(ci, Cipher.DECRYPT_MODE, key, Encode.base64DecodeAsByte(str)));
	}
}
