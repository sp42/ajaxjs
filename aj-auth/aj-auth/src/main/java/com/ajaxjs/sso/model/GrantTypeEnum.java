package com.ajaxjs.sso.model;

/**
 * 授权方式
 * 
 * @author Frank Cheung
 *
 */
public enum GrantTypeEnum {
	/**
	 * 授权码模式
	 */
	AUTHORIZATION_CODE("authorization_code");

	private String type;

	/**
	 * 授权方式，当前只有 authorization_code
	 * 
	 * @param type
	 */
	GrantTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
