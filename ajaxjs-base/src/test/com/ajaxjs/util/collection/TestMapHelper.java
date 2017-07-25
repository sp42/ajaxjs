package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

 

public class TestMapHelper {
	@Test
	public void testIsEmptyString() {
		MapHelper<String, IValue> map = new MapHelper<String, IValue>();
		map.put("foo", new Value("string"));
		map.put("foo", new Value("123"));

//		System.out.println(map.get("foo").getValueOf().getClass().getName());
		assertNotNull(map.get("foo"));
		assertEquals("java.lang.Integer", map.get("foo").getValueOf().getClass().getName());
	}
	
	
	@Test
	public void testWrite() {
		new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", null);
			}
		})
	}
}
