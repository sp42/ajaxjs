package com.ajaxjs.storage.app.model;

import com.ajaxjs.storage.app.ApplicationSettings;

/**
 * @TableName("ufs_storage_app")
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Application {
	/**
	 * 应用 ID
	 */
	private String id;

	/**
	 * 应用名称
	 */
	private String name;

	/**
	 * 应用描述
	 */
	private String description;

	/**
	 * 存储策略
	 */
	private String strategyIds;

	/**
	 * 
	 */
	private Boolean checkFileField = true;

	/**
	 * 应用设置
	 */
	private ApplicationSettings settings = new ApplicationSettings();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getStrategyIds() {
		return strategyIds;
	}

	public void setStrategyIds(String strategyIds) {
		this.strategyIds = strategyIds;
	}

	public Boolean getCheckFileField() {
		return checkFileField;
	}

	public void setCheckFileField(Boolean checkFileField) {
		this.checkFileField = checkFileField;
	}

	public ApplicationSettings getSettings() {
		return settings;
	}

	public void setSettings(ApplicationSettings settings) {
		this.settings = settings;
	}
}
