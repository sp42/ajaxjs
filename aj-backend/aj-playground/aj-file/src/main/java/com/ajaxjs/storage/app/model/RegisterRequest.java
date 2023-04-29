package com.ajaxjs.storage.app.model;

import com.ajaxjs.storage.app.ApplicationSettings;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class RegisterRequest {
	/**
	 * 应用名称
	 */
	private String name;

	/**
	 * 应用描述
	 */
	private String description;

	/**
	 * 应用设置
	 */
	private ApplicationSettings settings;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApplicationSettings getSettings() {
		return settings;
	}

	public void setSettings(ApplicationSettings settings) {
		this.settings = settings;
	}

}
