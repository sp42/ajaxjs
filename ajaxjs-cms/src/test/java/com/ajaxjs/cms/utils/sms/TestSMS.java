package com.ajaxjs.cms.utils.sms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.ajaxjs.cms.utils.sms.Aliyun;
import com.ajaxjs.cms.utils.sms.Message;
import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.util.io.resource.ScanClass;

public class TestSMS {
//	@Test
	public void testSend() {
		Message message = new Message();
		message.setPhoneNo("13711228150");
		message.setSignName("我是zyjf");
		message.setTemplateCode("SMS_128495105");
		message.setTemplateParam("{\"code\":\"123\"}");

		SMS sms = new Aliyun();
		assertTrue(sms.send(message));
	}

	@Test
	public void testSendIoc() {
		Message message = new Message();
		message.setPhoneNo("13711228150");
		message.setSignName("我是zyjf");
		message.setTemplateCode("SMS_128495105");
		message.setTemplateParam("{\"code\":\"123\"}");

		Set<Class<Object>> classes = ScanClass.scanClass("com.ajaxjs.cms.util.sms");
		classes.addAll(ScanClass.scanClass("com.ajaxjs.cms.util.sms"));
		
		BeanContext.init(classes);

		Dummy sms = (Dummy) BeanContext.getBean("Dummy");
		assertNotNull(sms.getSms());
//		assertTrue(sms.getSms().send(message));
	}
}
