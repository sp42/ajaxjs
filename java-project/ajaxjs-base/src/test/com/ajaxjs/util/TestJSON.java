package test.com.ajaxjs.util;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ajaxjs.util.json.JSON;

import static org.junit.Assert.*;

public class TestJSON {

	@Test
	public void testGetMap() {
		Map<String, Object> map;
		map = JSON.getMap("{a:'hello', b: 'world!', c: { d: 'Nice!'}}");
		System.out.println(map.get("a"));
		assertNotNull(map);
		map = JSON.getMap("{a:'hello', b: 'world!', c: { d: 'Nice!'}}", "c");
		System.out.println(map.get("d"));
		assertNotNull(map);
		map = JSON.getMap("{a:'hello', b: 'world!', c: { d: 'Nice!', e: { f: 'fff'}}}", "c.e");
		System.out.println(map.get("f"));
		assertNotNull(map);
	}

	@Test
	public void testGetListMap() {
		List<Map<String, Object>> list;
		list = JSON.getList("[{a:'hello'}, 123, true]");
		System.out.println(list.get(0).get("a"));
		assertTrue(list.size() > 0);

		list = JSON.getList("[{a:'hello'}, {b: 'world!'}, {c: { d: 'Nice!'}}]");
		System.out.println(list.get(0).get("a"));
		assertTrue(list.size() > 0);

		list = JSON.getList("{a:'hello', b: 'world!', c: [{ d: 'Nice!!!'}]}", "c");
		System.out.println(list.get(0).get("d"));
	}

	@Test
	public void testGetListString() {
		List<String> list;
		list = JSON.getStringList("['a', 'b', 'c']");
		System.out.println(list.get(0));
		assertTrue(list.size() > 0);

		list = JSON.getStringList("[1, 'b', 'c']");
		System.out.println(list.get(1));
		assertTrue(list.size() > 0);

	}
}
