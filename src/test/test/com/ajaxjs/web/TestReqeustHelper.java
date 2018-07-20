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
}
