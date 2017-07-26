package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import static com.ajaxjs.util.collection.CollectionUtil.*;

public class TestCollection {
	@Test
	public void testIsNotNull() {
		// Array
		assertTrue(!isNull(new Object[] { 1, 2 }));
		assertTrue(!isNull(new String[] { "1" }));
		assertTrue(isNull(new Object[] {}));

		Object[] arr = null;
		String[] strs = null;
		assertTrue(isNull(arr));
		assertTrue(isNull(strs));

		// List
		assertTrue(!isNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
			}
		}));

		assertTrue(isNull(new ArrayList<String>()));

		assertTrue(!isNull(new ArrayList<Object>() {
			private static final long serialVersionUID = 1L;
			{
				add(null);
			}
		}));

		// Map
		assertTrue(!isNull(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("foo", null);
			}
		}));

		assertTrue(isNull(new HashMap<String, Object>()));
	}

	@Test
	public void testArray() {
		String str = "1,2,3";
		int[] arr = new int[] { 1, 2, 3 };
		
		assertEquals(Arrays.toString(strArr2intArr(str, ",")), Arrays.toString(arr));
		assertEquals(Arrays.toString(int_arr2string_arr(arr)), Arrays.toString(new String[] { "1", "2", "3" }));
		assertEquals(Arrays.toString(stringList2arr(new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("1");
				add("2");
				add("3");
			}
		})), Arrays.toString(new String[] { "1", "2", "3" }));

	}
}
