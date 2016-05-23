package com.ajaxjs.test.javascript;


import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.app.App;
import com.ajaxjs.javascript.Utils;
import com.ajaxjs.json.IEngine;
import com.ajaxjs.json.Json;
import com.ajaxjs.json.Nashorn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptException;


public class TestJS {
	IEngine js;

	@BeforeClass
	public static void init() {
	}

	@Before
	public void setUp() {
		js = new Nashorn();
	}

	@Test
	public void testEval() throws ScriptException {
		js.eval("var foo ='Hello World!';");
		Object obj;
		obj = js.eval("foo='Hello World!';");
		String str = js.eval("foo;", String.class);

		assertNotNull(obj);
		assertEquals(str, "Hello World!");
		js.eval("foo = 111;");
		assertTrue(js.eval("foo;", int.class) == 111);

		js.eval("foo = false;");
		assertEquals(js.eval("foo;", boolean.class), false);
	}

	@Test
	public void testLoad() throws ScriptException, IOException {
		Object obj;

		js.load("C:/project/bigfoot/java_v3/com/ajaxjs/app/config.js");
		obj = js.eval("bf");
		assertNotNull(obj);

		js.load(App.class, "config.js");
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
		String str = js.eval("'Hello';", String.class);

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
		Object obj = js.eval("json = [{\"foo\" : \"88888\"}, {\"bar\" : \"99999\"}];");
		
		Map<String, Object>[] map = Utils.toMapArray(obj);
		assertNotNull(map);
		assertEquals(map.length, 2);
		assertEquals(map[0].get("foo"), "88888");
		assertEquals(map[1].get("bar"), "99999");
	}

	@Test
	public void testFormatter() {
		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		String fotmatStr = Utils.format(jsonStr);
		// fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
		// fotmatStr = fotmatStr.replaceAll("\t", "    ");
		System.out.println(fotmatStr);
		assertNotNull(fotmatStr);
	}
	
	@Test
	public void testStringify_Map(){
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "11");
		map.put("bar", 2222);
		map.put("bar2", null);
		map.put("bar3", true);
		
		String jsonStr = Json.stringify(map);
		assertNotNull(jsonStr);
		assertEquals(jsonStr, "{\"foo\":\"11\",\"bar3\":true,\"bar\":2222,\"bar2\":null}");
	}
	
	@Test
	public void testStringify_Obj(){
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
//			@SuppressWarnings("unused")
//			public Date date = new Date();   
			@SuppressWarnings("unused")
			public String msg = "Hello world";
			@SuppressWarnings("unused")
			public Object[] arr = new Object[]{
				1, "2", null	
			};
	    };
		
		String jsonStr = Json.stringify(obj);
		// 输出 {"foo":"11","bar":"2222"}
		assertNotNull(jsonStr);
		assertEquals(jsonStr, "{\"NULL\":null,\"str\":null,\"isOk\":false,\"n0\":0,\"n1\":111,\"n2\":222,\"msg\":\"Hello world\",\"arr\":[1,\"2\",null]}");
	}
	
//	@Test
//	public void testNativeObject2Hash(){
//		Map<String, Object> map = Mapper.callExpect_Map("{'a':0, 'b':1}");
//		assertNotNull(map);
//		assertEquals(map.get("a"), 0);
//		
//		Object obj = "{'a':0, 'b':1};"; 
//		obj = js.eval("json = " + obj.toString());
//		map = Mapper.NativeObject2Map((NativeObject)obj);
//		assertNotNull(map);
//		assertEquals(map.get("b"), 1);
//		
//		map = Mapper.callExpect_Map("var json = {'a':0, 'b':1};", "json");
//		assertNotNull(map);
//		assertEquals(map.get("b"), 1);
//	}
	
//	@Test
//	public void testNativeArray2Map(){
//		String arrStr = "[{'a':0, 'b':1}, {'c':2}]";
//		Map<String, Object>[] map = Mapper.callExpect_MapArray(arrStr);
//		assertNotNull(map[0]);
//		assertEquals(map[0].get("a"), 0);
//		
//		JsEngine js = new JsEngine();
//		Object obj = js.eval("json = " + arrStr);
//	 
//		
//		map = Mapper.NativeArray2MapArray((NativeArray)obj);
//		assertNotNull(map[1]);
//		assertEquals(map[1].get("c"), 2);
//	}
	
//	@Test
//	public void testNativeStringify(){
//		Object obj = js.eval("json = {'a':0, 'b':1};");
//		String jsonStr = Util.navtiveStringify((NativeObject)obj);
//		assertNotNull(jsonStr);
//	}
}
