package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import static com.ajaxjs.util.Encode.*;

public class TestEncode {
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
	public void testUrlDecode() {
		assertEquals(urlDecode(urlEncode(str)), str);
	}

	@Test
	public void testMisc() {
		assertEquals(base64Decode(base64Encode(str)), str);
	}

	@Test
	public void testMD5() {
		assertEquals("4297F44B13955235245B2497399D7A93", md5("123123"));
	}

	@Test
	public void testSHA1() {
		assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", getSHA1("abc"));
	}

	@Test
	public void testSHA256() {
		assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", getSHA256("abc"));
	}
}
