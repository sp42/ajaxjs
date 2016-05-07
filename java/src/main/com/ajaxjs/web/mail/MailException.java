package com.ajaxjs.web.mail;

/**
 * 扩展的一个 mail 异常类
 * 
 * @author frank
 */
public class MailException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param string
	 * @param errCode 出错代码
	 */
	public MailException(String string, int errCode) {
		super(string);
		this.errCode = errCode; 
	}

	/**
	 * 出错代码，卡在哪里了
	 */
	public int errCode;
}
