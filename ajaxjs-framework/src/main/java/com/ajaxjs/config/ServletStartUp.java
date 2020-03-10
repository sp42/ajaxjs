package com.ajaxjs.config;

import javax.servlet.ServletContext;

/**
 * 当 Servlet 启动时执行的回调
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface ServletStartUp {
	public void onStartUp(ServletContext cxt);
}
