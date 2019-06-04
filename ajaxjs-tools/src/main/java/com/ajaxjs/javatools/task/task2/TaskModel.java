package com.ajaxjs.javatools.task.task2;

/**
 * 基于线程池和反射机制实现定时任务
 * @author http://blog.csdn.net/5iasp/article/details/10949925
 *
 */
public class TaskModel {
	private String className;
	private String methodName;
	private long initialDelay;
	private long period;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
}