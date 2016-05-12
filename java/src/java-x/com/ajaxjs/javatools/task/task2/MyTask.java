package com.ajaxjs.javatools.task.task2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class MyTask implements Runnable {
	private TaskModel taskModel;

	public MyTask() {}

	public MyTask(TaskModel tm) {
		this.taskModel = tm;
	}

	public void run() {
		System.out.println("call at " + (new Date()));
		Class<?> classType = null;
		
		try {
			classType = Class.forName(taskModel.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Method getMethod = null;
		try {
			getMethod = classType.getMethod(taskModel.getMethodName());
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		try {
			getMethod.invoke(classType);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}