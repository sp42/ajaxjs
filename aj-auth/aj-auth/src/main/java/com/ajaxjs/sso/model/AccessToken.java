package com.ajaxjs.sso.model;

import lombok.Data;

/**
 * 访问令牌
 * 
 * @author Frank Cheung
 *
 */
@Data
public class AccessToken implements ExpiresCheck {
	private Long id;

	/**
	 * Access Token
	 */
	private String accessToken;

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

	@Override
	public Long getExpiresIn() {
		return expiresIn;
	}
}