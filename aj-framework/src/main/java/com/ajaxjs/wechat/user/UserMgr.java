package com.ajaxjs.wechat.user;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.ajaxjs.wechat.applet.model.LoginSession;

/**
 * 用户管理
 * 
 * @author Frank Cheung
 *
 */
public class UserMgr {
	/**
	 * 用户登录会话。
	 * 
	 * Key 是 session id，外显用
	 */
	public static Map<String, LoginSession> SESSION = new ConcurrentHashMap<>();

	/**
	 * 用户是否已经登录
	 * 
	 * @param sessionId 用户会话外显 id
	 * @return true 表示登录
	 */
	public static boolean isLogined(String sessionId) {
		return SESSION.containsKey(sessionId);
	}

	/**
	 * 用户是否已经登录
	 * 
	 * 完整检查版本
	 * 
	 * @param sessionId 用户会话外显 id
	 * @return true 表示登录
	 */
	public static LoginSession isLoginedCheck(String sessionId) {
		Objects.requireNonNull(sessionId, "缺少 用户会话 id 参数！");

		if (!isLogined(sessionId))
			throw new SecurityException("用户 " + sessionId + " 未登录！");

		return SESSION.get(sessionId);
	}
}
