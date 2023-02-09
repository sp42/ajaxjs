package com.ajaxjs.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ajaxjs.auth.controller.DataServiceApiController;
import com.ajaxjs.base.ISendSMS;
import com.ajaxjs.user.controller.ResetPasswordController;
import com.ajaxjs.user.service.ResetPasswordService;
import com.ajaxjs.user.service.SendEmail;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestResetPassword {
	@Autowired
	DataServiceApiController apiController;

	@Autowired
	ResetPasswordController resetPasswordController;

	@Before
	public void init() {
		apiController.initCache();
	}

	@Autowired
	ResetPasswordService resetPasswordService;

	@Autowired
	SendEmail sendEmail;

//	@Test
	public void testSendEmail() {
		assertTrue(sendEmail.send("sp42@qq.com", "test", "dsds<br>我希望可以<b>跟你做</b>朋友34354344"));
	}

	@Autowired
	ISendSMS sendSMS;

//	@Test
	public void testSendSms() {
		assertTrue(sendSMS.send("13711228150", "343424"));
	}

//	@Test
	public void makeEmailToken() {
		String token = resetPasswordService.makeEmailToken("sp42@qq.com", 1);
		System.out.println(token);
		assertNotNull(token);

		assertTrue(resetPasswordService.checkEmailToken(token, "sp42@qq.com"));
	}

//	@Test
	public void testTpl() {
		// 设置文字模板，其中 #{} 表示表达式的起止，#user 是表达式字符串，表示引用一个变量。
		String template = "ddd#{#user}，早上好";

		Map<String, String> map = new HashMap<>(16);
		map.put("user", "黎明");

		String result = SendEmail.simpleTemplate(template, map);
		assertEquals("ddd黎明，早上好", result);

		map = new HashMap<>(16);
		map.put("username", "黎明");
		map.put("timeout", "12");

		result = sendEmail.getEmailContent(map);
		System.out.println(result);
	}

	HttpServletRequest req = mock(HttpServletRequest.class);

//	@Test
	public void sendRestPswEmail() {
		assertNotNull(resetPasswordController.sendRestEmail("sp42@qq.com", 1));
	}

	@Test
	public void verifyTokenUpdatePsw() {
		String token = "9488ffe30b5bba032d52a9f93de2e2db6dcc0c047n/99+sXUgrWIC1rhgNTYA==";
		resetPasswordController.verifyTokenUpdatePsw(token, "a123123abc", "sp42@qq.com", 1);
	}
}
