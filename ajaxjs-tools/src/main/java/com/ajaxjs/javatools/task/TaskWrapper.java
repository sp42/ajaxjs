package com.ajaxjs.javatools.task;

/**
 * 任务包装类
 * @author frank
 *
 */
public class TaskWrapper {
	private Runnable task;
	private int priority;
	
	public TaskWrapper(int priority, Runnable task) {
		this.priority = priority;
		this.task = task;
	}

	public Runnable getTask() {
		return task;
	}

	public void setTask(Runnable task) {
		this.task = task;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}