package com.ajaxjs.test.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajaxjs.util.map.IValue;
import com.ajaxjs.util.map.MapHelper;
import com.ajaxjs.util.map.Value;

public class TestMapHelper {
	@Test
	public void testIsEmptyString() {
		MapHelper<String, IValue> map = new MapHelper<String, IValue>();
		map.put("foo", new Value("string"));
		map.put("foo", new Value("123"));

//		System.out.println(map.get("foo").getValueOf().getClass().getName());
		assertNotNull(map.get("foo"));
		assertEquals("java.lang.Integer", map.get("foo").getValueOf().getClass().getName());
	}
	
	
	@Test
	public void testWrite() {
	 
	}
}
