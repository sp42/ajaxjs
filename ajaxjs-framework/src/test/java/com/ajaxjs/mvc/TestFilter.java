package com.ajaxjs.mvc;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.ajaxjs.web.mock.BaseControllerTest;
import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mock.MockResponse;
import com.ajaxjs.web.mvc.controller.MvcDispatcher;

public class TestFilter extends BaseControllerTest {

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
		
		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);
		
		assertNotNull(writer.toString());
	}

	@Test
	public void testAesFilter()throws IOException, ServletException {
		request = MockRequest.mockRequest("/ajaxjs-web", "/filter/api");
		when(request.getMethod()).thenReturn("GET");
//		when(request.getParameter("token")).thenReturn(TokenService.getTimeStampToken(ConfigService.getValueAsString("System.api.AES_Key")));

		MvcDispatcher.dispatcher.apply(request, response);
		chain.doFilter(request, response);
		
		assertNotNull(writer.toString());
	}
}
