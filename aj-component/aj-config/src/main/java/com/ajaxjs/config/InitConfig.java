package com.ajaxjs.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 配置中心的启动入口
 * https://www.cnblogs.com/yihuihui/p/12034522.html
 * 
 * @author sp42 frank@ajaxjs.com
 */
@WebListener
@WebFilter(urlPatterns = "/*")
public class InitConfig implements ServletContextListener {
	@Autowired
	private ConfigService cfg;

	@Override
	public void contextInitialized(ServletContextEvent e) {
		cfg.init(e.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
