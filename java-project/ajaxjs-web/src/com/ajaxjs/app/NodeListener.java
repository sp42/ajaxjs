package com.ajaxjs.app;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public class NodeListener implements ServletRequestListener {	
	public static boolean isInited;
	
	@Override
	public void requestInitialized(ServletRequestEvent e) {
		new NodeProcessor((HttpServletRequest) e.getServletRequest());
	}

	@Override
	public void requestDestroyed(ServletRequestEvent e) {
	}
	
}
