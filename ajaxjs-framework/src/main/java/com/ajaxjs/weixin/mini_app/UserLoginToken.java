package com.ajaxjs.weixin.mini_app;

/**
 * 小程序用户登录的凭证
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserLoginToken {
	/**
	 * 用户 id
	 */
	private Long userId;

	private String openId;

	/**
	 * 服务端会话密钥
	 */
	private String sessionKey;

	/**
	 * 客户端密钥（业务登录凭证）
	 */
	private String sessionId;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
