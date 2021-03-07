package com.demo.aop;

import java.util.logging.Logger;

import com.ajaxjs.util.ioc.Component;

@Component
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
