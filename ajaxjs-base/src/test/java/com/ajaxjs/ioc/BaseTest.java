package com.ajaxjs.ioc;

import com.ajaxjs.ioc.testcase.Subject;
import com.ajaxjs.util.logger.LogHelper;

public class BaseTest implements Subject {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseTest.class);
	
	@Override
	public void doIt() {
		LOGGER.info("print BaseTest");
	}
}