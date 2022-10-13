package com.ajaxjs.wechat.applet.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.ajaxjs.util.StrUtil;

public class RsaCryptoUtil {
	private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";

	public static String encryptOAEP(String message, X509Certificate certificate) throws IllegalBlockSizeException {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
			byte[] data = message.getBytes(StandardCharsets.UTF_8);
			byte[] ciphertext = cipher.doFinal(data);

			return Base64.getEncoder().encodeToString(ciphertext);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("无效的证书", e);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
		}
	}

	public static String decryptOAEP(String ciphertext, PrivateKey privateKey) throws BadPaddingException {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] data = Base64.getDecoder().decode(ciphertext);

			return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("无效的私钥", e);
		} catch (BadPaddingException | IllegalBlockSizeException e) {
			throw new BadPaddingException("解密失败");
		}
	}

	/**
	 * 对签名数据进行签名。
	 * 
	 * 使用商户私钥对待签名串进行 SHA256 with RSA 签名，并对签名结果进行 Base64 编码得到签名值。
	 * 
	 * @param message
	 * @return 签名结果
	 */
	public static String sign(PrivateKey privateKey, byte[] message) {
		try {
			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initSign(privateKey);
			sign.update(message);

			return StrUtil.base64Encode(sign.sign());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("当前 Java 环境不支持 SHA256withRSA", e);
		} catch (SignatureException e) {
			throw new RuntimeException("签名计算失败", e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException("无效的私钥", e);
		}
	}
}
