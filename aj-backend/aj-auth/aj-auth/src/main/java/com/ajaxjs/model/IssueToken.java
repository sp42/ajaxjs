package com.ajaxjs.model;

import com.ajaxjs.framework.IBaseModel;

import lombok.Data;

/**
 * 颁发 Token 时候所需的信息
 * 
 * @author Frank Cheung
 *
 */
@Data
public class IssueToken implements IBaseModel {
	/**
	 * Access Token
	 */
	private String access_token;

	/**
	 * 刷新 Token
	 */
	private String refresh_token;

	/**
	 * 过期时间
	 */
	private Long expires_in;

	/**
	 * 权限范围
	 */
	private String scope;

}
