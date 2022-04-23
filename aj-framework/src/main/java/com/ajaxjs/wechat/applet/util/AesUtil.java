package com.ajaxjs.wechat.applet.util;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.ajaxjs.util.StrUtil;

/**
 * 证书和回调报文解密
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class AesUtil {
	private static final String TRANSFORMATION = "AES/GCM/NoPadding";

	private static final int KEY_LENGTH_BYTE = 32;
	private static final int TAG_LENGTH_BIT = 128;

	private final byte[] aesKey;

	/**
	 * 解密器
	 * 
	 * @param key
	 */
	public AesUtil(byte[] key) {
		if (key.length != KEY_LENGTH_BYTE)
			throw new IllegalArgumentException("无效的 ApiV3Key，长度必须为32个字节");

		this.aesKey = key;
	}

	/**
	 * AEAD_AES_256_GCM 解密
	 * 
	 * @param associatedData
	 * @param nonce
	 * @param ciphertext
	 * @return
	 * @throws GeneralSecurityException
	 */
	public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext) throws GeneralSecurityException {
		SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
		GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, spec);
			cipher.updateAAD(associatedData);

			return StrUtil.byte2String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
