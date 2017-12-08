package test.com.ajaxjs.security;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.ajaxjs.security.SecurityFilter;


public class TestFilter {
	@Test
	public void testReferer() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		FilterChain chain = mock(FilterChain.class);
		when(request.getHeader("referer")).thenReturn("www.qq.com");
		when(request.getServerName()).thenReturn("www.qq.com");
		when(request.getMethod()).thenReturn("POST");
		when(request.getParameter("foo")).thenReturn("bar");
		
//		assertEquals(request.getHeader("referer"), request.getServerName());
		
		try {
			new SecurityFilter().doFilter(request, response, chain);
			
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
		//SecurityMgr.aop(request);
	}
}
