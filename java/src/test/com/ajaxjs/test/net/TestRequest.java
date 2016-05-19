package com.ajaxjs.test.net;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.test.framework.MockRequest;
import com.ajaxjs.web.Requester;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

public class TestRequest extends MockRequest{
	@BeforeClass
	public static void init(){}
	@Before
	public void setUp(){
		
	}

	@Test
	public void testRequestHelper() throws IOException{
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("name", "bar");
		hash.put("id", "1001");
		hash.put("isGood", "true");
		
		hash.put("ChineseName", new String("张三".getBytes(), "ISO8859_1"));
		
		HttpServletRequest request = initRequest("foo");
		request = initRequest(request, hash, true);
		Requester requestHelper = new Requester(request);
		
		assertNotNull(request);
		assertNotNull(requestHelper);

		assertEquals("bar", requestHelper.getParameter("name"));
		
		IllegalArgumentException knownException = null;
		try{
			requestHelper.getParameter("你好");
		}catch(IllegalArgumentException e){
			knownException = e;
		}finally{
			// 总是有异常的
			assertNotNull("捕获已知异常", knownException);
		}
		
		requestHelper.isTypeCast = true;
		
		assertEquals(1001, (int)requestHelper.getWithTypeCast("id"));
		assertTrue((boolean)requestHelper.getWithTypeCast("isGood"));

		requestHelper.isAutoEncodeChinese = true;
		assertEquals("张三", requestHelper.get("ChineseName"));
	}
}
