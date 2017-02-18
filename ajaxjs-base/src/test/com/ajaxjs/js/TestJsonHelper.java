package test.com.ajaxjs.js;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.js.JsonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJsonHelper {
	JsonHelper engine;

	@Before
	public void setUp() {
		engine = new JsonHelper();
	}
	
	@Test
	public void testAccessJsonMember(){
		engine.setJsonString("{\"foo\" : \"88888\", \"bar\": {a:'hello', b: 'world!', c: { d: 'Nice!'}}}");
		Object obj;
		
		obj = engine.accessJsonMember(null, null);
		assertEquals(obj, null); // 永远返回 null
		
		obj = engine.accessJsonMember(null, Object.class);
		assertNotNull(obj);
		
		assertEquals(engine.accessJsonMember("foo", String.class), "88888");
		assertEquals(engine.accessJsonMember("bar.a", String.class), "hello");
		assertEquals(engine.accessJsonMember("bar.c.d", String.class), "Nice!");

	}
	
	@Test
	public void testGetMap() {
		Map<String, Object> map;
		
		engine.setJsonString("{\"foo\" : \"88888\", \"bar\":99999}");
		map = engine.getMap(null);

		assertEquals(map.get("foo"), "88888");
		assertEquals(map.get("bar"), 99999);
		
		engine.setJsonString("{a:'hello', b: 'world!', c: { d: 'Nice!'}}");
		map = engine.getMap("c");
		
		assertEquals(map.get("d"), "Nice!");
		
		engine.setJsonString("{a:'hello', b: 'world!', c: { d: 'Nice!', e: { f: 'fff'}}}");
		map = engine.getMap("c.e");
		
		assertEquals(map.get("f"), "fff");
	}

	@Test
	public void testGetListString() {
		List<String> list;
		
		engine.setJsonString("['a', 'b', 'c']");
		list = engine.getStringList(null);
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(0), "a");

		engine.setJsonString("[1, 'b', 'c']");
		list = engine.getStringList(null);

		assertTrue(list.size() > 0);
		assertEquals(list.get(1), "b");
		
		engine.setJsonString("{a:[1, 'b', 'c']}");
		list = engine.getStringList("a");
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(1), "b");
	}
	
	@Test
	public void testGetListMap() {
		List<Map<String, Object>> list;
		
		// map
		engine.setJsonString("[{\"foo\" : \"88888\"}, {\"bar\" : \"99999\"}]");
		list = engine.getList(null);

		assertEquals(list.size(), 2);
		assertEquals(list.get(0).get("foo"), "88888");
		assertEquals(list.get(1).get("bar"), "99999");
		
		// mixed map
		engine.setJsonString("[{a:'hello'}, {b: 'world!'}, {c: { d: 'Nice!'}}]");
		list = engine.getList(null);
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(0).get("a"), "hello");

		// single map
		engine.setJsonString("[{a:'hello'}, 123, true]");
		list = engine.getList(null);
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(0).get("a"), "hello");

		// by key
		engine.setJsonString("{a:'hello', b: 'world!', c: [{ d: 'Nice!!!'}]}");
		list = engine.getList("c");
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(0).get("d"), "Nice!!!");
	}

	@Test
	public void testStringify() {
		engine.eval("var foo = {a:'hello', b: 'world!', c: [{ d: 'Nice!!!'}]};");
		
		assertEquals(engine.stringify("foo"), "{\"a\":\"hello\",\"b\":\"world!\",\"c\":[{\"d\":\"Nice!!!\"}]}");
		assertEquals(engine.stringifyObj(engine.eval("foo;")), "{\"a\":\"hello\",\"b\":\"world!\",\"c\":[{\"d\":\"Nice!!!\"}]}");
	}

	@Test
	public void testStringify_Map() {
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "11");
		map.put("bar", 2222);
		map.put("bar2", null);
		map.put("bar3", true);

		String jsonStr = JsonHelper.stringifyMap(map);
		assertEquals(jsonStr, "{\"foo\":\"11\",\"bar3\":true,\"bar\":2222,\"bar2\":null}");
	}

	@Test
	public void testStringify_Obj() {
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

		String jsonStr = JsonHelper.stringify_object(obj);
		// 输出 {"foo":"11","bar":"2222"}
		assertNotNull(jsonStr);
		assertEquals(jsonStr,
				"{\"NULL\":null,\"str\":null,\"isOk\":false,\"n0\":0,\"n1\":111,\"n2\":222,\"msg\":\"Hello world\",\"arr\":[1,\"2\",null]}");
	}
	
	@Test
	public void testFormatter() {
		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		String fotmatStr = JsonHelper.format(jsonStr);
		
		// fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
		// fotmatStr = fotmatStr.replaceAll("\t", "    ");
		
		System.out.println(fotmatStr);
		assertNotNull(fotmatStr);
	}

}