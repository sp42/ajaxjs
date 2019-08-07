package com.ajaxjs.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.ajaxjs.net.http.NetUtil;

public class TesNetUtil {
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
		assertEquals(1500L, size);
	}


	@Test
	public void testDownload2disk() throws IOException {
		assertNotNull(NetUtil.download("https://www.baidu.com/", "c:/temp"));
		String url = "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg";
		assertNotNull(NetUtil.download(url, "c:/temp"));
	}
}
