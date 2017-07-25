package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.ajaxjs.util.collection.CollectionUtil;

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
}
