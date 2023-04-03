package com.ajaxjs.security;

import com.ajaxjs.web.security.ListCheck;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestSecurity {

	@Autowired
	private ListCheck c;

//	@Test
//	public void testListCheck() {
//		assertTrue(c.isInWhiteList("test_abc"));
//		assertTrue(!c.isInWhiteList("test_abc22"));
//		assertTrue(!c.isInBlackList("test_abc22"));
//	}
//
//	@Autowired
//	private Xss xss;
//
//	@Test
//	public void testXss() {
//		String script = "Foo <script>alert(3)</script>";
//		assertEquals("Foo ", xss.clean(script));
//		assertEquals("&lt;script&gt;alert(3)&lt;/script&gt;", xss.clean(script, Handle.TYPE_ESCAPSE));
//	}
//
//	@Autowired
//	private SqlInject sqlInject;
//
//	@Test
//	public void testSqlInject() {
//		String str = "?id=1&SELECT * FROM user";
//		assertEquals("?id=1& * FROM user", sqlInject.clean(str));
//	}
}
