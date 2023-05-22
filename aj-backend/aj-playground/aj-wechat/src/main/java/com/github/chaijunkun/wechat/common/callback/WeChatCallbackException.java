package com.github.chaijunkun.wechat.common.callback;

/**
 * 微信回调时产生的异常定义
 * @author chaijunkun
 * @since 2015年4月27日
 */
public class WeChatCallbackException extends Exception {
	
	private static final long serialVersionUID = -3031178332014649011L;
	
	/** 回调产生异常的错误枚举 */
	public static enum CallbackErrEnum {
		/** 回调请求签名不正确 */
		CallbackRequestSignErr(1001, "回调请求签名不正确"),
		/** 回调消息签名不正确 */
		CallbackMsgSignErr(1002, "回调消息签名不正确"),
		/** 回调解密错误 */
		CallbackDecryptionErr(1003, "回调解密错误"),
		/** 回调加密错误 */
		CallbackEncryptionErr(1004, "回调加密错误"),
		/** AES加解密密钥不正确 */
		AESKeyErr(1003, "AES加解密密钥不正确"),
		/** 系统错误 */
		SysErr(9999, "系统错误");
		
		/** 错误代码 */
		private int errcode;
		
		/** 错误消息 */
		private String message;
		
		private CallbackErrEnum(int errcode, String message){
			this.errcode = errcode;
			this.message = message;
		}
		/**
		 * 获取错误代码
		 * @return 错误代码
		 */
		public int getErrcode() {
			return errcode;
		}
		
		/**
		 * 获取错误消息
		 * @return 错误消息
		 */
		public String getMessage() {
			return message;
		}
	}
	
	/** 错误代码 */
	private int errcode;
	
	public WeChatCallbackException(int errcode, String message) {
		super(message);
		this.errcode = errcode;
	}
	
	public WeChatCallbackException(int errcode, String message, Throwable cause) {
		super(message, cause);
		this.errcode = errcode;
	}
	
	public WeChatCallbackException(CallbackErrEnum errEnum) {
		super(errEnum.getMessage());
		this.errcode = errEnum.getErrcode();
	}
	
	public WeChatCallbackException(CallbackErrEnum errEnum, Throwable cause) {
		super(errEnum.getMessage(), cause);
		this.errcode = errEnum.getErrcode();
	}

	/**
	 * 获取错误代码
	 * @return 错误代码
	 */
	public int getErrcode() {
		return errcode;
	}

	/**
	 * 设置错误代码
	 * @param errcode 错误代码
	 */
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	
}
