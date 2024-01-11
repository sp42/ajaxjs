package com.ajaxjs.net;

import com.ajaxjs.net.http.Head;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestHead {
	@Test
	public void testRedirect() {
		String redirect = Head.get302redirect("https://baidu.com");
		assertTrue("http://www.baidu.com/".equals(redirect));
	}

	@Test
	public void testIs404() {
		assertTrue(Head.is404("https://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokiddle.png"));
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		assertTrue(!Head.is404(url));
	}

	@Test
	public void testGetFileSize() {
		String url = "https://cdn2.jianshu.io/assets/web/nav-logo-4c7bbafe27adc892f3046e6978459bac.png";
		long size = Head.getFileSize(url);
		assertEquals(1500L, size);
	}
}
