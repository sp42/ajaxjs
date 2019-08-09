package com.ajaxjs.net;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.net.http.Tools;
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
		mail.setSubject("hihi44你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");

		assertTrue(Sender.send(mail));
	}
	
//	@Test
	public void testTools() throws IOException {
		boolean is = Tools.isDomianRegisterAvailable("q4353qfdsf34z.com");
		assertFalse(is);
		is = Tools.isDomianRegisterAvailable("qq.com");
		assertTrue(is);
	}

//	@Test
	public void testGetWhois() throws IOException {
		Map<String, String> text = Tools.getWhois("qq.com");
		System.out.println(text);
	}

}