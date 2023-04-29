package com.ajaxjs.user.sso.model;

/**
 * 访问令牌
 * 
 * @author Frank Cheung
 *
 */
public class AccessToken  {
	private Long id;

	/**
	 * Access Token
	 */
	private String accessToken;
	
	/**
	 * Access Token
	 */
	private String refreshToken;

	/**
	 * 关联的用户ID
	 */
	private Long userId;
	/**
	 * 关联的用户名
	 */
	private String userName;

	/**
	 * 接入的客户端ID
	 */
	private Long clientId;

	/**
	 * 过期时间戳
	 */
	private Long expiresIn;

	/**
	 * 授权类型，比如：authorization_code
	 */
	private String grantType;

	/**
	 * 可被访问的用户的权限范围，比如：basic、super
	 */
	private String scope;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	
	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}