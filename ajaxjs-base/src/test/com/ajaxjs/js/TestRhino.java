package test.com.ajaxjs.js;

import static org.junit.Assert.*;
import org.junit.*;


import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

public class TestRhino {

//	@Test
//	public void testNativeObject2Hash() {
//		Map<String, Object> map = Rhino.getMap("{'a':0, 'b':1}");
//		assertNotNull(map);
//		assertEquals(map.get("a"), 0);
//
//		Object obj = "{'a':0, 'b':1};";
//
//		obj = Rhino.eval("json = " + obj.toString());
//		map = Rhino.NativeObject2Map((NativeObject) obj);
//		assertNotNull(map);
//		assertEquals(map.get("b"), 1);
//
//		map = Rhino.getMap("var json = {'a':0, 'b':1};", "json");
//		assertNotNull(map);
//		assertEquals(map.get("b"), 1);
//	}
//
//	@Test
//	public void testNativeArray2Map() throws ScriptException {
//		String arrStr = "[{'a':0, 'b':1}, {'c':2}]";
//		List<Map<String, Object>> map = Rhino.getList(arrStr);
//		assertNotNull(map.get(0));
//		assertEquals(map.get(0).get("a"), 0);
//
//		Object obj = Rhino.eval("json = " + arrStr);
//
//		Map<String, Object>[] maps = Rhino.NativeArray2MapArray((NativeArray) obj);
//		assertNotNull(maps[0]);
//		assertEquals(maps[1].get("c"), 2);
//	}
	
	@Test
	public void testNativeStringify() throws ScriptException {
//		Object obj = jsonHelper.getEngine().eval("json = {'a':0, 'b':1};");
//		@SuppressWarnings("unchecked")
//		String jsonStr = JsonHelper.stringify((NativeObject) obj);
//		assertNotNull(jsonStr);
	}
}
