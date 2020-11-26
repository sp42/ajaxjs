package com.ajaxjs.workflow.process;

import java.util.Date;

/**
 * 历史流程实例实体类。 history 减少了 version 和 parentNodeName 字段，增加了 endDate 字段
 */
public class ProcessHistory extends ProcessActive {
	private static final long serialVersionUID = 5853727929104539328L;

	/**
	 * 创建历史流程实例
	 */
	public ProcessHistory() {
	}

	/**
	 * 根据流程实创建历史流程实例
	 * 
	 * @param process 活动实例对象
	 */
	public ProcessHistory(ProcessActive process) {
		setId(process.getId());
		setProcessId(process.getProcessId());
		setCreateDate(process.getCreateDate());
		setExpireDate(process.getExpireDate());
		setCreator(process.getCreator());
		setParentId(process.getParentId());
		setPriority(process.getPriority());
		setOrderNo(process.getOrderNo());
		setVariable(process.getVariable());
	}

	/**
	 * 根据历史实例撤回活动实例
	 * 
	 * @return 活动实例对象
	 */
	public ProcessActive undo() {
		ProcessActive active = new ProcessActive();
		active.setId(getId());
		active.setProcessId(getProcessId());
		active.setParentId(getParentId());
		active.setCreator(getCreator());
		active.setCreateDate(getCreateDate());
		active.setUpdator(getUpdator());
		active.setUpdateDate(getEndDate());
		active.setExpireDate(getExpireDate());
		active.setOrderNo(getOrderNo());
		active.setPriority(getPriority());
		active.setVariable(getVariable());
		active.setVersion(0);

		return active;
	}

	/**
	 * 流程实例结束时间
	 */
	private Date endDate;

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
