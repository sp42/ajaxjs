package com.ajaxjs.user.sso.model;

/**
 * 错误码相关枚举类
 */
public enum ErrorCodeEnum {
	// @formatter:off 
	INVALID_REQUEST("invalid_request", "请求缺少某个必需参数，包含一个不支持的参数或参数值，或者格式不正确。"),
	INVALID_CLIENT("invalid_client", "请求的 client_id 或 client_secret 参数无效。"),
	INVALID_GRANT("invalid_grant", "请求的 Authorization Code、Access Token、Refresh Token 等信息是无效的。"), 
	UNSUPPORTED_GRANT_TYPE("unsupported_grant_type", "不支持的 grant_type。"),
	INVALID_SCOPE("invalid_scope", "请求的 scope 参数是无效的、未知的、格式不正确的，或所请求的权限范围超过了数据拥有者所授予的权限范围。"), 
	EXPIRED_TOKEN("expired_token", "请求的 Access Token 或 Refresh Token 已过期。"),
	REDIRECT_URI_MISMATCH("redirect_uri_mismatch", "请求的 redirect_uri 所在的域名与开发者注册应用时所填写的域名不匹配。"), 
	INVALID_REDIRECT_URI("invalid_redirect_uri", "请求的回调 URL 不在白名单中。"),
	UNKNOWN_ERROR("unknown_error", "程序发生未知异常，请联系管理员解决。");
	// @formatter:on

	/**
	 * 错误码
	 */
	private String error;

	/**
	 * 错误描述信息
	 */
	private String errorDescription;

	/**
	 * 创建错误码
	 * 
	 * @param error       错误码
	 * @param description 错误描述信息
	 */
	ErrorCodeEnum(String error, String description) {
		this.error = error;
		this.errorDescription = description;
	}

	public String getError() {
		return error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}
}
