package com.ajaxjs.wechat.applet.model;

/**
 * 登录状态
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class LoginSession {
	/**
	 * 用户唯一标识
	 */
	private String openid;

	/**
	 * 会话密钥
	 */
	private String session_key;

	/**
	 * 用户的系统 id
	 */
	private long userId;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 自定义的会话 id，返回给小程序
	 */
	private String session_id;
}
