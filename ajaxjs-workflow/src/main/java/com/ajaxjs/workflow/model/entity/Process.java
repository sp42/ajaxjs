package com.ajaxjs.workflow.model.entity;

import com.ajaxjs.framework.BaseModel;

public class Process extends BaseModel {
	private static final long serialVersionUID = -2466821536491858398L;
	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 流程定义显示名称
	 */
	private String displayName;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

	/**
	 * 流程定义类型（预留字段）
	 */
	private String type;
	
	/**
	 * 当前流程的实例url（一般为流程第一步的url） 该字段可以直接打开流程申请的表单
	 */
	private String instanceUrl;
}
