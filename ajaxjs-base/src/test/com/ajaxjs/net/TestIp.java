package test.com.ajaxjs.net;

import static org.junit.Assert.*;
import org.junit.Test;

import com.ajaxjs.net.IP;
import com.ajaxjs.net.http.Client;


public class TestIp {
	@Test 
	public void testLanIp(){
		String ip = IP.getLocalIp();
		System.out.println(ip);
		assertTrue(ip.startsWith("192.168"));
	}
	 
}