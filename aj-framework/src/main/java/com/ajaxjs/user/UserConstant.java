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
	/**
	 * Session 中获取 user 的 key
	 */
	public static final String USER_SESSION_KEY = "USER";

	public static final String LOGIN_PASSED = "PASSED";

	public static interface Login {
		public static final int PSW_LOGIN_ID = 1;
		public static final int PSW_LOGIN_EMAIL = 2;
		public static final int PSW_LOGIN_PHONE = 4;

		/**
		 * 登录类型
		 * 
		 * @author Frank Cheung
		 *
		 */
		public static interface LoginType {
			/**
			 * 普通密码账号
			 */
			public static final int PASSWORD = 1;

			/**
			 * 微信
			 */
			public static final int WECHAT = 2;

			/**
			 * 微信小程序
			 */
			public static final int WECHAT_APPLET = 3;
		}
	}

	public static final int VERIFIED_EMAIL = 1;
	public static final int VERIFIED_PHONE = 2;

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
