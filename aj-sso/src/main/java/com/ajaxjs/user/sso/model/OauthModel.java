package com.ajaxjs.user.sso.model;

import com.ajaxjs.framework.IBaseModel;

/**
 * OAuth 协议标准对象
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface OauthModel {
	/**
	 * 
	 */
	public static class Argee implements IBaseModel {
		/**
		 * 状态码，一般是 200
		 */
		public Integer code;

		/**
		 * 跳转地址
		 */
		public String redirect_uri;
	}
}
