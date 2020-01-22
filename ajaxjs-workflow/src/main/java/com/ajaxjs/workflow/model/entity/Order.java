/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.entity;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 流程工作单实体类（一般称为流程实例）
 * 
 */
public class Order extends BaseModel {
	private static final long serialVersionUID = -8335779448165343933L;

	/**
	 * 版本
	 */
	private Integer version = 0;

	/**
	 * 流程定义ID
	 */
	private Long processId;

	/**
	 * 流程实例创建者ID
	 */
	private String creator;

	/**
	 * 流程实例为子流程时，该字段标识父流程实例ID
	 */
	private Long parentId;

	/**
	 * 流程实例为子流程时，该字段标识父流程哪个节点模型启动的子流程
	 */
	private String parentNodeName;

	/**
	 * 流程实例期望完成时间
	 */
	private Date expireDate;

	/**
	 * 流程实例上一次更新人员ID
	 */
	private String lastUpdator;

	/**
	 * 流程实例优先级
	 */
	private Integer priority;

	/**
	 * 流程实例编号
	 */
	private String orderNo;

	/**
	 * 流程实例附属变量
	 */
	private String variable;

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getLastUpdator() {
		return lastUpdator;
	}

	public void setLastUpdator(String lastUpdator) {
		this.lastUpdator = lastUpdator;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getParentNodeName() {
		return parentNodeName;
	}

	public void setParentNodeName(String parentNodeName) {
		this.parentNodeName = parentNodeName;
	}

	public String getVariable() {
		return variable;
	}

	public Map<String, Object> getVariableMap() {
		Map<String, Object> map = JsonHelper.parseMap(variable);
		if (map == null)
			return Collections.emptyMap();

		return map;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Order(id=").append(getId());
		sb.append(",processId=").append(processId);
		sb.append(",creator=").append(creator);
		sb.append(",createDate").append(getCreateDate());
		sb.append(",orderNo=").append(orderNo).append(")");

		return sb.toString();
	}
}
