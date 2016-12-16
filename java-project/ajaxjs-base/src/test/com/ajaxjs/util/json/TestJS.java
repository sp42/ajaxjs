package test.com.ajaxjs.util.json;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.junit.*;

import com.ajaxjs.util.json.JSON;
import com.ajaxjs.util.json.JsonHelper;

/**
 * @TODO
 * @author frank
 *
 */
public class TestJS {
	@Test
	public void testEval() throws ScriptException {
		JSON.eval("var foo ='Hello World!';");
//		Object obj;
//		obj = JSON.eval("foo='Hello World!';");
		String str = JSON.accessMember("var foo ='Hello World!';", "foo;", String.class);

		assertNotNull(str);
		assertEquals(str, "Hello World!");
		JSON.eval("foo = 111;");
		assertEquals(new Integer(111), JSON.eval("foo;", Integer.class));

		JSON.eval("foo = false;");
		assertEquals(JSON.eval("foo;", Boolean.class), false);
	}


	@Test
	public void testEval_return_String() throws ScriptException {
		String str = JSON.eval("'Hello';", String.class);

		assertNotNull(str);
		assertEquals(str, "Hello");
	}

	@Test
	public void testEval_return_Map() {
		Map<String, Object> map = JSON.getMap("json = {\"foo\" : \"88888\", \"bar\":99999};");

		assertNotNull(map);
		assertEquals(map.get("foo"), "88888");
		assertEquals(map.get("bar"), 99999);
	}

	@Test
	public void testEval_return_Map_String() {
		Map<String, Object> map = JSON.getMap("json = {\"foo\" : \"88888\"};");

		assertNotNull(map);
		assertEquals(map.get("foo").toString(), "88888");
	}

	@Test
	public void testEval_return_MapArray()  {
		List<Map<String, Object>> map = JSON.getList("[{\"foo\" : \"88888\"}, {\"bar\" : \"99999\"}];");

		assertNotNull(map);
		assertEquals(map.size(), 2);
		assertEquals(map.get(0).get("foo"), "88888");
		assertEquals(map.get(1).get("bar"), "99999");
	}

	@Test
	public void testToJavaType() {
		
	}
	
	@Test
	public void testFormatter() {
		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		String fotmatStr = JsonHelper.format(jsonStr);
		// fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
		// fotmatStr = fotmatStr.replaceAll("\t", "    ");
//		System.out.println(fotmatStr);
		assertNotNull(fotmatStr);
	}

}
