package com.ajaxjs.sso.model;

import lombok.Data;

/**
 * 客户端
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Data
public class ClientDetails {
	private Long id;

	/**
	 * 接入的客户端ID
	 */
	private String clientId;

	/**
	 * 接入的客户端的名称
	 */
	private String name;

	/**
	 * 接入的客户端的密钥
	 */
	private String clientSecret;

	/**
	 * 回调地址
	 */
	private String redirectUri;

	private String content;

	private Integer status;
}