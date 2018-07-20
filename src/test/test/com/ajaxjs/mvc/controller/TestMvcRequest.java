package test.com.ajaxjs.mvc.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.web.test.MockRequest;

public class TestMvcRequest {
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
