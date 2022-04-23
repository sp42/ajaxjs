package com.ajaxjs.user.sso.model;

/**
 * 
 * @author Frank Cheung
 *
 */
public class ClientUser {
	private Long id;

	private Long authClientId;

	private Long userId;

	private Long authScopeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAuthClientId() {
		return authClientId;
	}

	public void setAuthClientId(Long authClientId) {
		this.authClientId = authClientId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAuthScopeId() {
		return authScopeId;
	}

	public void setAuthScopeId(Long authScopeId) {
		this.authScopeId = authScopeId;
	}
}