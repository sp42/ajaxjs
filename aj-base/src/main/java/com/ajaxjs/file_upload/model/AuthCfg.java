package com.ajaxjs.file_upload.model;

/**
 * 认证配置
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class AuthCfg {
	private String clientId;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
