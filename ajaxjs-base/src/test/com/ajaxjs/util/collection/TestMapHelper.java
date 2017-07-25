package test.com.ajaxjs.util.collection;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

 

public class TestMapHelper {
	@Test
	public void testIsEmptyString() {
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
