package com.ajaxjs.util.cryptography;

public class CryptographyImpl {
	/**
	 * AES 加密
	 * 
	 * @param str 要加密的内容
	 * @param key 密钥
	 * @return 加密后的内容
	 */
	public String AES_Encrypt(String str, String key) {
		return SymmetricCipher.AES_Encrypt(str, key);
	}

	/**
	 * AES 解密
	 * 
	 * @param str 要解密的内容
	 * @param key 密钥
	 * @return 加密后的内容
	 */
	public String AES_Decrypt(String str, String key) {
		return SymmetricCipher.AES_Decrypt(str, key);
	}
}
