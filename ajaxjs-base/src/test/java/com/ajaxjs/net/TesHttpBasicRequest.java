package com.ajaxjs.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Test;

import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.net.http.NetUtil;

public class TesHttpBasicRequest {
	String url = "https://www.baidu.com/";

	// @Test
	public void testGet() {
		String html = HttpBasicRequest.simpleGET(url), html2 = NetUtil.get(url);
		assertEquals(html, html2);// 两种方法作用相同
	}

	// @Test
	public void testForce_GZipGet() {
		String html = HttpBasicRequest.get(url, true);
		assertNotNull(html);
	}

	// @Test
	public void testPost() {
		String result = HttpBasicRequest.post("http://localhost:8080/post", new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", "bar");
			}
		});

		result = HttpBasicRequest.post("http://localhost:8080/post", "a=1&b=2&c=3");
		assertNotNull(result);
	}

	@Test
	public void combo() {
		String html = NetUtil.get("https://gitee.com/sp42_admin/ajaxjs", true, conn -> {
			HttpBasicRequest.setUserAgentDefault.accept(conn);
		});

		assertNotNull(html);
	}
}
