package com.ajaxjs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.ThirdServices;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.net.mail.Mail;

public class TestThirdServices {
	static ThirdServices services;

	@BeforeClass
	public static void initDb() {
		ConfigService.load("D:\\project\\doctor\\src\\main\\resources\\site_config.json");
		BeanContext.init("com.ajaxjs.cms");
		BeanContext.injectBeans();

		services = BeanContext.getByClass(ThirdServices.class);
		assertNotNull(services);
	}

	@Test
	public void testSendSms() {
		assertTrue(services.getSendSms().apply("13711228150", "Hi, 这是一个测试短信"));
	}

//	@Test
	public void testEmail() {
		Mail mail = new Mail();
		mail.setFrom("admin@bgdiving.com");
		mail.setTo("frank@ajaxjs.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");

		assertTrue(services.getSendEmail().apply(mail));
	}
}
