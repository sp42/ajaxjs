package com.ajaxjs.util.ioc.testcase;

import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;

@Bean
public class LoginAction {
	@Resource("CaptchaServiceImpl_A")
	private CaptchaService captchaService;
	
	@Resource("CaptchaServiceImpl_B")
	public CaptchaService captchaService2;

	public CaptchaService getCaptchaService() {
		return captchaService;
	}

 
	public SendSMSService getSendSMSService() {
		return sendSMSService;
	}

	public void setSendSMSService(SendSMSService sendSMSService) {
		this.sendSMSService = sendSMSService;
	}

	private SendSMSService sendSMSService;
}
