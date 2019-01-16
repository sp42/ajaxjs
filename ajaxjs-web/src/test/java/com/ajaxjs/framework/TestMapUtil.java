package com.ajaxjs.framework;

import static com.ajaxjs.framework.MapUtil.to;
import static com.ajaxjs.framework.MapUtil.toJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.keyvalue.MappingValue;

public class TestMapUtil {
	static Map<String, String[]> m1 = new HashMap<String, String[]>() {
		private static final long serialVersionUID = 1L;
		{
			put("foo", new String[] { "a", "b" });
			put("bar", new String[] { "1", "c", "2" });
		}
	};

	static Map<String, String> m2 = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("foo", "a");
			put("bar", "1");
		}
	};

	@Test
	public void test() {
		Map<String, Object> map = to(m1, null);
		System.out.println(map);

		map = to(m1, arr -> arr[0]);
		System.out.println(map.get("bar").getClass());

		map = to(m1, arr -> MappingValue.toJavaValue(arr[0]));
		System.out.println(map.get("bar").getClass());
	}

	@Test
	public void testStringifySimpleObject() {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public Object NULL = null;
			@SuppressWarnings("unused")
			public String str = null;
			@SuppressWarnings("unused")
			public Boolean isOk = false;
			@SuppressWarnings("unused")
			public Integer n0 = 0;
			@SuppressWarnings("unused")
			public Number n1 = 111;
			@SuppressWarnings("unused")
			public int n2 = 222;
			// @SuppressWarnings("unused")
			// public Date date = new Date();
			@SuppressWarnings("unused")
			public String msg = "Hello world";
			@SuppressWarnings("unused")
			public Object[] arr = new Object[] { 1, "2", null };
		};

		String jsonStr = toJson(obj);
		// 输出 {"foo":"11","bar":"2222"}
		assertNotNull(jsonStr);
		assertEquals("{\"NULL\":null,\"str\":null,\"isOk\":false,\"n0\":0,\"n1\":111,\"n2\":222,\"msg\":\"Hello world\",\"arr\":[1, \"2\", null]}", jsonStr);
	}

	@Test
	public void testStringifyMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "11");
		map.put("bar", 2222);
		map.put("bar2", null);
		map.put("bar3", true);

		String jsonStr = toJson(map);
		assertEquals("{\"bar2\":null,\"bar\":2222,\"foo\":\"11\",\"bar3\":true}", jsonStr);
	}
	
	@Test
	public void testObj2jsonVaule() {
		assertEquals(null, 		toJson(null));
		assertEquals("1.0", 	toJson(1D));
		assertEquals("true", 	toJson(true));
		assertEquals("1", 		toJson(1));
		assertEquals("1", 		toJson(1L));
		assertEquals("\"2018-02-20 00:00:00\"", 		toJson((Date) MappingValue.objectCast("2018-2-20", Date.class)));
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		assertEquals("[1, 2, 3]", 		toJson(list));
		assertEquals("[1, 2, 3]", 		toJson(new Integer[] { 1, 2, 3 }));
		assertEquals("[1, 2, 3]", 		toJson(new int[] { 1, 2, 3 }));
		
		List<String> list2 = new ArrayList<>();
		list2.add("1");
		list2.add("2");
		list2.add("3");
		assertEquals("[\"1\", \"2\", \"3\"]", 		toJson(list2));
		assertEquals("[\"1\", \"2\", \"3\"]", 		toJson(new String[] { "1", "2", "3" }));
		
		Map<String, Object> map = new HashMap<>();
		assertEquals("{}", 		toJson(map));
		map.put("foo", "bar");
		assertEquals("{\"foo\":\"bar\"}", 		toJson(map));
		map.put("bar", 1);
		assertEquals("{\"bar\":1,\"foo\":\"bar\"}", 		toJson(map));
		
		List<Map<String, Object>> list3 = new ArrayList<>();
		assertEquals("[]", 		toJson(list3));
		list3.add(map);
		list3.add(map);
		assertEquals("[{\"bar\":1,\"foo\":\"bar\"}, {\"bar\":1,\"foo\":\"bar\"}]", 		toJson(list3));
	}
}
