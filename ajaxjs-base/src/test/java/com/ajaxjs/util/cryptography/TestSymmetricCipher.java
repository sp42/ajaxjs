package com.ajaxjs.util.cryptography;

import org.junit.Test;

import com.ajaxjs.util.cryptography.SymmetricCipher;

import static org.junit.Assert.*;

public class TestSymmetricCipher {
	String input = "cy11Xlbrmzyh:604:301:1353064296";
	String key = "37d5aed075525d4fa0fe635231cba447";
	
	@Test
	public void testEncryption() {
		String EncryptedPassword = SymmetricCipher.AES_Encrypt(input, key);
		assertEquals(input, SymmetricCipher.AES_Decrypt(EncryptedPassword, key));
	}
}
