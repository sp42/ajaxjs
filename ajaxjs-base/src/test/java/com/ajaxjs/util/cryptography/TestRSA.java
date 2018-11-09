package com.ajaxjs.util.cryptography;

import static com.ajaxjs.util.cryptography.RSA.*;
import static org.junit.Assert.assertEquals;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.junit.Test;

public class TestRSA {
	public static final String input = "美好的一天";

	@Test
	public void testEncryption() {
		Map<String, byte[]> keyMap = generateKeyBytes();

		// 加密
		PublicKey publicKey = restorePublicKey(keyMap.get(PUBLIC_KEY));
		byte[] encodedText = RSAEncode(publicKey, input.getBytes());

		// 解密
		PrivateKey privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
		String decoded = RSADecode(privateKey, encodedText);

		assertEquals(input, decoded);
	}
}
