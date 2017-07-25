package test.com.ajaxjs.util;

import static org.junit.Assert.*;
import org.junit.Test;

import static com.ajaxjs.util.Value.*;

public class TestValue {
	@Test
	public void testTypeConvert() {
		assertTrue(12 == TypeConvert(12, Integer.class));
		assertTrue(TypeConvert(true, Boolean.class));
	}

	@Test
	public void testToJavaValue() {
		assertTrue((int) toJavaValue("123") == 123);
		assertTrue((boolean) toJavaValue("true") == true);
		assertTrue((boolean) toJavaValue("false") == false);
		assertTrue(toJavaValue("null") == null);
	}
	
	@Test
	public void testToBoolean() {
		assertTrue(toBoolean(true));
		assertTrue(toBoolean("true"));
		assertTrue(toBoolean("True"));
		
		assertTrue(!toBoolean("fAlse"));
		assertTrue(!toBoolean("null"));
		
		assertTrue(toBoolean("on"));
		assertTrue(toBoolean("yes"));
		assertTrue(toBoolean(1));
		assertTrue(toBoolean("1"));
	}
}
