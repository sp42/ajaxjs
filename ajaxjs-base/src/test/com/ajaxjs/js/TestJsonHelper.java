package test.com.ajaxjs.js;

import static org.junit.Assert.*;
import org.junit.*;
import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.util.reflect.BeanUtil;

import test.com.ajaxjs.mock.MapMock;
import test.com.ajaxjs.mock.User;

import static com.ajaxjs.js.JsonHelper.*;

import java.util.HashMap;
import java.util.Map;

public class TestJsonHelper {
	JsonHelper engine;

	@Before
	public void setUp() {
		engine = new JsonHelper();
	}

	@Test
	public void testBean2Json() {
		User user = BeanUtil.map2Bean(MapMock.user, User.class, true);
		String json = bean2json(user);
		// System.out.println(json);
		assertNotNull(json);
		
		user = json2bean(json, User.class);
		assertEquals(user.getName(), "Jack");
		assertEquals(user.getLuckyNumbers()[0], 2);
		assertNotNull(user);
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