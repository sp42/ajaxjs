package com.ajaxjs.web.mvc;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.web.mock.MockRequest;
import com.ajaxjs.web.mvc.controller.MvcRequest;

public class TestMvcRequest {
	@Test
	public void testGetRequestURI() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar/xxx/index.jsp");
		when(request.getAttribute("javax.servlet.forward.request_uri")).thenReturn("/abc");
		assertEquals("/abc", new MvcRequest(request).getRequestURI());
		assertEquals("abc", new MvcRequest(request).getFolder());
	}
	
	@Test
	public void testGetRoute() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar/xxx/index.jsp");
		assertEquals("/bar/xxx", new MvcRequest(request).getRoute());
	}
	@Test
	public void testGetIp() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar");
		when(request.getRemoteAddr()).thenReturn("10.0.0.1");
		assertEquals("10.0.0.1", new MvcRequest(request).getIp());
	}

	@Test
	public void testGetBasePath() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar");
		when(request.getScheme()).thenReturn("https");
		when(request.getServerName()).thenReturn("www.ibm.com");
		when(request.getServerPort()).thenReturn(8081);

		assertEquals("https://www.ibm.com:8081/foo", new MvcRequest(request).getBasePath());
	}
}
