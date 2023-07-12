//package com.ajaxjs.jsonparser;
//
//import com.ajaxjs.util.MappingValue;
//import com.ajaxjs.util.map.JsonHelper;
//import org.junit.Test;
//
//import java.util.*;
//
//import static com.ajaxjs.util.map.JsonHelper.format;
//import static com.ajaxjs.util.map.JsonHelper.parseMap;
//import static com.ajaxjs.util.map.MapTool.as;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//public class TestJsonHelper {
//
//	static Map<String, String[]> m1 = new HashMap<String, String[]>() {
//		private static final long serialVersionUID = 1L;
//		{
//			put("foo", new String[] { "a", "b" });
//			put("bar", new String[] { "1", "c", "2" });
//		}
//	};
//
//	static Map<String, String> m2 = new HashMap<String, String>() {
//		private static final long serialVersionUID = 1L;
//		{
//			put("foo", "a");
//			put("bar", "1");
//		}
//	};
//
//	@Test
//	public void test() {
//		Map<String, Object> map;
//
//		map = as(m1, arr -> arr[0]);
//		assertNotNull(map.get("bar").getClass());
//
//		map = as(m1, arr -> MappingValue.toJavaValue(arr[0]));
//		assertNotNull(map.get("bar").getClass());
//	}
//
//	@Test
//	public void testStringifySimpleObject() {
//		Object obj = new Object() {
//			@SuppressWarnings("unused")
//			public Object NULL = null;
//			@SuppressWarnings("unused")
//			public String str = null;
//			@SuppressWarnings("unused")
//			public Boolean isOk = false;
//			@SuppressWarnings("unused")
//			public Integer n0 = 0;
//			@SuppressWarnings("unused")
//			public Number n1 = 111;
//			@SuppressWarnings("unused")
//			public int n2 = 222;
//			// @SuppressWarnings("unused")
//			// public Date date = new Date();
//			@SuppressWarnings("unused")
//			public String msg = "Hello world";
//			@SuppressWarnings("unused")
//			public Object[] arr = new Object[] { 1, "2", null };
//		};
//
//		String jsonStr = JsonHelper.toJson(obj);
//		// 输出 {"foo":"11","bar":"2222"}
//		assertNotNull(jsonStr);
//		assertEquals("{\"NULL\":null,\"str\":null,\"isOk\":false,\"n0\":0,\"n1\":111,\"n2\":222,\"msg\":\"Hello world\",\"arr\":[1, \"2\", null]}", jsonStr);
//	}
//
//	@Test
//	public void testStringifyMap() {
//		Map<String, Object> map = new HashMap<>();
//		map.put("foo", "11");
//		map.put("bar", 2222);
//		map.put("bar2", null);
//		map.put("bar3", true);
//
//		String jsonStr = JsonHelper.toJson(map);
//		assertEquals("{\"bar2\":null,\"bar\":2222,\"foo\":\"11\",\"bar3\":true}", jsonStr);
//	}
//
//	@Test
//	public void testObj2jsonVaule() {
//		assertEquals(null, JsonHelper.toJson(null));
//		assertEquals("1.0", JsonHelper.toJson(1D));
//		assertEquals("true", JsonHelper.toJson(true));
//		assertEquals("1", JsonHelper.toJson(1));
//		assertEquals("1", JsonHelper.toJson(1L));
//		assertEquals("\"2018-02-20 00:00:00\"", JsonHelper.toJson((Date) MappingValue.objectCast("2018-2-20", Date.class)));
//
//		List<Integer> list = new ArrayList<>();
//		list.add(1);
//		list.add(2);
//		list.add(3);
//		assertEquals("[1, 2, 3]", JsonHelper.toJson(list));
//assertEquals("[1, 2, 3]", JsonHelper.toJson(new Integer[] { 1, 2, 3 }));
//assertEquals("[1, 2, 3]", JsonHelper.toJson(new int[] { 1, 2, 3 }));
//
//		List<String> list2 = new ArrayList<>();
//		list2.add("1");
//		list2.add("2");
//		list2.add("3");
//		assertEquals("[\"1\", \"2\", \"3\"]", JsonHelper.toJson(list2));
//		assertEquals("[\"1\", \"2\", \"3\"]", JsonHelper.toJson(new String[] { "1", "2", "3" }));
//
//		Map<String, Object> map = new HashMap<>();
//		assertEquals("{}", JsonHelper.toJson(map));
//		map.put("foo", "bar");
//		assertEquals("{\"foo\":\"bar\"}", JsonHelper.toJson(map));
//		map.put("bar", 1);
//		assertEquals("{\"bar\":1,\"foo\":\"bar\"}", JsonHelper.toJson(map));
//
//List<Map<String, Object>> list3 = new ArrayList<>();
//assertEquals("[]", JsonHelper.toJson(list3));
//list3.add(map);
//list3.add(map);
//assertEquals("[{\"bar\":1,\"foo\":\"bar\"}, {\"bar\":1,\"foo\":\"bar\"}]", JsonHelper.toJson(list3));
//
//	}
//
//	static String[] _map = {"{",
//			 "	\"site\" : {",
//			 "		\"titlePrefix\" : \"大华•川式料理\",",
//			 "		\"keywords\" : \"大华•川式料理\",",
//			 "		\"description\" : \"大华•川式料理饮食有限公司于2015年成立，本公司目标致力打造中国新派川菜系列。炜爵爷川菜料理系列的精髓在于清、鲜、醇、浓、香、烫、酥、嫩，擅用麻辣。在服务出品环节上，团队以ISO9000为蓝本建立标准化餐饮体系，务求以崭新的姿态面向社会各界人仕，提供更优质的服务以及出品。炜爵爷宗旨：麻辣鲜香椒，美味有诀窍，靓油用一次，精品煮御赐。 \",",
//			 "		\"footCopyright\":\"dsds\" ",
//			 "	},",
//			 "	\"dfd\":{",
//			 "		\"dfd\":\'fdsf\',",
//			 "		\"id\": 888,",
//			 "		\"dfdff\":{",
//			 "			\"dd\":\'fd\'",
//			 "		}",
//			 "	},",
//			 "	\"clientFullName\":88,",
//			 "	\"clientShortName\":\"大华\",",
//			 "	\"isDebug\": true,",
//			 "	\"data\" : {",
//			 "		\"newsCatalog_Id\" : 6,",
//			 "		\"jobCatalog_Id\" :7",
//			 "	}",
//			 "}"};
//
//	public static Map<String, Object> map = parseMap(String.join("", _map));
//
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testParsrMap() {
//		assertEquals("dsds", ((Map<String, Object>)map.get("site")).get("footCopyright"));
//	}
//
//	@Test
//	public void testParseMap() {
//		assertEquals("大华", map.get("clientShortName").toString());
//		assertEquals(88, map.get("clientFullName"));
//		assertEquals(true, (boolean)map.get("isDebug"));
//	}
//
//	static String[] _list = {"[ {",
//			 "		\'name\' : \"关于我们\",",
//			 "		\'id\' : \'about\',",
//			 "		\'children\' : [ ",
//			 "			{",
//			 "				name : \"公司历程\",",
//			 "				id : \'history\'",
//			 "			},",
//			 "			{",
//			 "				name : \"企业文化\",",
//			 "				id : \'cluture\'",
//			 "			}",
//			 "		]",
//			 "	}, {",
//			 "		\'name\' : \"美食天地\",",
//			 "		\'id\' : \'product\',",
//			 "		\'children\' : [ ",
//			 "			{",
//			 "				name : \"最新美食\",",
//			 "				id : \'new\',",
//			 "				\'children\' : [",
//			 "					{",
//			 "						\'id\' : \'yuecai\',",
//			 "						\'name\' : \'粤菜\'",
//			 "					},",
//			 "					{",
//			 "						\'id\' : \'xiangcai\',",
//			 "						\'name\' : \'湘菜\'",
//			 "					}",
//			 "				]",
//			 "			},",
//			 "			{",
//			 "				name : \"热门菜谱\",",
//			 "				id : \'hot\'",
//			 "			}",
//			 "		]",
//			 "	}]"};
//
//	public static List<Map<String, Object>> list = JsonHelper.parseList(String.join("", _list));
//
//	@Test
//	public void testParseList() {
//		assertEquals("about", ((Map<String, Object>)TestJsonHelper.list.get(0)).get("id"));
//	}
//
//	@Test
//	public void testFormatter() {
//		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
//		String fotmatStr = format(jsonStr);
//		assertNotNull(fotmatStr);
//	}
//
//}