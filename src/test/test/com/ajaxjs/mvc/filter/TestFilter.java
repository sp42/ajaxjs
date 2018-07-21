package test.com.ajaxjs.mvc.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.mvc.filter.AesFilter;
import com.ajaxjs.mvc.filter.CaptchaFilter;
import com.ajaxjs.web.MockWeb;

import test.com.ajaxjs.mvc.controller.BaseTest;

public class TestFilter extends BaseTest {

	// 单测技巧，每个 url 对应一个 request、一个 response
	@Before
	public void load() throws ServletException {
		request = MockWeb.mockRequest("/ajaxjs-web", "/filter");
		response = mock(HttpServletResponse.class);
		writer = MockWeb.writerFactory(response);
	}

	@Test
	public void testFilter() throws IOException, ServletException {
		when(request.getMethod()).thenReturn("GET");
		dispatcher.doFilter(request, response, chain);
		assertNotNull(writer.toString());
	}

	@Test
	public void testCaptchaFilter() throws IOException, ServletException {
		request = MockWeb.mockRequest("/ajaxjs-web", "/filter/captcha");

		Map<String, Object> map = new HashMap<>();
		map.put(CaptchaFilter.SESSION_KEY, "123123");
		MockWeb.mockSession(request, map);

		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter(CaptchaFilter.submitedFieldName)).thenReturn("12313");
		dispatcher.doFilter(request, response, chain);

		assertEquals("{\"isOk\": false, \"msg\" : \"验证码不正确\"}", writer.toString());
		//		assertNotNull(writer.toString());
	}

	@Test
	public void testAesFilter()throws IOException, ServletException {
		request = MockWeb.mockRequest("/ajaxjs-web", "/filter/api");
		when(request.getMethod()).thenReturn("GET");
		when(request.getParameter("token")).thenReturn(AesFilter.getTimeStampToken(AesFilter.aesKey));

		dispatcher.doFilter(request, response, chain);
		assertNotNull(writer.toString());
	}
}
