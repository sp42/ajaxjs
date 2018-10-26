package test.com.ajaxjs.util;

import static org.junit.Assert.*;
import static com.ajaxjs.util.StringUtil.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestStringUtil {
	@Test
	public void testIsEmptyString() {
		assertTrue(isEmptyString(""));
		assertTrue(isEmptyString(" "));
		assertTrue(isEmptyString(null));
	}

	@Test
	public void testSplit() {
		assertEquals(2, split("a,b").length);
		assertEquals(3, split("a/b/c").length);
		assertEquals(4, split("a\\b\\c\\d").length);
		assertEquals(5, split("a|b|c|d|e").length);
		assertEquals(5, split("a;b;c;d;e").length);
	}
	
	@Test
	public void testStringJoin() {
		assertEquals("a, b", stringJoin(new String[] {"a", "b"}));
		assertEquals("a,b,c", stringJoin(new String[] { "a", "b", "c" }, ","));
		assertEquals("a,b,c", stringJoin(new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("a");
				add("b");
				add("c");
			}
		}, ","));
	}

	@Test
	public void testRepeatStr() {
		assertEquals(repeatStr("Hi", ",", 3), "Hi,Hi,Hi");
	}

	@Test
	public void testContainsIgnoreCase() {
		assertTrue(containsIgnoreCase("abc", "A"));
	}

	@Test
	public void testRegMatch() {
		assertEquals(regMatch("^a", "abc"), "a");// 匹配结果，只有匹配第一个
		assertEquals(regMatch("^a", "abc", 0), "a");// 可指定分组
		assertEquals(regMatch("^a(b)", "abc", 1), "b");
	}


}
