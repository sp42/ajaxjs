package com.ajaxjs.user;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户字典，常量
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface UserConstant {
	public static final String LOGIN_PASSED = "PASSED";
	
	public static final int PSW_LOGIN_ID = 1;
	public static final int PSW_LOGIN_EMAIL = 2;
	public static final int PSW_LOGIN_PHONE = 4;
	
	public static final int VERIFIED_EMAIL = 1;
	public static final int VERIFIED_PHONE = 2;

	
	public static final int WECHAT = 1;
	public static final int PASSWORD = 2;
	public static final int WECHAT_MINI = 3;

	public static final Map<Integer, String> LOGIN_TYPE = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -1L;
		{
			put(WECHAT, "微信");
			put(PASSWORD, "普通密码账号");
			put(WECHAT_MINI, "小程序");
		}
	};

	public static final int UNKNOW = 0;
	public static final int MALE = 1;
	public static final int FEMALE = 2;

	public static final Map<Integer, String> SEX_GENDER = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -1L;
		{
			put(UNKNOW, "未知");
			put(MALE, "男性");
			put(FEMALE, "女性");
		}
	};
}
