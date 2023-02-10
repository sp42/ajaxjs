package com.ajaxjs.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ajaxjs.web.security.Filter;

public class TestSecurity {
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
