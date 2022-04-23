package com.ajaxjs.user.sso.model;

/**
 * 可被访问的用户权限
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}