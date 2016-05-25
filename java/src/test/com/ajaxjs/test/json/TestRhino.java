package com.ajaxjs.test.json;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.app.App;
import com.ajaxjs.json.Json;
import com.ajaxjs.json.Rhino;

import java.io.IOException;
import java.util.Map;
import javax.script.ScriptException;


public class TestRhino {
	Rhino js;

	@BeforeClass
	public static void init() {
	}

	@Before
	public void setUp() {
		js = new Rhino();
	}

	@Test
	public void testEval() throws ScriptException {
		js.eval("var foo ='Hello World!';");
		Object obj;
		obj = js.eval("foo='Hello World!';");
		String str = js.eval_return_String("foo;");

		assertNotNull(obj);
		assertEquals(str, "Hello World!");
		js.eval("foo = 111;");
		assertEquals(js.eval_return_Int("foo;"), 111);

		js.eval("foo = false;");
		assertEquals(js.eval_return_Boolean("foo;"), false);
	}

	@Test
	public void testLoad() throws ScriptException, IOException {
		Object obj;

		js.load("C:/project/bigfoot/java/com/ajaxjs/framework/config.js");
		obj = js.eval("bf");
		assertNotNull(obj);

		js.load(App.class, "JSON_Tree.js");
		obj = js.eval("bf");
		assertNotNull(obj);
	}

	@Test
	public void testPut() throws ScriptException {
		js.put("a", 6);
		Object obj = js.eval("a");

		assertNotNull(obj);
		assertEquals(obj, 6);
	}
	
	@Test
	public void testGet() throws ScriptException {
		js.eval("a={b:{c:{d:1}}}");
		
		assertNotNull(js.get("a"));
		assertNotNull(js.get("a", "b", "c", "d"));
	}

	// @Test
	// public void testCall() throws ScriptException {
	// js.eval("function max_num(a, b){return (a > b) ? a : b;}");
	// Object obj = js.call(null, "max_num", 6, 4);
	//
	// assertNotNull(obj);
	// assertEquals(obj, 6);
	// }
	//
	@Test
	public void testEval_return_String() throws ScriptException {
		String str = js.eval_return_String("'Hello';");

		assertNotNull(str);
		assertEquals(str, "Hello");
	}

	@Test
	public void testEval_return_Map() {
		Map<String, Object> map = js.eval_return_Map("json = {\"foo\" : \"88888\", \"bar\":99999};");

		assertNotNull(map);
		assertEquals(map.get("foo"), "88888");
		assertEquals(map.get("bar"), 99999);
	}

	@Test
	public void testEval_return_Map_String() {
		Map<String, Object> map = js.eval_return_Map("json = {\"foo\" : \"88888\"};");

		assertNotNull(map);
		assertEquals(map.get("foo").toString(), "88888");
	}

	@Test
	public void testEval_return_MapArray()  {
		Map<String, Object>[] map = js.eval_return_MapArray("[{\"foo\" : \"88888\"}, {\"bar\" : \"99999\"}];");

		assertNotNull(map);
		assertEquals(map.length, 2);
		assertEquals(map[0].get("foo"), "88888");
		assertEquals(map[1].get("bar"), "99999");
	}

	@Test
	public void testToJavaType() {
		
	}
	
	@Test
	public void testFormatter() {
		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		String fotmatStr = Json.format(jsonStr);
		// fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
		// fotmatStr = fotmatStr.replaceAll("\t", "    ");
		System.out.println(fotmatStr);
		assertNotNull(fotmatStr);
	}
}
