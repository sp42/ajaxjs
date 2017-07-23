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
	public void testStringJoin() {
		assertEquals(stringJoin(new String[] { "a", "b", "c" }, ","), "a,b,c");
		assertEquals(stringJoin(new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("a");
				add("b");
				add("c");
			}
		}, ","), "a,b,c");
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

	static String str = "中国";

	@Test
	public void testUrlDecode() {
		assertEquals(urlDecode(urlEncode(str)), str);
	}

	@Test
	public void testMisc() {
		assertEquals(base64Decode(base64Encode(str)), str);
		assertEquals(md5("123123"), "4297F44B13955235245B2497399D7A93");
	}
}
