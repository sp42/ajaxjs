package com.ajaxjs.util.map;

import static com.ajaxjs.util.map.MapTool.as;
import static com.ajaxjs.util.map.MapTool.join;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.StringUtils;

import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.TestCaseUserBean;

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
	public void testToMap() {
		assertEquals(1, MapTool.toMap(new String[] { "a", "b", "d" }, new String[] { "1", "c", "2" }, ObjectHelper::toJavaValue).get("a"));
		assertEquals(1, MapTool.toMap(new String[] { "a=1", "b=2", "d=c" }, ObjectHelper::toJavaValue).get("a"));
		assertEquals("你好", MapTool.toMap(new String[] { "a=%e4%bd%a0%e5%a5%bd", "b=2", "d=c" }, v -> StringUtils.uriDecode(v, StandardCharsets.UTF_8)).get("a"));
	}

	@Test
	public void testAsString() {
		assertEquals("500", as(map, v -> v.toString()).get("bar"));
		assertEquals("[1, c, 2]", as(new HashMap<String, String[]>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", new String[] { "a", "b" });
				put("bar", new String[] { "1", "c", "2" });
			}
		}, v -> Arrays.toString(v)).get("bar"));
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
		}, v -> ObjectHelper.toJavaValue(v.toString())).get("bar"));
	}

	public static Map<String, Object> userWithoutChild = new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("id", 1L);
			put("name", "Jack");
			put("age", 30);
			put("birthday", new Date());
		}
	};

	public static class MapMock {
		static boolean s = true;
		public static Map<String, Object> user = new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("id", 1L);
				put("name", "Jack");
				put("sex", s);
				put("age", 30);
				put("birthday", new Date());

				put("children", "Tom,Peter");
				put("luckyNumbers", "2, 8, 6");
			}
		};
	}

	@Test
	public void testMap2Bean() {
		TestCaseUserBean user = MapTool.map2Bean(userWithoutChild, TestCaseUserBean.class);// 直接转
		assertNotNull(user);
		assertEquals(user.getName(), "Jack");

		user = MapTool.map2Bean(MapMock.user, TestCaseUserBean.class, true);

		assertNotNull(user);
		assertEquals("Tom", user.getChildren()[0]);
		assertEquals(8, user.getLuckyNumbers()[1]);
		assertEquals(true, user.isSex());
	}

	@Test
	public void testBean2Map() {
		TestCaseUserBean user = MapTool.map2Bean(MapMock.user, TestCaseUserBean.class, true);
		Map<String, Object> map = MapTool.bean2Map(user);

		assertNotNull(map);
		assertEquals("Jack", map.get("name"));
	}

//	@Test
//	public void testBean2Json() {
//		TestCaseUserBean user = map2Bean(MapMock.user, TestCaseUserBean.class, true);
//		String json = beanToJson(user);
//		assertNotNull(json);
//
//		user = json2bean(json, TestCaseUserBean.class);
//		assertEquals("Jack", user.getName());
//		assertEquals(2, user.getLuckyNumbers()[0]);
//		assertNotNull(user);
//	}

	@Test
	public void testXml() {
		String xml = MapTool.mapToXml(userWithoutChild);
		assertEquals(xml, MapTool.mapToXml(MapTool.xmlToMap(xml)));
	}
}
