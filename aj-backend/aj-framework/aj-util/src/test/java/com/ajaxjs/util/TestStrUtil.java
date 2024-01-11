package com.ajaxjs.util;

import static com.ajaxjs.util.StrUtil.*;
import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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

	@Test
	public void messageFormat() {
		MessageFormat.format("您好{0}，晚上好！您目前余额：{1,number,#.##}元，积分：{2}", "张三", 10.155, 10);
	}

	@Test
	public void testJoin() {
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");

		System.out.println(join(list, "&"));
	}
}
