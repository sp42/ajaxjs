package com.ajaxjs.ioc;

import com.ajaxjs.util.logger.LogHelper;

class BaseTest implements Subject {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseTest.class);
	
	@Override
	public void doIt() {
		LOGGER.info("print BaseTest");
	}
}