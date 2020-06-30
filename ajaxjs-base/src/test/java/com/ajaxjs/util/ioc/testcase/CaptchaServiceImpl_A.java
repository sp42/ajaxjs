package com.ajaxjs.util.ioc.testcase;

import java.util.logging.Logger;

import com.ajaxjs.util.ioc.Bean;

@Bean
public class CaptchaServiceImpl_A implements CaptchaService {

	@Override
	public void showImage() {
		Logger.getGlobal().info("CaptchaServiceImpl_A");
	}

	@Override
	public boolean checkCode() {
		return false;
	}

}
