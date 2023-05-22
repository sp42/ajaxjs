package com.github.chaijunkun.wechat.common.util;

/**
 * AES加密异常的包装类
 * @author chaijunkun
 * @since 2016年9月3日
 */
public class AESEncryptionException extends Exception {

	private static final long serialVersionUID = -1333120724817973636L;

	public AESEncryptionException(String message) {
		super(message);
	}

	public AESEncryptionException(Throwable cause) {
		super(cause);
	}

}
