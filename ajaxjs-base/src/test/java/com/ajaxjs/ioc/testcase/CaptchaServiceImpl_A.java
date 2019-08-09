package com.ajaxjs.ioc.testcase;

import com.ajaxjs.ioc.Bean;

@Bean
public class CaptchaServiceImpl_A implements CaptchaService {

	@Override
	public void showImage() {
		System.out.println("CaptchaServiceImpl_A");
	}

	@Override
	public boolean checkCode() {
		// TODO Auto-generated method stub
		return false;
	}

}
