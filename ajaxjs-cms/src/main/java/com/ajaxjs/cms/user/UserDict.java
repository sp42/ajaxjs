package com.ajaxjs.cms.user;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public interface UserDict {
	public static final int WECHAT = 1;

	public static final Map<Integer, String> LoginType = new HashMap() {
		private static final long serialVersionUID = -1L;
		{
			put(WECHAT, "微信");
		}
	};

	public static final int UNKNOW = 0;
	public static final int MALE = 1;
	public static final int FEMALE = 2;

	public static final Map<Integer, String> SexGender = new HashMap() {
		private static final long serialVersionUID = -1L;
		{
			put(UNKNOW, "未知");
			put(MALE, "男性");
			put(FEMALE, "女性");
		}
	};
}
