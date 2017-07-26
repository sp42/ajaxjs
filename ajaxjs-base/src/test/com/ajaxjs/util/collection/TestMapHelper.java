package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;
import static com.ajaxjs.util.collection.MapHelper.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestMapHelper {
	Map<String, Object> map = new HashMap<String, Object>() {
		private static final long serialVersionUID = 1L;
		{
			put("foo", null);
			put("bar", 500);
			put("zx", "hi");
		}
	};
	
	@Test
	public void testJoin() {
		assertEquals("zx=hi&foo=null&bar=500", join(asString(map)));
	}
	
	@Test
	public void testAsString() {
		assertEquals(asString(map).get("bar"), "500");
	}
	
	@Test
	public void testAsObject() {
		assertEquals(asObject(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", "null");
				put("bar", "500");
				put("zx", "hi");
			}
		}).get("bar"), "500");
		
		assertEquals(asObject(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", "null");
				put("bar", "500");
				put("zx", "hi");
			}
		}, true).get("bar"), 500);
	}
	
	@Test
	public void testToMap() {
		assertEquals(toMap(new HashMap<String, String[]>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", new String[] { "a", "b" });
				put("bar", new String[] { "1", "c", "2" });
			}
		}).get("bar"), "1,c,2");
		
		assertEquals(
			toMap(new String[] { "a", "b", "d" }, new String[] { "1", "c", "2" }).get("a"),
			1
		);
		
		assertEquals(
			toMap(new String[] { "a=1", "b=2", "d=c" }).get("a"),
			1
		);
		
		assertEquals(
			toMap(new String[] { "a=%e4%bd%a0%e5%a5%bd", "b=2", "d=c" }, true).get("a"),
			"你好"
		);
	}
}
