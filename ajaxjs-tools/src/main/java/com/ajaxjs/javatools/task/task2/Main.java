package com.ajaxjs.javatools.task.task2;

import java.util.List;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ScheduledExecutorService;  
import java.util.concurrent.TimeUnit; 

public class Main {
	public static void main(String[] args) {
		ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
		/*
		 * TaskModel tm=new TaskModel();
		 * tm.setClassName("com.yanek.task.TaskA"); tm.setMethodName("testA");
		 * tm.setInitialDelay(3); tm.setPeriod(5);
		 */
		List<TaskModel> tasks = XmlReader.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			TaskModel tm = (TaskModel) tasks.get(i);
			scheduExec.scheduleAtFixedRate(new MyTask(tm), tm.getInitialDelay(), tm.getPeriod(), TimeUnit.SECONDS);
		}
	}
}
