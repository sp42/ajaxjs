package com.github.chaijunkun.wechat.common.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * AES加密解密组件
 * @author chaijunkun
 * @since 2016年9月3日
 */
public class AESUtil {

	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
	
	static{
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * AES加密
	 * @param plain 明文
	 * @param key 密钥
	 * @return
	 * @throws AESEncryptionException 
	 */
	public static byte[] encrypt(byte[] plain, byte[] key) throws AESEncryptionException {
		try{
			Cipher cipher = Cipher.getInstance(ALGORITHM);  
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES"); //生成加密解密需要的Key  
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);  
			return cipher.doFinal(plain);  
		}catch(Exception e){
			throw new AESEncryptionException(e);
		}
	}

	/**
	 * AES解密
	 * @param cipherData  密文
	 * @param key 密钥
	 * @return
	 * @throws AESDecryptionException 
	 */
	public static byte[] decrypt(byte[] cipherData, byte[] key) throws AESDecryptionException {
		try{
			Cipher cipher = Cipher.getInstance(ALGORITHM);  
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);  
			return cipher.doFinal(cipherData);  
		}catch(Exception e){
			throw new AESDecryptionException(e);
		}
	}
	
}