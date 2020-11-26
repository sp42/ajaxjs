package com.ajaxjs.workflow.task;

import com.ajaxjs.sql.annotation.IgnoreDB;
import com.ajaxjs.workflow.model.TaskModel.PerformType;

/**
 * 历史任务实体类。
 * 
 * history 减少了 version 字段
 */
public class TaskHistory extends Task {
	private static final long serialVersionUID = 6814632180050362450L;

	@IgnoreDB
	@Override
	public Integer getVersion() {
		return null;
	}

	/**
	 * 创建历史任务
	 */
	public TaskHistory() {
	}

	/**
	 * 根据 task 创建历史任务
	 * 
	 * @param task 任务对象
	 */
	public TaskHistory(Task task) {
		setId(task.getId());
		setOrderId(task.getOrderId());
		setCreateDate(task.getCreateDate());
		setName(task.getName());
		setDisplayName(task.getDisplayName());
		setTaskType(task.getTaskType());
		setExpireDate(task.getExpireDate());
		setActionUrl(task.getActionUrl());
		setParentId(task.getParentId());
		setVariable(task.getVariable());
		setPerformType(task.getPerformType());
		setOperator(task.getOperator());
	}

	/**
	 * 根据历史任务产生撤回的任务对象
	 * 
	 * @return 任务对象
	 */
	public Task undoTask() {
		Task task = new Task();
		task.setOrderId(getOrderId());
		task.setName(getName());
		task.setDisplayName(getDisplayName());
		task.setTaskType(getTaskType());
		task.setExpireDate(getExpireDate());
		task.setActionUrl(getActionUrl());
		task.setParentId(getParentId());
		task.setVariable(getVariable());
		task.setPerformType(getPerformType());
		task.setOperator(getOperator());

		return task;
	}

	/**
	 * 是否任意的（ANY）参与类型
	 * 
	 * @return true 表示为任意的参与类型
	 */
	@IgnoreDB
	public boolean isPerformAny() {
		return getPerformType().intValue() == PerformType.ANY.ordinal();
	}
}
