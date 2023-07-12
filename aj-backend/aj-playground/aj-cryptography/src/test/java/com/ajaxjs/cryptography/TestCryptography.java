package com.ajaxjs.cryptography;


import com.ajaxjs.cryptography.asymmetric.DH;
import com.ajaxjs.cryptography.asymmetric.RSA;
import com.ajaxjs.cryptography.encryption.DES;
import com.ajaxjs.cryptography.encryption.SymmetricCipher;
import org.junit.Test;

import java.security.Security;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCryptography {
    @Test
    public void testDES() {
        String key = DES.init(null);
        String word = "123", encWord = DES.encryptDES(word, key);

        assertEquals(word, DES.decryptDES(encWord, key));

        assertEquals(word, SymmetricCipher.DES_Decrypt(SymmetricCipher.DES_Encrypt(word, key), key));
    }

    @SuppressWarnings("restriction")
    @Test
    public void test3DES() {
        // 添加新安全算法,如果用JCE就要把它添加进去
        // 这里addProvider方法是增加一个新的加密算法提供者(个人理解没有找到好的答案,求补充)
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        // byte数组(用来生成密钥的)
        final byte[] keyBytes = {0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51, (byte) 0xCB, (byte) 0xDD, 0x55, 0x66, 0x77, 0x29, 0x74,
                (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2};
        String word = "This is a 3DES test. 测试";

        byte[] encoded = SymmetricCipher.encryptTripleDES(keyBytes, word);

        assertEquals(word, SymmetricCipher.decryptTripleDES(keyBytes, encoded));
    }

    @Test
    public void testAES() {
    }

    @Test
    public void testPBE() {
        // 加密前的原文
        String word = "hello world !!!";
        // 口令
        String key = "qwert";

        // 初始化盐
        byte[] salt = SymmetricCipher.init();
        byte[] encData = SymmetricCipher.encryptPBE(key, salt, word);
        assertEquals(word, SymmetricCipher.decryptPBE(key, salt, encData));
    }

    @Test
    public void testRSA() {
        // 生成公钥私钥
        Map<String, byte[]> map = RSA.init();
        String publicKey = RSA.getPublicKey(map), privateKey = RSA.getPrivateKey(map);

//		System.out.println("公钥: \n\r" + publicKey);
//		System.out.println("私钥： \n\r" + privateKey);
//		System.out.println("公钥加密--------私钥解密");

        String word = "你好，世界！";

        byte[] encWord = RSA.encryptByPublicKey(word.getBytes(), publicKey);
        String decWord = new String(RSA.decryptByPrivateKey(encWord, privateKey));
//		System.out.println("加密前: " + word + "\n\r" + "解密后: " + decWord);
        assertEquals(word, decWord);

//		System.out.println("私钥加密--------公钥解密");

        String english = "Hello, World!";
        byte[] encEnglish = RSA.encryptByPrivateKey(english.getBytes(), privateKey);
        String decEnglish = new String(RSA.decryptByPublicKey(encEnglish, publicKey));
//		System.out.println("加密前: " + english + "\n\r" + "解密后: " + decEnglish);

        assertEquals(english, decEnglish);
//		System.out.println("私钥签名——公钥验证签名");

        // 产生签名
        String sign = RSA.sign(encEnglish, privateKey);
//		System.out.println("签名:\r" + sign);
        // 验证签名
        assertTrue(RSA.verify(encEnglish, publicKey, sign));
    }

    @Test
    public void testDH() {
        // 生成甲方密钥对
        Map<String, byte[]> mapA = DH.init();
        String publicKeyA = DH.getPublicKey(mapA);
        String privateKeyA = DH.getPrivateKey(mapA);
        System.out.println("甲方公钥:\n" + publicKeyA);
        System.out.println("甲方私钥:\n" + privateKeyA);

        // 由甲方公钥产生本地密钥对
        Map<String, byte[]> mapB = DH.init(publicKeyA);
        String publicKeyB = DH.getPublicKey(mapB);
        String privateKeyB = DH.getPrivateKey(mapB);
        System.out.println("乙方公钥:\n" + publicKeyB);
        System.out.println("乙方私钥:\n" + privateKeyB);

        String word = "abc";
        System.out.println("原文: " + word);

        // 由甲方公钥，乙方私钥构建密文
        byte[] encWord = DH.encryptDH(word.getBytes(), publicKeyA, privateKeyB);

        // 由乙方公钥，甲方私钥解密
        byte[] decWord = DH.decryptDH(encWord, publicKeyB, privateKeyA);
        System.out.println("解密: " + new String(decWord));
    }
}