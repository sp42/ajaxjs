package com.ajaxjs.util.cryptography.asymmetric;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import org.springframework.util.Base64Utils;

import com.ajaxjs.util.cryptography.encryption.SymmetriCipherInfo;

/**
 * DH 加解密工具类
 */
public class DH extends Common {
	/**
	 * 定义加密方式
	 */
	private static final String KEY_DH = "DH";

	/**
	 * 默认密钥字节数
	 */
	private static final int KEY_SIZE = 1024;

	/**
	 * DH加密下需要一种对称加密算法对数据加密，这里我们使用DES，也可以使用其他对称加密算法
	 */
	private static final String KEY_DH_PUBLICKEY = "DHPublicKey";

	private static final String KEY_DH_PRIVATEKEY = "DHPrivateKey";

	/**
	 * 初始化甲方密钥
	 * 
	 * @return
	 */
	public static Map<String, byte[]> init() {
		return getKeyPair(KEY_DH, KEY_SIZE, KEY_DH_PUBLICKEY, KEY_DH_PRIVATEKEY);
	}

	/**
	 * 初始化乙方密钥
	 * 
	 * @param key 甲方密钥
	 * @return
	 */
	public static Map<String, byte[]> init(String key) {
		try {
			// 解析甲方密钥
			PublicKey publicKey = KeyFactory.getInstance(KEY_DH).generatePublic(new X509EncodedKeySpec(Base64Utils.decodeFromString(key)));
			// 由甲方公钥构建乙方密钥
			DHParameterSpec spec = ((DHPublicKey) publicKey).getParams();
			KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_DH);
			generator.initialize(spec);

			return getKeyPair(generator, KEY_DH_PUBLICKEY, KEY_DH_PRIVATEKEY);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * DH
	 * 
	 * @param isEncrypt  是否加密(true)，反之为解密（false）
	 * @param data       待处理数据
	 * @param publicKey  乙方公钥
	 * @param privateKey 甲方私钥
	 * @return
	 */
	public static byte[] doDH(boolean isEncrypt, byte[] data, String publicKey, String privateKey) {
		// 生成本地密钥
		SecretKey secretKey = getSecretKey(publicKey, privateKey);
		// 数据解密
		return SymmetriCipherInfo.doCipher(secretKey.getAlgorithm(), Cipher.DECRYPT_MODE, secretKey, null, data);
	}

	/**
	 * DH 解密
	 * 
	 * @param data       待解密数据
	 * @param publicKey  乙方公钥
	 * @param privateKey 甲方私钥
	 * @return
	 */
	public static byte[] decryptDH(byte[] data, String publicKey, String privateKey) {
		return doDH(false, data, publicKey, privateKey);
	}

	/**
	 * DH 加密
	 * 
	 * @param data       带加密数据
	 * @param publicKey  甲方公钥
	 * @param privateKey 乙方私钥
	 * @return
	 */
	public static byte[] encryptDH(byte[] data, String publicKey, String privateKey) {
		return doDH(true, data, publicKey, privateKey);
	}

	/**
	 * 获取公钥
	 * 
	 * @param map
	 * @return
	 */
	public static String getPublicKey(Map<String, byte[]> map) {
		return getKey(KEY_DH_PUBLICKEY, map);
	}

	/**
	 * 获取私钥
	 * 
	 * @param map
	 * @return
	 */
	public static String getPrivateKey(Map<String, byte[]> map) {
		return getKey(KEY_DH_PRIVATEKEY, map);
	}

	/**
	 * 构建本地密钥
	 * 
	 * @param publicKey  公钥
	 * @param privateKey 私钥
	 * @return
	 */
	private static SecretKey getSecretKey(String publicKey, String privateKey) {
		try {
			KeyFactory factory = KeyFactory.getInstance(KEY_DH);

			// 初始化公钥
			PublicKey localPublicKey = factory.generatePublic(new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey)));
			// 初始化私钥
			PrivateKey localPrivateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey)));

			KeyAgreement agreement = KeyAgreement.getInstance(factory.getAlgorithm());
			agreement.init(localPrivateKey);
			agreement.doPhase(localPublicKey, true);

			return agreement.generateSecret("DiffieHellman");// 生成本地密钥
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}