package com.ajaxjs.sms;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import com.ajaxjs.config.ConfigService;

public class TestSms {

	@Test
	public void testAli() throws IOException {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");

		SmsMessage message = new SmsMessage();
		message.setPhoneNo("13711228150");
		message.setTemplateCode("SMS_138067918");
		message.setSignName("我是zyjf");
		message.setTemplateParam(String.format("{\"code\":\"%s\"}", "88888"));

		assertTrue(AliyunSMS.send(message));
	}
}
