package com.ajaxjs.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;

import org.junit.Test;

import com.ajaxjs.net.http.Tools;

public class TestServletHelper {
	@Test
	public void testInitFilterConfig2Map() {
		FilterConfig config = mock(FilterConfig.class);
		// 模拟注解
		Vector<String> v = new Vector<>();
		v.addElement("urlPatterns");
		when(config.getInitParameterNames()).thenReturn(v.elements());
		when(config.getInitParameter("urlPatterns")).thenReturn("/service/*");

		Map<String, String> map = ServletHelper.initFilterConfig2Map(config);
		assertEquals("/service/*", map.get("urlPatterns"));
	}

	@Test
	public void testInitServletConfig2Map() {
		ServletConfig config = mock(ServletConfig.class);
		// 模拟注解
		Vector<String> v = new Vector<>();
		v.addElement("urlPatterns");
		when(config.getInitParameterNames()).thenReturn(v.elements());
		when(config.getInitParameter("urlPatterns")).thenReturn("/service/*");

		Map<String, String> map = ServletHelper.initServletConfig2Map(config);
		assertEquals("/service/*", map.get("urlPatterns"));
	}

	@Test
	public void testGetIpLocation() throws IOException {
		Map<String, Object> ip = Tools.getIpLocation("35.220.250.107");
		assertNotNull(ip);

		assertTrue(!ServletHelper.isChinaMainlandIp("35.220.250.107"));
	}
}
