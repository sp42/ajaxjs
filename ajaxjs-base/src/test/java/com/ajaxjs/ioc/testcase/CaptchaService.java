package com.ajaxjs.ioc.testcase;

// 图片验证码服务
public interface CaptchaService {
	/**
	 * 加载验证码图片
	 */
	void showImage();

	/**
	 * 检查验证码是否正确
	 * @return
	 */
	boolean checkCode();
}
