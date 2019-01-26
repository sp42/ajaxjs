package com.ajaxjs.util;

import static com.ajaxjs.util.BeanUtil.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestBeanUtil {
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
		TestCaseUserBean user = map2Bean(userWithoutChild, TestCaseUserBean.class);// 直接转
		assertNotNull(user);
		assertEquals(user.getName(), "Jack");

		user = map2Bean(MapMock.user, TestCaseUserBean.class, true);

		assertNotNull(user);
		assertEquals(user.getChildren()[0], "Tom");
		assertEquals(user.getLuckyNumbers()[1], 8);
		assertEquals(user.isSex(), true);
	}

	@Test
	public void testBean2Map() {
		TestCaseUserBean user = map2Bean(MapMock.user, TestCaseUserBean.class, true);
		Map<String, Object> map = bean2Map(user);

		assertNotNull(map);
		assertEquals(map.get("name"), "Jack");
	}

	@Test
	public void testBean2Json() {
		TestCaseUserBean user = map2Bean(MapMock.user, TestCaseUserBean.class, true);
		String json = beanToJson(user);
		assertNotNull(json);

		System.out.println(json);
		user = json2bean(json, TestCaseUserBean.class);
		assertEquals("Jack", user.getName());
		assertEquals(2, user.getLuckyNumbers()[0]);
		assertNotNull(user);
	}
}
