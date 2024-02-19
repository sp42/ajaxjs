package com.ajaxjs.util.cryptography;

import com.ajaxjs.util.EncryptUtil;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * RSA 非对称加密/解密
 */
public class RSA extends Common {
	private static final LogHelper LOGGER = LogHelper.getLog(RSA.class);

	/**
	 * 定义加密方式
	 */
	private final static String KEY_RSA = "RSA";// "RSA/ECB/PKCS1Padding"

	/**
	 * 定义签名算法
	 */
	private final static String KEY_RSA_SIGNATURE = "MD5withRSA";

	/**
	 * 定义公钥算法
	 */
	private final static String KEY_RSA_PUBLIC_KEY = "RSAPublicKey";

	/**
	 * 定义私钥算法
	 */
	private final static String KEY_RSA_PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 初始化密钥
	 * 
	 * 注意这里是生成密钥对 KeyPair，再由密钥对获取公私钥
	 * 
	 * @return 密钥对
	 */
	public static Map<String, byte[]> init() {
		return getKeyPair(KEY_RSA, 1024, KEY_RSA_PUBLIC_KEY, KEY_RSA_PRIVATE_KEY);
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data       加密数据
	 * @param privateKey 私钥
	 * @return
	 */
	public static String sign(byte[] data, String privateKey) {
		// 用私钥对信息生成数字签名
		try {
			Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
			signature.initSign((PrivateKey) restoreKey(false, privateKey));
			signature.update(data);

			return Base64Utils.encodeToString(signature.sign());
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data      加密数据
	 * @param publicKey 公钥
	 * @param sign      数字签名
	 * @return 校验成功返回true，失败返回false
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) {
		try {
			Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
			signature.initVerify((PublicKey) restoreKey(true, publicKey));
			signature.update(data);

			return signature.verify(Base64Utils.decodeFromString(sign));
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			LOGGER.warning(e);
			return false;
		}
	}

	// --------------------------------------------------------------------------------------

	/**
	 * 还原公钥/私钥
	 * 
	 * @param isPublic 是否公钥，反之私钥
	 * @param key
	 * @return
	 */
	private static Key restoreKey(boolean isPublic, String key) {
		byte[] bytes = Base64Utils.decodeFromString(key);

		try {
			KeyFactory f = KeyFactory.getInstance(KEY_RSA);
			return isPublic ? f.generatePublic(new X509EncodedKeySpec(bytes)) : f.generatePrivate(new PKCS8EncodedKeySpec(bytes));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	// ------------------------- PUBLIC KEY ------------------------

	/**
	 * 处理公钥
	 * 
	 * @param isEncrypt 是否加密(true)，反之为解密（false）
	 * @param data
	 * @param key
	 * @return
	 */
	private static byte[] action(boolean isEncrypt, boolean isPublic, byte[] data, String key) {
		int mode = isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
		return EncryptUtil.doCipher(KEY_RSA, mode, restoreKey(isPublic, key), null, data);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data 待加密数据
	 * @param key  公钥
	 * @return
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key) {
		return action(true, true, data, key);
	}

	/**
	 * 公钥解密
	 * 
	 * @param data 加密数据
	 * @param key  公钥
	 * @return
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) {
		return action(false, true, data, key);
	}

	// ------------------------- PRIVATE KEY ------------------------

	/**
	 * 私钥加密
	 * 
	 * @param data 待加密数据
	 * @param key  私钥
	 * @return
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) {
		return action(true, false, data, key);
	}

	/**
	 * 私钥解密
	 * 
	 * @param data 加密数据
	 * @param key  私钥
	 * @return
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) {
		return action(false, false, data, key);
	}

	// ------------------------- GET KEY AS STRING ------------------------

	/**
	 * 获取公钥
	 * 
	 * @param map
	 * @return
	 */
	public static String getPublicKey(Map<String, byte[]> map) {
		return getKey(KEY_RSA_PUBLIC_KEY, map);
	}

	/**
	 * 获取私钥
	 * 
	 * @param map
	 * @return
	 */
	public static String getPrivateKey(Map<String, byte[]> map) {
		return getKey(KEY_RSA_PRIVATE_KEY, map);
	}

}