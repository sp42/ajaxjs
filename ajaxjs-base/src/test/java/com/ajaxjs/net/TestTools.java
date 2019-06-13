package com.ajaxjs.net;

import static org.junit.Assert.assertNotNull;

import com.ajaxjs.net.http.Tools;

public class TestTools {
//	@Test
	public void testGetIp() {
		String ip = Tools.getLocalIp();
		assertNotNull(ip);
		System.out.println(ip);

		ip = Tools.getLocalIp2();
		assertNotNull(ip);
		System.out.println(ip);
	}
}
