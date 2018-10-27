package test.com.ajaxjs.util;

import static com.ajaxjs.util.CommonUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.ajaxjs.util.CommonUtil;

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

	@Test
	public void testNow() {
		assertNotNull(now()); // 返回当前时间的 YYYY-MM-dd HH:MM:ss 字符串类型
		assertNotNull(now(commonDateFormat)); // 返回当前时间，并对当前时间进行格式化
	}

	@Test
	public void testFormat() {
		Date date = CommonUtil.Objet2Date("2017-07-25 11:16:09");
		
		assertEquals("Tue Jul 25 11:16:09 GMT+08:00 2017", date.toString());
		assertEquals("2017-07-25 11:16", CommonUtil.formatDateShorter(date));

		assertEquals(date.getTime(), CommonUtil.Objet2Date(date).getTime());
		assertEquals(date.getTime(), CommonUtil.Objet2Date(date.getTime()).getTime());
		assertEquals(date.getTime(), CommonUtil.Objet2Date("2017-07-25 11:16:09").getTime()); // 转换字符串类型的日期到 Date 类型
	}
}
