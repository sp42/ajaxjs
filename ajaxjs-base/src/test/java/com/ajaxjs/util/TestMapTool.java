package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.MapTool;
import com.ajaxjs.util.MappingValue;

import static com.ajaxjs.util.MapTool.*;

public class TestMapTool {
	Map<String, Object> map = new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("foo", null);
			put("bar", 500);
			put("zx", "hi");
		}
	};

	@Test
	public void testJoin() {
		assertEquals("bar=500&foo=null&zx=hi", join(as(map, v -> v.toString())));
	}

	@Test
	public void testAsString() {
		assertEquals("500", as(map, v -> v.toString()).get("bar"));
	}

	@Test
	public void testAsObject() {
		assertEquals(500, as(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", "null");
				put("bar", "500");
				put("zx", "hi");
			}
		}, v -> MappingValue.toJavaValue(v.toString())).get("bar"));
	}

	@Test
	public void testToMap() {
		assertEquals("[1, c, 2]", as(new HashMap<String, String[]>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", new String[] { "a", "b" });
				put("bar", new String[] { "1", "c", "2" });
			}
		}, v -> Arrays.toString(v)).get("bar"));

		assertEquals(1, MapTool.toMap(new String[] { "a", "b", "d" }, new String[] { "1", "c", "2" }, MappingValue::toJavaValue).get("a"));

		assertEquals(1, MapTool.toMap(new String[] { "a=1", "b=2", "d=c" }, MappingValue::toJavaValue).get("a"));

		assertEquals("你好", MapTool.toMap(new String[] { "a=%e4%bd%a0%e5%a5%bd", "b=2", "d=c" }, Encode::urlDecode).get("a"));
	}

	@Test
	public void testXml() {
//		MappingHelper.mapToXml(data);

	}
}
