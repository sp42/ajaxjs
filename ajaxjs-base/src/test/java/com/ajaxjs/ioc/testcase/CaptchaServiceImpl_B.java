package com.ajaxjs.ioc.testcase;

import com.ajaxjs.ioc.Bean;

@Bean
public class CaptchaServiceImpl_B implements CaptchaService {
	@Override
	public void showImage() {
		System.out.println("CaptchaServiceImpl_B");

	}

	@Override
	public boolean checkCode() {
		return false;
	}
}
