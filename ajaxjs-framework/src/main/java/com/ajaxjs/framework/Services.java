package com.ajaxjs.framework;

import com.ajaxjs.net.mail.Mail;

public interface Services {
	/**
	 * 发送邮件
	 * 
	 * @return true = 发送成功
	 */
	public boolean sendMail(Mail mail);
}
