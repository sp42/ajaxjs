package com.ajaxjs.ioc.testcase;

public class LoginAction {
	private CaptchaService captchaService;

	public CaptchaService getCaptchaService() {
		return captchaService;
	}

	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
	}
	
	public SendSMSService getSendSMSService() {
		return sendSMSService;
	}

	public void setSendSMSService(SendSMSService sendSMSService) {
		this.sendSMSService = sendSMSService;
	}

	private SendSMSService sendSMSService;
}
