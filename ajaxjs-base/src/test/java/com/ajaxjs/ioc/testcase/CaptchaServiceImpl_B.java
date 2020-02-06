package com.ajaxjs.ioc.testcase;

import java.util.logging.Logger;

import com.ajaxjs.ioc.Bean;

@Bean
public class CaptchaServiceImpl_B implements CaptchaService {
	@Override
	public void showImage() {
		Logger.getGlobal().info("CaptchaServiceImpl_B");
	}

	@Override
	public boolean checkCode() {
		return false;
	}
}
