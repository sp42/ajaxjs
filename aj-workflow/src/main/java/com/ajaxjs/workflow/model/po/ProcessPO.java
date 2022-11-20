package com.ajaxjs.workflow.model.po;

import com.ajaxjs.workflow.model.ProcessModel;

/**
 * 流程
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ProcessPO extends BasePersistantObject {
	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 创建者
	 */
	private Long creator;

	/**
	 * 流程定义显示名称
	 */
	private String displayName;

	/**
	 * 流程定义模型
	 */
	private ProcessModel model;

	/**
	 * 流程定义类型（预留字段）
	 */
	private String type;

	/**
	 * 当前流程的实例 url（一般为流程第一步的 url） 该字段可以直接打开流程申请的表单
	 */
	private String instanceUrl;

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

	public ProcessModel getModel() {
		return model;
	}

	public void setModel(ProcessModel model) {
		this.model = model;
	}

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
		this.creator = creator;
	}

}
