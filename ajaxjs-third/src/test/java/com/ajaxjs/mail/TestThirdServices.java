package com.ajaxjs.mail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ajaxjs.app.ThirdPartyService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.net.mail.Mail;

public class TestThirdServices {

	@Test
	public void testEmail() {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");
		BeanContext.init("com.ajaxjs.app");
		BeanContext.injectBeans();

		Mail mail = new Mail();
		mail.setFrom("admin@bgdiving.com");
		mail.setTo("frank@ajaxjs.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");

		ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);
		assertNotNull(services);

		assertTrue(services.sendEmail(mail));
	}
}
