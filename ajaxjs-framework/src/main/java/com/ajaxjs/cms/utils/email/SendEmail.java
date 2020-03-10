package com.ajaxjs.cms.utils.email;

import com.ajaxjs.net.mail.Mail;

/**
 * 发送邮件服务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface SendEmail {
	/**
	 * 发送邮件
	 * 
	 * @return true = 发送成功
	 */
	public boolean sendMail(Mail mail);
}
