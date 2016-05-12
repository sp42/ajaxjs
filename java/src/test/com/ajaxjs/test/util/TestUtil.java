package com.ajaxjs.test.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.ajaxjs.util.*;

public class TestUtil {
	@Test
	public void testIsNotNull() {
		// Array
		assertTrue(Util.isNotNull(new Object[] { 1, 2 }));
		assertTrue(Util.isNotNull(new String[] { "1" }));
		assertTrue(!Util.isNotNull(new Object[] {}));

		Object[] arr = null;
		String[] strs = null;
		assertTrue(!Util.isNotNull(arr));
		assertTrue(!Util.isNotNull(strs));

		// List
		assertTrue(Util.isNotNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
			}
		}));

		assertTrue(!Util.isNotNull(new ArrayList<String>()));

		assertTrue(Util.isNotNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(null);
			}
		}));

		// Map
		assertTrue(Util.isNotNull(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", null);
			}
		}));
		assertTrue(!Util.isNotNull(new HashMap<String, Object>()));
	}

	@Test
	public void testWrite() {

		// assertNotNull(content);
	}
}
