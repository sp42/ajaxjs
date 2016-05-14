package com.ajaxjs.test.net;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.MailException;
import com.ajaxjs.net.mail.Sender;

public class TestMail {
//	@Test
//	public void test163() throws MailException, UnknownHostException, IOException {
//		Mail mail = new Mail();
//		mail.setMailServer("smtp.163.com");
//		mail.setAccount("pacoweb");
//		mail.setPassword("1120");
//		mail.setFrom("pacoweb@163.com");
//		mail.setTo("sp42@qq.com");
//		mail.setSubject("hihi你好");
//		mail.setHTML_body(true);
//		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");
//		
//		try(Sender sender = new Sender(mail)){
//			boolean isOk = sender.sendMail();
//			System.out.println(isOk);
//			assertTrue(isOk);
//		};
//	}
	
	@Test
	public void testQQ() throws MailException, UnknownHostException, IOException {
		Mail mail = new Mail();
		mail.setMailServer("smtp.exmail.qq.com");
//		mail.setPort(465);
		mail.setAccount("zhangxin@3gtv.net");
		mail.setPassword("Tomcom1120");
		mail.setFrom("zhangxin@3gtv.net");
		mail.setTo("zhongxiaoming@3gtv.net");
		mail.setSubject("晓鸣 ，hihi你好，这是我这里发出的测试");
		mail.setHTML_body(true);
		mail.setContent("Test 测试<a href=\"http://qq.com\">fdsfds</a>");
		
		try(Sender sender = new Sender(mail)){
			boolean isOk = sender.sendMail();
			System.out.println(isOk);
			assertTrue(isOk);
		};
	}
}