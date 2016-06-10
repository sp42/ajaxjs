package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

import static com.ajaxjs.util.Util.*;

public class TestUtil {
	@Test
	public void testIsNotNull() {
		// Array
		assertTrue(isNotNull(new Object[] { 1, 2 }));
		assertTrue(isNotNull(new String[] { "1" }));
		assertTrue(!isNotNull(new Object[] {}));

		Object[] arr = null;
		String[] strs = null;
		assertTrue(!isNotNull(arr));
		assertTrue(!isNotNull(strs));

		// List
		assertTrue(isNotNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
			}
		}));

		assertTrue(!isNotNull(new ArrayList<String>()));

		assertTrue(isNotNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(null);
			}
		}));

		// Map
		assertTrue(isNotNull(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", null);
			}
		}));
		assertTrue(!isNotNull(new HashMap<String, Object>()));
	}

	@Test
	public void testTypeConvert(){
		assertTrue(12 == TypeConvert(12, Integer.class));
		assertTrue(TypeConvert(true, Boolean.class));
		assertTrue(toBoolean(true));
		assertTrue(toInt(88) == 88);
		Date now = new Date();
		assertEquals(toDate(now), now);
		assertEquals(to_String("foo"), "foo");
		assertEquals(to_String("foo", true), "foo");
		assertEquals(to_String("foo", false), "foo");
		
		assertTrue(isUUID(getUUID()));
	}
	
	@Test
	public void testMisc(){
		assertEquals(base64Decode(base64Encode("foo")), "foo");
		assertEquals(md5("123123"), "4297F44B13955235245B2497399D7A93");
		System.out.println(passwordGenerator(8));
	}
}
