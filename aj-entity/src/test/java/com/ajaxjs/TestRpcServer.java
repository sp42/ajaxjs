package com.ajaxjs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestRpcServer {

	@Test
	public void test() throws Exception {
		System.out.println("Dubbo Server Running!");
//		Thread.sleep(100000);
		System.in.read(); // #4
	}
}
