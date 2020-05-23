package com.ajaxjs;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.ajaxjs.net.mail.Mail;

/**
 * 各种服务。应继承该类进行扩展。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class ThirdServices {
	/**
	 * 发送邮件：邮件实体/是否成功
	 */
	private Function<Mail, Boolean> sendEmail;

	/**
	 * 发送短信:短信号码/短信内容/是否成功
	 */
	private BiFunction<String, String, Boolean> sendSms;

	public Function<Mail, Boolean> getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(Function<Mail, Boolean> sendEmail) {
		this.sendEmail = sendEmail;
	}

	public BiFunction<String, String, Boolean> getSendSms() {
		return sendSms;
	}

	public void setSendSms(BiFunction<String, String, Boolean> sendSms) {
		this.sendSms = sendSms;
	}
}
