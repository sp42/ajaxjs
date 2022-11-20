/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.ajaxjs.workflow.model.po;

import java.io.Serializable;

/**
 * 任务参与者实体类
 * 
 */
public class TaskActor implements Serializable {
	private static final long serialVersionUID = 2969915022122094614L;

	/**
	 * 关联的任务ID
	 */
	private Long taskId;

	/**
	 * 关联的参与者ID（参与者可以为用户、部门、角色）
	 */
	private Long actorId;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getActorId() {
		return actorId;
	}

	public void setActorId(Long actorId) {
		this.actorId = actorId;
	}
}
