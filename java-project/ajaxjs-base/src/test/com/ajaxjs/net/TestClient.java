package test.com.ajaxjs.net;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajaxjs.http.Client;

public class TestClient {
	@Test
	public void testGet() {
		String url = "https://baidu.com";
		String html = Client.GET(url);
//		System.out.println(html);
		assertNotNull(html);
		
		html = Client.simpleGET(url);
		System.out.println(html);
		assertNotNull(html);
	}
}
