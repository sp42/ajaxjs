package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.ajaxjs.util.cache.ExpireCache;

public class TestCache {
	@Test
	public void testExpire() throws InterruptedException {
		ExpireCache.CACHE.put("foo", "bar", 3);

		Thread.sleep(1000);
		assertEquals("bar", ExpireCache.CACHE.get("foo"));

		Thread.sleep(2010);
		assertNull(ExpireCache.CACHE.get("foo"));

		ExpireCache.CACHE.put("bar", "foo", 2, () -> "foo-2");

		Thread.sleep(1000);
		assertEquals("foo", ExpireCache.CACHE.get("bar"));

		Thread.sleep(2010);
		assertEquals("foo-2", ExpireCache.CACHE.get("bar"));
	}
}
