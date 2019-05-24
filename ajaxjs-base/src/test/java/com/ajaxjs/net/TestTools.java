package com.ajaxjs.net;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

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

	@Test
	public void testGetIpLocation() throws IOException {
		Map<String, Object> ip = Tools.getIpLocation("119.33.28.152");
		assertNotNull(ip);
		System.out.println(ip);
	}
}
