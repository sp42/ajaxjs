package com.ajaxjs.net.http;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ajaxjs.net.http.Client;

public class TestGet {
	String url = "https://www.baidu.com/";
	
	@Test
	public void testIs404() {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		assertTrue(!Client.is404(url));
		assertTrue(Client.is404("http://www.qq.com/54543"));
	}
	
	
	@Test
	public void testGetFileSize() {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		long size = Client.getFileSize(url);
		assertEquals(size, 4102L);
	}
	@Test
	public void testRedirect() {
		String redirect = Client.get302redirect("https://baidu.com");
		assertTrue(url.equals(redirect));
	}
	
	@Test
	public void testGet() {
		String html;
		html = Client.GET(url);
		assertNotNull(html);
		
		html = Client.simpleGET(url);
		assertNotNull(html);
	}
	
	@Test
	public void testGZipGet() {
		String html;
		html = Client.GET_Gzip("http://u.3gtv.net");
		assertNotNull(html);
	}
	
	@Test
	public void testForce_GZipGet() {// B 站强制 Gzip 返回，無論请求是否带有 GZIP
		String url = "http://www.bilibili.com/video/av5178498/";
		String html = Client.GET_Gzip(url);
		assertNotNull(html);
	}
	
	@Test
	public void testDownload2disk() throws IOException {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
//		Get.download2disk(url, "c:/temp/dd.png");
		
		
//		String saveTo = "c:/temp/js.js";
//		Get.download2disk("http://bdimg.share.baidu.com/static/api/js/base/tangram.js?v=37768233.js", saveTo);
//		assertNotNull(FileUtil.readFileAsText(saveTo));
	}
}
