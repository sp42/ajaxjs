/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.entity;

import org.snaker.engine.model.TaskModel.PerformType;

/**
 * 历史任务实体类
 * 
 */
public class TaskHistory extends Task {
	private static final long serialVersionUID = 6814632180050362450L;

	public TaskHistory() {
	}

	public TaskHistory(Task task) {	
		setId(task.getId());
		setOrderId(task.getOrderId());
		setCreateDate(task.getCreateDate());
		setName(task.getName());
		setDisplayName(task.getDisplayName());
		setTaskType(task.getTaskType());
		setExpireDate(task.getExpireDate());
		setActionUrl(task.getActionUrl());
		setParentTaskId(task.getParentTaskId());
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
		task.setParentTaskId(getParentTaskId());
		task.setVariable(getVariable());
		task.setPerformType(getPerformType());
		task.setOperator(getOperator());

		return task;
	}

	public boolean isPerformAny() {
		return getPerformType().intValue() == PerformType.ANY.ordinal();
	}
}
