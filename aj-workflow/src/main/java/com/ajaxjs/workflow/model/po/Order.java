package com.ajaxjs.workflow.model.po;

import java.util.Date;

/**
 * 流程工作单实体类（一般称为流程实例）
 */
public class Order extends BasePersistantObject {
	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 流程定义 id
	 */
	private Long processId;

	/**
	 * 流程实例为子流程时，该字段标识父流程实例 id
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
		sb.append("Order(id=").append(getId());
		sb.append(",processId=").append(processId);
		sb.append(",creator=").append(getCreator());
		sb.append(",createDate").append(getCreateDate());
		sb.append(",orderNo=").append(orderNo).append(")");

		return sb.toString();
	}
}