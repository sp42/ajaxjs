package com.ajaxjs.keyvalue;

import static com.ajaxjs.keyvalue.MappingValue.TypeConvert;
import static com.ajaxjs.keyvalue.MappingValue.objectCast;
import static com.ajaxjs.keyvalue.MappingValue.toBoolean;
import static com.ajaxjs.keyvalue.MappingValue.toJavaValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class TestMappingValue {
	@Test
	public void testTypeConvert() {
		assertTrue(12 == TypeConvert(12, Integer.class));
		assertTrue(TypeConvert(true, Boolean.class));
	}

	@Test
	public void testToJavaValue() {
		assertTrue((int) toJavaValue("123") == 123);
		assertTrue((boolean) toJavaValue("true") == true);
		assertTrue((boolean) toJavaValue("false") == false);
		assertTrue(toJavaValue("null") == null);
	}

	@Test
	public void testToBoolean() {
		assertTrue(toBoolean(true));
		assertTrue(toBoolean("true"));
		assertTrue(toBoolean("True"));

		assertTrue(!toBoolean("fAlse"));
		assertTrue(!toBoolean("null"));

		assertTrue(toBoolean("on"));
		assertTrue(toBoolean("yes"));
		assertTrue(toBoolean(1));
		assertTrue(toBoolean("1"));
	}

	@Test
	public void testObjectCast() {
		assertTrue((boolean) objectCast("true", boolean.class));
		assertEquals(1000, (int) objectCast("1000", int.class));
		assertEquals(1L, (long) objectCast("1", long.class));
		assertEquals("foo", (String) objectCast("foo", String.class));

		int[] arr = (int[]) objectCast("1,2,3", int[].class);
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, arr));
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		arr = (int[]) objectCast(list, int[].class);
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, arr));

		String[] arr2 = (String[]) objectCast("1,2,3", String[].class);
		assertTrue(Arrays.equals(new String[] { "1", "2", "3" }, arr2));
		
		List<String> list2 = new ArrayList<>();
		list2.add("1");
		list2.add("2");
		list2.add("3");
		
		arr2 = (String[]) objectCast(list2, String[].class);
		assertTrue(Arrays.equals(new String[] { "1", "2", "3" }, arr2));
		
		assertEquals("Tue Feb 20 00:00:00 GMT+08:00 2018", ((Date) objectCast("2018-2-20", Date.class)).toString());
	}
	

}
