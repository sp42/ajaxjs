package com.ajaxjs.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.ajaxjs.net.http.NetUtil;

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
	}

	@Test
	public void testGetFileSize() {
		String url = "https://cdn2.jianshu.io/assets/web/nav-logo-4c7bbafe27adc892f3046e6978459bac.png";
		long size = NetUtil.getFileSize(url);
		long size2 = NetUtil.getRemoteSize(url);
		assertEquals(1500L, size);
		assertEquals(1500L, size2);
	}

	@Test
	public void testForce_GZipGet() {
		String html = NetUtil.get(url, true);

		assertNotNull(html);
	}

	@Test
	public void testDownload2disk() throws IOException {
		assertNotNull(NetUtil.download(url, "c:/temp"));
		String url = "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg";
		assertNotNull(NetUtil.download(url, "c:/temp"));
	}

	@Test
	public void testPost() {
		String result = NetUtil.post("http://localhost:8080/pachong/post.jsp", new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", "bar");
			}
		});
		assertNotNull(result);
	}
}
