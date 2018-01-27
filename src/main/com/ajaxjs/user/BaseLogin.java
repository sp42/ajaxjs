package com.ajaxjs.user;

public class BaseLogin {
	public static final int loginType_password = 1;
	public static final int loginType_3rd = 2;
	public static final int loginType_token = 4;
	/**
	 * 以用户名登录
	 */
	public static final int passWordLoginType_username = 1;
	
	/**
	 * 以邮件登录
	 */
	public static final int passWordLoginType_email = 2;
	
	/**
	 * 以手机登录
	 */
	public static final int passWordLoginType_phone = 4;
}
