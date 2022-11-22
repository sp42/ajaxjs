package com.ajaxjs.workflow.model.po;

import java.io.Serializable;

/**
 * 任务参与者实体类
 * 
 */
public class TaskActor implements Serializable {
	private static final long serialVersionUID = 2969915022122094614L;

	/**
	 * 关联的任务 id
	 */
	private Long taskId;

	/**
	 * 关联的参与者 id（参与者可以为用户、部门、角色）
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
