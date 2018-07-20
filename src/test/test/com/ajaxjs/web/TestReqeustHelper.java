package test.com.ajaxjs.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.web.RequestHelper;
import com.ajaxjs.web.test.MockRequest;

public class TestReqeustHelper {
	@Test
	public void testGetBasePath() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar");
		when(request.getScheme()).thenReturn("https");
		when(request.getServerName()).thenReturn("www.ibm.com");
		when(request.getServerPort()).thenReturn(8081);

		assertEquals("https://www.ibm.com:8081/foo", new RequestHelper(request).getBasePath());
	}

	@Test
	public void testGetIp() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar");
		when(request.getRemoteAddr()).thenReturn("10.0.0.1");
		assertEquals("10.0.0.1", new RequestHelper(request).getIp());
	}

	@Test
	public void testGetIpByHostName() {
		assertEquals("125.39.240.113", RequestHelper.getIpByHostName("qq.com"));
	}

	@Test
	public void testPing() {
		assertTrue(RequestHelper.ping("127.0.0.1"));
	}
}
