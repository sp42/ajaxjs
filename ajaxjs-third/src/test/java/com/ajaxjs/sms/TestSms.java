package com.ajaxjs.sms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.ajaxjs.app.ThirdPartyService;
import com.ajaxjs.framework.TestHelper;
import com.ajaxjs.util.ioc.ComponentMgr;

public class TestSms {
//	@Test
	public void testAli() throws IOException {
		TestHelper.initAll();

		SmsMessage message = new SmsMessage();
		message.setPhoneNo("13711228150");
		message.setTemplateCode("SMS_138067918");
		message.setSignName("我是zyjf");
		message.setTemplateParam(String.format("{\"code\":\"%s\"}", "88888"));

		assertTrue(AliyunSMS.send(message));
	}

	@Test
	public void byService() {
		TestHelper.initAll();

		ThirdPartyService services = ComponentMgr.get(ThirdPartyService.class);
		assertNotNull(services);

		assertTrue(services.sendSms("13711228150", "SMS_138067918", String.format("{\"code\":\"%s\"}", 3444)));
	}
}
