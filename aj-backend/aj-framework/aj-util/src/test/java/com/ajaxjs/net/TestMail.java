package com.ajaxjs.net;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.Sender;

public class TestMail {
//	@Test
	public void test163() {
		Mail mail = new Mail();
		mail.setMailServer("smtp.163.com");
		mail.setAccount("pacoweb");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("frank@ajaxjs.com");
		mail.setSubject("你好容祯");
		mail.setHTML_body(false);
		mail.setContent("xccx我希望可以跟你做朋友34354344");

		assertTrue(Sender.send(mail));
	}

	@Test
	public void testYM163() {
		Mail mail = new Mail();
		mail.setMailServer("smtp.ym.163.com");
		mail.setAccount("admin@bgdiving.com");
		mail.setFrom("admin@bgdiving.com");
		mail.setTo("frank@ajaxjs.com");
		mail.setSubject("你好容祯");
		mail.setHTML_body(false);
		mail.setContent("xccx我希望可以跟你做朋友34354344");

		assertTrue(Sender.send(mail));
	}

}