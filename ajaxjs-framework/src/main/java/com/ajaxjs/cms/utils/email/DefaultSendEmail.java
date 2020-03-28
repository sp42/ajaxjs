package com.ajaxjs.cms.utils.email;

import java.io.IOException;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.MailException;
import com.ajaxjs.net.mail.Sender;

public class DefaultSendEmail implements SendEmail {

	public boolean sendMail(Mail mail) {
		mail.setMailServer("smtp.163.com");
		mail.setAccount("pacoweb");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("sp42@qq.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");

		try (Sender sender = new Sender(mail)) {
			return sender.sendMail();
		} catch (IOException | MailException e) {
			e.printStackTrace();
			return false;
		}
	}

}
