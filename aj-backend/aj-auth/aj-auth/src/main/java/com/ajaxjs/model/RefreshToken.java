package com.ajaxjs.model;

import lombok.Data;

/**
 * 刷新令牌
 * 
 * @author Frank Cheung
 *
 */
@Data
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
}