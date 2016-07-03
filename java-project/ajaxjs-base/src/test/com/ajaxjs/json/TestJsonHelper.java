package test.com.ajaxjs.json;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.util.json.JSON;
import com.ajaxjs.util.json.JsonHelper;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import sun.org.mozilla.javascript.internal.NativeObject;

public class TestJsonHelper {
	JsonHelper jsonHelper;

	@BeforeClass
	public static void init() {
	}

	@Before
	public void setUp() {
		jsonHelper = new JsonHelper(JSON.engineFatory());
	}

	@Test
	public void testStringify_Map() {
		Map<String, Object> map = new HashMap<>();
		map.put("foo", "11");
		map.put("bar", 2222);
		map.put("bar2", null);
		map.put("bar3", true);

		String jsonStr = JsonHelper.stringify(map);
		assertNotNull(jsonStr);
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

		String jsonStr = JsonHelper.stringify(obj);
		// 输出 {"foo":"11","bar":"2222"}
		assertNotNull(jsonStr);
		assertEquals(jsonStr,
				"{\"NULL\":null,\"str\":null,\"isOk\":false,\"n0\":0,\"n1\":111,\"n2\":222,\"msg\":\"Hello world\",\"arr\":[1,\"2\",null]}");
	}

	@Test
	public void testNativeStringify() throws ScriptException {
		Object obj = jsonHelper.getEngine().eval("json = {'a':0, 'b':1};");
		@SuppressWarnings("unchecked")
		String jsonStr = JsonHelper.stringify((NativeObject) obj);
		assertNotNull(jsonStr);
	}
}