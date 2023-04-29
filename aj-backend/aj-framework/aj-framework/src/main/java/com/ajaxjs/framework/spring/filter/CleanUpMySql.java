package com.ajaxjs.framework.spring.filter;

import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Driver;

/**
 * web应用程序似乎启动了一个名为[mysql-cj-abandoned-connection-cleanup]的线程，但未停止，可能会造成内存泄漏...
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class CleanUpMySql implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			Enumeration<Driver> drivers = DriverManager.getDrivers();

			while (drivers.hasMoreElements()) {
				Driver driver = (Driver) drivers.nextElement();
				DriverManager.deregisterDriver(driver);
//				System.out.println("deregistering jdbc driver: " + driver);
			}

			AbandonedConnectionCleanupThread.uncheckedShutdown();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("销毁工作异常");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	}

}
