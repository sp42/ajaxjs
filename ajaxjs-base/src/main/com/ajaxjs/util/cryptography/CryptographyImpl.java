package com.ajaxjs.util.cryptography;

public class CryptographyImpl implements Cryptography {
	@Override
	public String AES_Encrypt(String str, String key) {
		return SymmetricCipher.AES_Encrypt(str, key);
	}

	@Override
	public String AES_Decrypt(String str, String key) {
		return SymmetricCipher.AES_Decrypt(str, key);
	}
}
