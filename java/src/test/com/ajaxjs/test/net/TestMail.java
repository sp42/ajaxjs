package com.ajaxjs.test.net;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.*;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.MailException;
import com.ajaxjs.net.mail.Sender;

public class TestMail {
	@Test
	public void testSend() throws MailException, UnknownHostException, IOException{
		Mail mail = new Mail();
		mail.setMailServer("smtp.163.com");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("2220746@qq.com");
		mail.setContent("paco r u ok?");
		
		assertNotNull(mail);
		Sender sender = new Sender(mail);
		sender.sendMail();
		sender.close();
	}
}
