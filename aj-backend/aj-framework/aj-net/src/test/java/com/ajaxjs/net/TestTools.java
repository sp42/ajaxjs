package com.ajaxjs.net;

import com.ajaxjs.net.tools.ExternalIPUtil;
import com.ajaxjs.net.tools.Tools;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestTools {
	@Test
	public void testGetDomainName() {
		assertEquals("tomcat8080.com", Tools.getDomainName("http://tomcat8080.com/abc=def"));
	}

	@Test
	public void testExternalIPUtil() {
		String ip = ExternalIPUtil.get();
		assertNotNull(ip);
	}
}
