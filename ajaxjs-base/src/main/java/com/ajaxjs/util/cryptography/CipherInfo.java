package com.ajaxjs.util.cryptography;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.ajaxjs.util.logger.LogHelper;

public class CipherInfo {
	private static final LogHelper LOGGER = LogHelper.getLog(CipherInfo.class);

	/**
	 * 
	 */
	private String keyAlgorithm;

	/**
	 * 
	 */
	private String cipherAlgorithm;

	/**
	 * 
	 */
	private int keySize;

	/**
	 * 
	 * @param cipherAlgorithm
	 * @param keyAlgorithm
	 * @param keySize
	 */
	public CipherInfo(String cipherAlgorithm, String keyAlgorithm, int keySize) {
		this.cipherAlgorithm = cipherAlgorithm;
		this.keyAlgorithm = keyAlgorithm;
		this.keySize = keySize;
	}

	/**
	 * 进行加密或解密，三步走
	 * 
	 * @param algorithm 选择的算法
	 * @param mode      模式，是解密还是加密？
	 * @param key       密钥
	 * @param s         输入的内容
	 * @return 结果
	 */
	static byte[] doCipher(String algorithm, int mode, Key key, byte[] s) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(mode, key);

			/*
			 * 为了防止解密时报 javax.crypto.IllegalBlockSizeException: Input length must be
			 * multiple of 8 when decrypting with padded cipher 异常， 不能把加密后的字节数组直接转换成字符串
			 */
			return cipher.doFinal(s);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * AES/DES 专用
	 * 
	 * @param ci
	 * @param mode
	 * @param key
	 * @param bytes
	 * @return
	 */
	static byte[] doCipher(CipherInfo ci, int mode, String key, byte[] bytes) {
		SecretKey _key;// 获得密钥对象

		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(key.getBytes());
			KeyGenerator kg = KeyGenerator.getInstance(ci.getCipherAlgorithm());
			kg.init(ci.getKeySize(), sr);

			_key = kg.generateKey();// 生成密钥
		} catch (NoSuchAlgorithmException e) {
			LOGGER.warning(e);
			return null;
		}

		return doCipher(ci.getCipherAlgorithm(), mode, _key, bytes);
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}

	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
	}

	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	public void setCipherAlgorithm(String cipherAlgorithm) {
		this.cipherAlgorithm = cipherAlgorithm;
	}

}
