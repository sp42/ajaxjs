package com.ajaxjs.weixin.mini_app.model;

/**
 * 登录或注册后，返回给客户端（小程序）用的 session 还有用户 id。 我们采用明文 id 和 session
 * 加密身份双重传输。因为通信过程是无态的，必须附加这些参数。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserSession {
	/**
	 * 这是加密的过的 Session
	 */
	private String sessionId;

	/**
	 * 明文的用户 id
	 */
	private Long userId;

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
