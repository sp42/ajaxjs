package com.ajaxjs.net;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.Sender;

public class TestMail {
	@Test
	public void test163() {
		Mail mail = new Mail();
		mail.setMailServer("smtp.163.com");
		mail.setAccount("pacoweb");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("sp42@qq.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");

		assertTrue(Sender.send(mail));
	}

}