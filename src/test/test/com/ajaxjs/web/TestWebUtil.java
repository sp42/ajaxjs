package test.com.ajaxjs.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.ajaxjs.web.WebUtil;
import com.ajaxjs.web.test.MockRequest;

public class TestWebUtil {
	@Test
	public void testGetBasePath() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar");
		when(request.getScheme()).thenReturn("https");
		when(request.getServerName()).thenReturn("www.ibm.com");
		when(request.getServerPort()).thenReturn(8081);

		assertEquals("https://www.ibm.com:8081/foo", WebUtil.getBasePath(request));
	}

	@Test
	public void testGetIp() {
		HttpServletRequest request = MockRequest.mockRequest("foo", "bar");
		when(request.getRemoteAddr()).thenReturn("10.0.0.1");
		assertEquals("10.0.0.1", WebUtil.getIp(request));
	}
	
	@Test
	public void testGetIpByHostName() {
		assertEquals("125.39.240.113", WebUtil.getIpByHostName("qq.com"));
	}
	
	@Test
	public void testPing() {
		assertTrue(WebUtil.ping("127.0.0.1"));
	}
}
