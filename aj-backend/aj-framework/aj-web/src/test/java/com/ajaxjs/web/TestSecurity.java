package com.ajaxjs.web;

import com.ajaxjs.web.security.Filter;
import com.ajaxjs.web.security.ListCheck;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestSecurity {

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

	@Test
	public void testFilter() {
		String str = "abc \rlk\n";
		assertEquals("abc lk", Filter.cleanCRLF(str));

		str = "?id=1&SELECT * FROM user";
		assertEquals("?id=1& * FROM user", Filter.cleanSqlInject(str));

		String script = "Foo <script>alert(3)</script>";
		assertEquals("Foo ", Filter.cleanXSS(script));
		assertEquals("&lt;script&gt;alert(3)&lt;/script&gt;", Filter.cleanXSS(script, Filter.Handle.TYPE_ESCAPSE));
	}
}
