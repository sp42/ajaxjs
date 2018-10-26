package test.com.ajaxjs.util;

import static com.ajaxjs.util.CommonUtil.containsIgnoreCase;
import static com.ajaxjs.util.CommonUtil.isEmptyString;
import static com.ajaxjs.util.CommonUtil.regMatch;
import static com.ajaxjs.util.CommonUtil.repeatStr;
import static com.ajaxjs.util.CommonUtil.split;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestCommonUtil {
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
