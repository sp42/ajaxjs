package com.ajaxjs.user.sso.model;

/**
 * 客户端
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

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