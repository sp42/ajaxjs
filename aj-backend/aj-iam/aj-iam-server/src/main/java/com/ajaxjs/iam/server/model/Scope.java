package com.ajaxjs.iam.server.model;

import lombok.Data;

/**
 * 可被访问的用户权限
 * 
 * @author Frank Cheung
 *
 */
@Data
public class Scope {
	private Long id;

	/**
	 * 可被访问的用户的权限范围，比如：basic、super
	 */
	private String name;

	/**
	 * 简介
	 */
	private String content;
}