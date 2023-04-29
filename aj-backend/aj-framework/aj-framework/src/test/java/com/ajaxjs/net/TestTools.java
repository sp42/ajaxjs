package com.ajaxjs.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.net.tools.ExternalIPUtil;
import com.ajaxjs.net.tools.Tools;

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
