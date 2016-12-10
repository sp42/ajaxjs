package test.com.ajaxjs.util.json;

import static org.junit.Assert.*;

import java.util.Map;

import javax.script.ScriptException;

import org.junit.*;

import com.ajaxjs.json.IEngine;
import com.ajaxjs.json.Json;
import com.ajaxjs.json.Rhino;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

public class TestJS {
	IEngine js;
	Rhino rhino;

	@Before
	public void setUp() {
		rhino = new Rhino();
		js = rhino;
	}

	@Test
	public void testNativeObject2Hash() {
		Map<String, Object> map = rhino.eval_return_Map("{'a':0, 'b':1}");
		assertNotNull(map);
		assertEquals(map.get("a"), 0);

		Object obj = "{'a':0, 'b':1};";

		obj = js.eval("json = " + obj.toString());
		map = Rhino.NativeObject2Map((NativeObject) obj);
		assertNotNull(map);
		assertEquals(map.get("b"), 1);

		map = Json.callExpect_Map("var json = {'a':0, 'b':1};", "json");
		assertNotNull(map);
		assertEquals(map.get("b"), 1);
	}

	@Test
	public void testNativeArray2Map() throws ScriptException {
		String arrStr = "[{'a':0, 'b':1}, {'c':2}]";
		Map<String, Object>[] map = Json.callExpect_MapArray(arrStr);
		assertNotNull(map[0]);
		assertEquals(map[0].get("a"), 0);

		Object obj = rhino.eval("json = " + arrStr);

		map = Rhino.NativeArray2MapArray((NativeArray) obj);
		assertNotNull(map[1]);
		assertEquals(map[1].get("c"), 2);
	}

}
