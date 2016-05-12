package com.ajaxjs.javatools.task;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 监听器类： 启动时加载任务和启动任务
 *
 */
public class TaskListener implements ServletContextListener {
	public static final long SECOND = 1000L;

	private ServletContext context;

	public TaskListener() {
		System.out.println("LifeCycleListener new ... ");
	}

	public void contextInitialized(ServletContextEvent event) {
		System.out.println("ServletContext Initialized... ");
		context = event.getServletContext();

		String prefix = event.getServletContext().getRealPath("/");
		System.out.println(context + "root path===" + prefix);

		TestTask testtask = new TestTask();
		TaskEngine.scheduleTask(testtask, SECOND * 1, SECOND * 2);
		TaskEngine.start();
	}

	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("ServletContext Destroyed... ");
		TaskEngine.shutdown();
	}
}