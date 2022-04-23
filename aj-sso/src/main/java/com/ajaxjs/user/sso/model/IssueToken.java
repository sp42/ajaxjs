package com.ajaxjs.user.sso.model;

/**
 * 颁发 Token 时候所需的信息
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class IssueToken {
	/**
	 * Access Token
	 */
	private String access_token;

	/**
	 * 刷新 Token
	 */
	private String refresh_token;

	/**
	 * 过期时间
	 */
	private Long expires_in;

	/**
	 * 权限范围
	 */
	private String scope;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
