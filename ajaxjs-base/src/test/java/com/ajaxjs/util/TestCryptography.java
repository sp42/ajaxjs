package com.ajaxjs.util;

import org.junit.Test;

import com.ajaxjs.util.cryptography.SymmetricCipher;

import static com.ajaxjs.util.cryptography.RSA.PRIVATE_KEY;
import static com.ajaxjs.util.cryptography.RSA.PUBLIC_KEY;
import static com.ajaxjs.util.cryptography.RSA.RSADecode;
import static com.ajaxjs.util.cryptography.RSA.RSAEncode;
import static com.ajaxjs.util.cryptography.RSA.generateKeyBytes;
import static com.ajaxjs.util.cryptography.RSA.restorePrivateKey;
import static com.ajaxjs.util.cryptography.RSA.restorePublicKey;
import static org.junit.Assert.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class TestCryptography {
	String input = "cy11Xlbrmzyh:604:301:1353064296";
	String key = "37d5aed075525d4fa0fe635231cba447";

	@Test
	public void testEncryption() {
		String EncryptedPassword = SymmetricCipher.AES_Encrypt(input, key);
		assertEquals(input, SymmetricCipher.AES_Decrypt(EncryptedPassword, key));
	}

	public static final String RSAinput = "美好的一天";

	@Test
	public void testRSA_Encryption() {
		Map<String, byte[]> keyMap = generateKeyBytes();

		// 加密
		PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));
		byte[] encodedText = RSAEncode(publicKey, RSAinput.getBytes());

		// 解密
		PrivateKey privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
		String decoded = RSADecode(privateKey, encodedText);

		assertEquals(RSAinput, decoded);
	}
}
