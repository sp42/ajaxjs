package com.ajaxjs.util;

import static com.ajaxjs.util.StrUtil.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStrUtil {
	static String str = "中国";

	@Test
	public void testByte2String() {
		assertEquals("abc", byte2String(new byte[] { 97, 98, 99 }));
		assertEquals("abc", byte2String("abc"));
		assertEquals("中国", byte2String(str));
	}

	@Test
	public void testUrlChinese() {
//		assertEquals("中国", urlChinese("%E4%B8%AD%E5%9B%BD"));
	}

	@Test
	public void testMisc() {
		assertEquals(base64Decode(base64Encode(str)), str);
	}

	@Test
	public void testRegMatch() {
		assertEquals(regMatch("^a", "abc"), "a");// 匹配结果，只有匹配第一个
		assertEquals(regMatch("^a", "abc", 0), "a");// 可指定分组
		assertEquals(regMatch("^a(b)", "abc", 1), "b");
	}

	@Test
	public void testConcatUrl() {
		assertEquals("sdsd/aaa/bbb/sds", concatUrl("sdsd/aaa/", "/bbb/sds"));
		assertEquals("sdsd/aaa/bbb/sds", concatUrl("sdsd/aaa", "bbb/sds"));
		assertEquals("sdsd/aaa/bbb/sds", concatUrl("sdsd/aaa/", "bbb/sds"));
		assertEquals("sdsd/aaa/bbb/sds", concatUrl("sdsd/aaa", "/bbb/sds"));
	}

	@Test
	public void testLeftPad() {
		assertEquals("@@@@@12345", leftPad("12345", 10, "@"));
	}
}
