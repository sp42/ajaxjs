/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.controller;

import java.util.logging.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ajaxjs.mysql.db.UserDBConnections;
import com.ajaxjs.mysql.model.AppUser;

public class ResourceSessionListener implements HttpSessionListener {
	private static Logger logger = Logger.getLogger(ResourceSessionListener.class.getName());

	public void sessionCreated(HttpSessionEvent evt) {
	}

	public void sessionDestroyed(HttpSessionEvent evt) {
		AppUser appUser = AppUser.class.cast(evt.getSession().getAttribute(AppUser.SESSION_ATTRIBUTE));
		UserDBConnections conns = UserDBConnections.class.cast(evt.getSession().getAttribute("UserDBConnections"));
		if (conns != null) {
			if (appUser != null) {
				logger.info("Session destroyed. Close all connections held by " + appUser.getName());
			}
			conns.setValid(false);
			conns.close();
			evt.getSession().removeAttribute("UserDBConnections");
		}
	}
}
