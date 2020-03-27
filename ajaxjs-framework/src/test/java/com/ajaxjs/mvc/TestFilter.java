package com.ajaxjs.mvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.framework.filter.AesFilter;
import com.ajaxjs.web.captcha.CaptchaController;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;


public class TestFilter extends BaseTest {

	// 单测技巧，每个 url 对应一个 request、一个 response
	@Before
	public void load() throws ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/filter");
		response = mock(HttpServletResponse.class);
		writer = MockResponse.writerFactory(response);
	}

	@Test
	public void testFilter() throws IOException, ServletException {
		when(request.getMethod()).thenReturn("GET");
		dispatcher.doFilter(request, response, chain);
		assertNotNull(writer.toString());
	}

	@Test
	public void testCaptchaFilter() throws IOException, ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/filter/captcha");

		Map<String, Object> map = new HashMap<>();
		map.put(CaptchaController.CAPTCHA_CODE, "123123");
		MockRequest.mockSession(request, map);

		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter(CaptchaController.CAPTCHA_CODE)).thenReturn("12313");
		dispatcher.doFilter(request, response, chain);

		assertEquals("{\"isOk\": false, \"msg\" : \"验证码不正确\"}", writer.toString());
		//		assertNotNull(writer.toString());
	}

	@Test
	public void testAesFilter()throws IOException, ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/filter/api");
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("token")).thenReturn(AesFilter.getTimeStampToken(AesFilter.aesKey));

		dispatcher.doFilter(request, response, chain);
		assertNotNull(writer.toString());
	}
}
