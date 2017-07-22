package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.ajaxjs.util.CollectionUtil;

public class TestCollection {
	@Test
	public void testIsNotNull() {
		// Array
		assertTrue(CollectionUtil.isNotNull(new Object[] { 1, 2 }));
		assertTrue(CollectionUtil.isNotNull(new String[] { "1" }));
		assertTrue(!CollectionUtil.isNotNull(new Object[] {}));

		Object[] arr = null;
		String[] strs = null;
		assertTrue(!CollectionUtil.isNotNull(arr));
		assertTrue(!CollectionUtil.isNotNull(strs));

		// List
		assertTrue(CollectionUtil.isNotNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
			}
		}));

		assertTrue(!CollectionUtil.isNotNull(new ArrayList<String>()));

		assertTrue(CollectionUtil.isNotNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(null);
			}
		}));

		// Map
		assertTrue(CollectionUtil.isNotNull(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", null);
			}
		}));
		assertTrue(!CollectionUtil.isNotNull(new HashMap<String, Object>()));
	}

//	@Test
//	public void testTypeConvert(){
//		assertTrue(12 == TypeConvert(12, Integer.class));
//		assertTrue(TypeConvert(true, Boolean.class));
//		assertTrue(toBoolean(true));
//		assertTrue(toInt(88) == 88);
//		Date now = new Date();
//		assertEquals(toDate(now), now);
//		assertEquals(to_String("foo"), "foo");
//		assertEquals(to_String("foo", true), "foo");
//		assertEquals(to_String("foo", false), "foo");
//		
//		assertTrue(isUUID(getUUID()));
//	}
	

}
