package com.ajaxjs.test.web;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import com.ajaxjs.web.mail.Mail;
import com.ajaxjs.web.mail.Sender;
import com.ajaxjs.web.mail.MailException;

public class TestMail {
	@Test
	public void testGet() throws MailException, UnknownHostException, IOException {
		Mail mail = new Mail();
		mail.setMailServer("smtp.163.com");
		mail.setAccount("pacoweb");
		mail.setPassword("1120");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("sp42@qq.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");
		
		try(Sender sender = new Sender(mail)){
			boolean isOk = sender.sendMail();
			System.out.println(isOk);
			assertTrue(isOk);
		};
	}
}