package com.ajaxjs.workflow.process;

import java.util.Date;

import com.ajaxjs.framework.BaseModel;

/**
 * 流程工作单实体类（一般称为流程实例）
 * 
 */
public class ProcessActive extends BaseModel {
	private static final long serialVersionUID = -8335779448165343933L;

	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 流程定义ID
	 */
	private Long processId;

	/**
	 * 流程实例创建者ID
	 */
	private Long creator;

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
	private Long updator;

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

	public Long getCreator() {
		return creator;
	}

	public void setCreator(Long creator) {
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

	public Long getUpdator() {
		return updator;
	}

	public void setUpdator(Long lastUpdator) {
		this.updator = lastUpdator;
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
		sb.append("ActiveProcess(id=").append(getId());
		sb.append(",processId=").append(processId);
		sb.append(",creator=").append(creator);
		sb.append(",createDate").append(getCreateDate());
		sb.append(",No=").append(orderNo).append(")");

		return sb.toString();
	}
}
