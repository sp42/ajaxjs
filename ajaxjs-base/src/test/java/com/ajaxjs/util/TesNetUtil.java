package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesNetUtil {
	String url = "https://www.baidu.com/";
	
	@Test
	public void testGet() {
		String html = NetUtil.simpleGET(url);
		assertNotNull(html);
		String html2 = NetUtil.get(url);
		assertNotNull(html2);
		assertEquals(html, html2);
	}
	
	@Test
	public void testRedirect() {
		String redirect = NetUtil.get302redirect("https://baidu.com");
		assertTrue("http://www.baidu.com/".equals(redirect));
	}
	
	@Test
	public void testIs404() {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		assertTrue(!NetUtil.is404(url));
		assertTrue(NetUtil.is404("http://www.qq.com/54543"));
	}
	
	@Test
	public void testGetFileSize() {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		long size = NetUtil.getFileSize(url);
		long size2 = NetUtil.getRemoteSize(url);
//		assertEquals(4102L, size);
		assertEquals(4102L, size2);
	}
}
