package com.ajaxjs.util;

import static com.ajaxjs.util.cryptography.RSA_Cipher.PRIVATE_KEY;
import static com.ajaxjs.util.cryptography.RSA_Cipher.PUBLIC_KEY;
import static com.ajaxjs.util.cryptography.RSA_Cipher.decrypt;
import static com.ajaxjs.util.cryptography.RSA_Cipher.encrypt;
import static com.ajaxjs.util.cryptography.RSA_Cipher.generatePair;
import static com.ajaxjs.util.cryptography.RSA_Cipher.restorePrivateKey;
import static com.ajaxjs.util.cryptography.RSA_Cipher.restorePublicKey;
import static org.junit.Assert.assertEquals;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.cryptography.SymmetriCipher;

public class TestCryptography {
	public static final String input = "cy11Xlbrmzyh:604:301:1353064296";
	public static final String key = "37d5aed075525d4fa0fe635231cba447";

	@Test
	public void testAesEncryption() {
		String EncryptedPassword = SymmetriCipher.AES_Encrypt(input, key);
		assertEquals(input, SymmetriCipher.AES_Decrypt(EncryptedPassword, key));
	}

	@Test
	public void testDesEncryption() {
		String EncryptedPassword = SymmetriCipher.DES_Encrypt(input, key);
		assertEquals(input, SymmetriCipher.DES_Decrypt(EncryptedPassword, key));
	}

	public static final String RSAinput = "美好的一天";

	@Test
	public void testRSA_Encryption() {
		Map<String, byte[]> keyMap = generatePair();

		// 加密
		PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));
		byte[] encodedText = encrypt(publicKey, RSAinput.getBytes());

		// 解密
		PrivateKey privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
		String decoded = decrypt(privateKey, encodedText);

		assertEquals(RSAinput, decoded);
	}
}
