package com.github.chaijunkun.wechat.common.util;

/**
 * AES解密异常的包装类
 * @author chaijunkun
 * @since 2016年9月3日
 */
public class AESDecryptionException extends Exception {

	private static final long serialVersionUID = 3682077180671469977L;

	public AESDecryptionException(String message) {
		super(message);
	}

	public AESDecryptionException(Throwable cause) {
		super(cause);
	}

}
