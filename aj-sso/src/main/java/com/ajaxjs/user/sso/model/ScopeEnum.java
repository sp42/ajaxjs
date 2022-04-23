package com.ajaxjs.user.sso.model;

/**
 * 权限范围
 */
public enum ScopeEnum {
	BASIC("basic", "基础权限"), SUPER("super", "所有权限");

	private String code;
	
	private String description;

	/**
	 * 
	 * @param code
	 * @param description
	 */
	ScopeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
