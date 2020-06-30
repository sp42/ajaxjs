package com.ajaxjs.mail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ajaxjs.app.ThirdPartyService;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.framework.config.TestHelper;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.net.mail.Mail;

public class TestEmail {

	@Test
	public void testEmail() {
		TestHelper.initAll();

		Mail mail = new Mail();
		mail.setTo("sp42@qq.com");
		mail.setSubject("你好容祯");
		mail.setHTML_body(true);
		mail.setContent("我希望可以跟你做朋友 <a target=\"_blank\" href=\"http://www.qq.com\">QQ</a> 34354344");


		ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);
		assertNotNull(services);
		assertTrue(services.sendEmail(mail));
	}
}
