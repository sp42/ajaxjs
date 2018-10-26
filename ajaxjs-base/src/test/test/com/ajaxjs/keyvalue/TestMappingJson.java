package test.com.ajaxjs.keyvalue;

import static com.ajaxjs.keyvalue.MappingJson.obj2jsonVaule;
import static com.ajaxjs.keyvalue.MappingJson.stringifyMap;
import static com.ajaxjs.keyvalue.MappingJson.stringifySimpleObject;
import static com.ajaxjs.keyvalue.MappingValue.objectCast;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestMappingJson {
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

		String jsonStr = stringifySimpleObject(obj);
		// 输出 {"foo":"11","bar":"2222"}
		assertNotNull(jsonStr);
		assertEquals("{\"NULL\":null,\"str\":null,\"isOk\":false,\"n0\":0,\"n1\":111,\"n2\":222,\"msg\":\"Hello world\",\"arr\":[1,\"2\",null]}", jsonStr);
	}
	
	@Test
	public void testObj2jsonVaule() {
		assertEquals("null", 	obj2jsonVaule(null));
		assertEquals("1.0", 	obj2jsonVaule(1D));
		assertEquals("true", 	obj2jsonVaule(true));
		assertEquals("1", 		obj2jsonVaule(1));
		assertEquals("1", 		obj2jsonVaule(1L));
		assertEquals("\"2018-02-20 00:00:00\"", 		obj2jsonVaule((Date) objectCast("2018-2-20", Date.class)));
		
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		assertEquals("[1, 2, 3]", 		obj2jsonVaule(list));
		assertEquals("[1,2,3]", 		obj2jsonVaule(new int[] { 1, 2, 3 }));
		
		List<String> list2 = new ArrayList<>();
		list2.add("1");
		list2.add("2");
		list2.add("3");
		assertEquals("[\"1\", \"2\", \"3\"]", 		obj2jsonVaule(list2));
		assertEquals("[\"1\",\"2\",\"3\"]", 		obj2jsonVaule(new String[] { "1", "2", "3" }));
		
		Map<String, Object> map = new HashMap<>();
		assertEquals("{}", 		obj2jsonVaule(map));
		map.put("foo", "bar");
		assertEquals("{\"foo\":\"bar\"}", 		obj2jsonVaule(map));
		map.put("bar", 1);
		assertEquals("{\"bar\":1,\"foo\":\"bar\"}", 		obj2jsonVaule(map));
		
		List<Map<String, Object>> list3 = new ArrayList<>();
		assertEquals("[]", 		obj2jsonVaule(list3));
		list3.add(map);
		list3.add(map);
		assertEquals("[{\"bar\":1,\"foo\":\"bar\"}, {\"bar\":1,\"foo\":\"bar\"}]", 		obj2jsonVaule(list3));
	}
	
	@Test
	public void testStringifyMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "11");
		map.put("bar", 2222);
		map.put("bar2", null);
		map.put("bar3", true);

		String jsonStr = stringifyMap(map);
		assertEquals("{\"bar2\":null,\"bar\":2222,\"foo\":\"11\",\"bar3\":true}", jsonStr);
	}
}
