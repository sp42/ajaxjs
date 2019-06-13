package com.ajaxjs.cms.utils.email;

import com.ajaxjs.net.mail.Mail;

public interface SendEmail {
	/**
	 * 发送邮件
	 * 
	 * @return true = 发送成功
	 */
	public boolean sendMail(Mail mail);
}
