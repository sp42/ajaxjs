package com.ajaxjs.storage.app;

import java.util.EventObject;

import com.ajaxjs.storage.app.model.Application;

/**
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ApplicationRegisteredEvent extends EventObject {
	private static final long serialVersionUID = 512920258496027543L;

	private Application application;

	public ApplicationRegisteredEvent(Object source, Application application) {
		super(source);

		this.application = application;
	}

	public Application getApplication() {
		return application;
	}
}
