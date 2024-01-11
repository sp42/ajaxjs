package com.ajaxjs.util;

import static com.ajaxjs.util.Digest.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDigest {
	@Test
	public void testMD5() {
		assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5(""));
		assertEquals("4297F44B13955235245B2497399D7A93", md5("123123").toUpperCase());
	}

	@Test
	public void testSHA1() {
		assertEquals("40bd001563085fc35165329ea1ff5c5ecbdbbeef", getSHA1("123"));
		assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", getSHA1("abc"));
	}

	@Test
	public void testSHA256() {
		assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", getSHA256("abc"));
	}

	@Test
	public void testHMAC() {
		String key = getHMAC_Key();
		String word = "123";
		System.out.println(getHMAC(word, key));
//		assertEquals("a0dcb48689ace6690f2d8a2f7cdddc2f", getHMAC(word, key));
	}
}
