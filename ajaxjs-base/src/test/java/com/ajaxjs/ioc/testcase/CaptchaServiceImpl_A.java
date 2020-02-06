package com.ajaxjs.ioc.testcase;

import java.util.logging.Logger;

import com.ajaxjs.ioc.Bean;

@Bean
public class CaptchaServiceImpl_A implements CaptchaService {

	@Override
	public void showImage() {
		Logger.getGlobal().info("CaptchaServiceImpl_A");
	}

	@Override
	public boolean checkCode() {
		// TODO Auto-generated method stub
		return false;
	}

}
