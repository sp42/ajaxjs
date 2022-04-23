package com.ajaxjs.user.sso.model;

/**
 * 刷新令牌
 * 
 * @author Frank Cheung
 *
 */
public class RefreshToken implements ExpiresCheck {
	private Long id;

	/**
	 * 关联的表 auth_access_token 对应 的Access Token 记录
	 */
	private Long tokenId;

	/**
	 * Refresh Token
	 */
	private String refreshToken;

	/**
	 * 过期时间戳
	 */
	private Long expiresIn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTokenId() {
		return tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

}