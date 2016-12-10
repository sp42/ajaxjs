package test.com.ajaxjs.net;

import static org.junit.Assert.*;
import org.junit.Test;

import com.ajaxjs.http.Client;
import com.ajaxjs.net.IP;


public class TestIp {
	@Test 
	public void testLanIp(){
		String ip = IP.getLocalIp();
		System.out.println(ip);
		assertTrue(ip.startsWith("192.168"));
	}
	
	@Test 
	public void testServer(){
		IP.httpServer();
		String url = "http://" + IP.getLocalIp() + ":8081/myApp/";
		System.out.println(url);
		String result = Client.GET(url);
		System.out.println(result);
		assertTrue(result.trim().equals("ok"));
	}
}