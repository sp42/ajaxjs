package com.ajaxjs.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.stereotype.Component;

import com.ajaxjs.Version;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

/**
 * Eclipse 提示异常，生产环境应该不会
 * 
 * @author Frank Cheung
 *
 */
//@WebListener
//@Component
public class ContainerContextClosedHandler implements ServletContextListener {
	@SuppressWarnings("deprecation")
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (Version.isDebug) {
			Enumeration<Driver> drivers = DriverManager.getDrivers();

			Driver driver = null;

			// clear drivers
			while (drivers.hasMoreElements()) {
				try {
					driver = drivers.nextElement();
					DriverManager.deregisterDriver(driver);
				} catch (SQLException ex) {
					// deregistration failed, might want to do something, log at the very least
				}
			}

			// MySQL driver leaves around a thread. This static method cleans it up.
			try {
				AbandonedConnectionCleanupThread.shutdown();
			} catch (Exception e) {
				// again failure, not much you can do
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
	}

}
