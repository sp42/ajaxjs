package com.ajaxjs.config;

import javax.servlet.ServletContext;

/**
 * 当 Servlet 启动时执行的回调
 * 
 * @author Frank Cheung
 *
 */
public interface ServletStartUp {
	public void onStartUp(ServletContext cxt);
}
