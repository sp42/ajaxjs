package com.ajaxjs.js;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.map.ListMap;
import com.ajaxjs.util.map.ListMapConfig;

public class TestListMap {
	static ListMapConfig config = new ListMapConfig();

	static {
		config.mapEntryHandler = (String key, Object obj, Map<String, Object> map, Map<String, Object> superMap,
				int level) -> {
			System.out.println(key + ":" + obj);
			return true;
		};
	}

//	@Test
	public void testTraveler() {
		ListMap.traveler(TestJsonHelper.list, config);

		ListMapConfig config2 = new ListMapConfig();
		config2.mapEntryHandler = (key, obj, map, superMap, level) -> {
			if (key.equals("id")) {
				System.out.println(key + ":");
				return false;
			}

			return true;
		};

		ListMap.traveler(TestJsonHelper.list, config2);

		ListMap.buildPath(TestJsonHelper.list);

		System.out.println(TestJsonHelper.list);
	}

//	@Test
	public void testFindByPath() {
		assertEquals("关于我们", ListMap.findByPath("about", TestJsonHelper.list).get("name"));
		assertEquals("企业文化", ListMap.findByPath("about/cluture", TestJsonHelper.list).get("name"));
		assertEquals("粤菜", ListMap.findByPath("product/new/yuecai", TestJsonHelper.list).get("name"));
	}

//	@Test
	public void testMapListTravel() {
		ListMap.traveler(TestJsonHelper.map, config);
	}
	
	@Test
	public void testFlatMap() {
		Map<String, Object> fMap = ListMap.flatMap(TestJsonHelper.map);
		assertEquals(7, fMap.get("data.jobCatalog_Id"));
		System.out.println(fMap);
	}
}
