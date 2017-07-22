package test.com.ajaxjs.js;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.js.JsonParser;

import java.util.List;
import java.util.Map;

public class TestJSON {
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
		engine.setAutoDouble2IntInList(false);
		list = engine.getList(null);
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(0).get("a"), "hello");

		// by key
		engine.setJsonString("{a:'hello', b: 'world!', c: [{ d: 'Nice!!!'}]}");
		list = engine.getList("c");
		
		assertTrue(list.size() > 0);
		assertEquals(list.get(0).get("d"), "Nice!!!");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJson2Map() {
		Object obj = JsonParser.json2Map2("{x1:true, x2:2000, x3:\"1:hello world\",a:{b:{c:{d:{e:[1,\"j\",3,4,5,6]}}}}}");
		assertTrue(obj instanceof Map);
		Map<String, Object> map = (Map<String, Object>)obj;
		System.out.println(map.get("a"));
		assertTrue(map.get("a") instanceof Map);
		assertEquals(map.get("x1"), true);
		assertEquals(map.get("x2"), 2000);
		System.out.println(map.get("x3"));
		System.out.println(map.get("x1").getClass());
		assertNotNull(obj);
	}

}