package com.ajaxjs.web.captcha;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.Test;

import com.ajaxjs.web.mock.BaseControllerTest;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mvc.controller.MvcDispatcher;

public class TestCaptcha extends BaseControllerTest {
	@Test
	public void testCaptchaFilter() throws IOException, ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/filter/captcha");

		Map<String, Object> map = new HashMap<>();
		map.put(CaptchaController.CAPTCHA_CODE, "123123");
		MockRequest.mockSession(request, map);

		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter(CaptchaController.CAPTCHA_CODE)).thenReturn("12313");
		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);
		

		assertEquals("{\"isOk\": false, \"msg\" : \"验证码不正确\"}", writer.toString());
		//		assertNotNull(writer.toString());
	}
}
