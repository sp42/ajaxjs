package com.ajaxjs.net.mail;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

public class Test {
	@org.junit.Test
	public void test163() throws MailException, UnknownHostException, IOException {
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