package com.ajaxjs.weixin.open_account.model;

/**
 * 访问令牌，接口调用凭据。注意这里包含了 AccessToken + JsApiTicket
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class AccessToken extends BaseModel {
	/**
	 * 获取到的凭证
	 */
	private String access_token;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}
