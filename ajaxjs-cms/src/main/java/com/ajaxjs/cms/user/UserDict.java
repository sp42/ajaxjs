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
}
