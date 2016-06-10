package test.com.ajaxjs.net;

import static org.junit.Assert.*;


import org.junit.*;

import com.ajaxjs.net.IP;

 
public class TestUtil{
	@BeforeClass
	public static void init(){}
	
	@Before
	public void setUp(){
		
	}
	 
	@Test
	public void testIp() {
		String foo;
		System.out.println("本机IP：" + IP.getLocalHostIP()); 
    	System.out.println("本机IP：" + IP.getReal_LAN_IP());
    	System.out.println("本地主机名字为：" + IP.getLocalHostName()); 
    	
    	System.out.println("本机所有 ip");
		for(String localIP : IP.getAllLocalHostIP()){
			foo = localIP;
			assertNotNull(foo);
			System.out.println(localIP);
		}
		
		foo = IP.getIpByHostName("www.baidu.com");
		System.out.println("baidu IP: " + foo);
		assertNotNull(foo);
		
	}
	
//	@Test
//	public void testWeather(){
//		String foo = Weather.getWeather("广州");
//		assertNotNull(foo);
//	}
}