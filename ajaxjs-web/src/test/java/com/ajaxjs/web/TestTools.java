package com.ajaxjs.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class TestTools {

	@Test
	public void testGetIpLocation() throws IOException {
		Map<String, Object> ip = IpBlock.getIpLocation("35.220.250.107");
		assertNotNull(ip);
		System.out.println(ip);
		
		assertTrue(!IpBlock.isChinaMainlandIp("35.220.250.107"));
	}
}
