package test.com.ajaxjs.json;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.json.IEngine;
import com.ajaxjs.json.Json;
import com.ajaxjs.json.Rhino;

import java.util.HashMap;
import java.util.Map;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

public class TestMapper {
	IEngine js;
	Rhino rhino;
	@BeforeClass
	public static void init(){}
	
	@Before
	public void setUp(){
		rhino = new Rhino();
		js = rhino;
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
	
	@Test
	public void testNativeObject2Hash(){
		Map<String, Object> map = rhino.eval_return_Map("{'a':0, 'b':1}");
		assertNotNull(map);
		assertEquals(map.get("a"), 0);
		
		Object obj = "{'a':0, 'b':1};"; 
		
		obj = js.eval("json = " + obj.toString());
		map = Rhino.NativeObject2Map((NativeObject)obj);
		assertNotNull(map);
		assertEquals(map.get("b"), 1);
		
		map = Json.callExpect_Map("var json = {'a':0, 'b':1};", "json");
		assertNotNull(map);
		assertEquals(map.get("b"), 1);
	}
	
	@Test
	public void testNativeArray2Map(){
		String arrStr = "[{'a':0, 'b':1}, {'c':2}]";
		Map<String, Object>[] map = Json.callExpect_MapArray(arrStr);
		assertNotNull(map[0]);
		assertEquals(map[0].get("a"), 0);
		
		Object obj = js.eval("json = " + arrStr);
	 
		map = Rhino.NativeArray2MapArray((NativeArray)obj);
		assertNotNull(map[1]);
		assertEquals(map[1].get("c"), 2);
	}
	
	@Test
	public void testNativeStringify(){
		Object obj = js.eval("json = {'a':0, 'b':1};");
		String jsonStr = Json.JSON_stringify((NativeObject)obj);
		assertNotNull(jsonStr);
	}
}