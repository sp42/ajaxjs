package com.github.chaijunkun.wechat.common.api;

/**
 * 调用API时产生的异常定义
 * @author chaijunkun
 * @since 2015年4月27日
 */
public class WeChatAPIException extends Exception {
	
	private static final long serialVersionUID = -4404821691476273572L;

	/** 调用异常的错误枚举 */
	public static enum APIErrEnum {
		/** 系统错误 */
		SysErr(9999, "系统错误");
		
		/** 错误代码 */
		private int errcode;
		
		/** 错误消息 */
		private String message;
		
		private APIErrEnum(int errcode, String message){
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
	
	public WeChatAPIException(int errcode, String message) {
		super(message);
		this.errcode = errcode;
	}
	
	public WeChatAPIException(int errcode, String message, Throwable cause) {
		super(message, cause);
		this.errcode = errcode;
	}
	
	public WeChatAPIException(APIErrEnum errEnum) {
		super(errEnum.getMessage());
		this.errcode = errEnum.getErrcode();
	}
	
	public WeChatAPIException(APIErrEnum errEnum, Throwable cause) {
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
