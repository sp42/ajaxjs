package test.com.ajaxjs.net;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajaxjs.http.Client;

public class TestClient {
	String url = "https://www.baidu.com/";
	
//	@Test
	public void testIs404() {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		assertTrue(!Client.is404(url));
		assertTrue(Client.is404("http://www.qq.com/54543"));
	}
	
//	@Test
	public void testRedirect() {
		String redirect = Client.get302redirect("https://baidu.com");
		System.out.println(redirect);
		assertTrue(url.equals(redirect));

	}
	
//	@Test
	public void testGetFileSize() {
		String url = "http://c.csdnimg.cn/jifen/images/xunzhang/xunzhang/bokezhuanjiamiddle.png";
		long size = Client.getFileSize(url);
		assertEquals(size, 4102L);
	}
	
//	@Test
	public void testGet() {
		String html;
		html = Client.GET(url);
		System.out.println(html);
		assertNotNull(html);
		
		html = Client.simpleGET(url);
		System.out.println(html);
		assertNotNull(html);
	}
	
//	@Test
	public void testGZipGet() {
		String html;
		html = Client.GET_Gzip("http://u.3gtv.net");
		System.out.println(html);
		assertNotNull(html);
	}
	
	// B 站强制 Gzip 返回，無論请求是否带有 GZIP
	@Test
	public void testForce_GZipGet() {
		String url = "http://www.bilibili.com/video/av5178498/";
		String html = Client.GET_Gzip(url);
		System.out.println(html);
		assertNotNull(html);
	}
}
