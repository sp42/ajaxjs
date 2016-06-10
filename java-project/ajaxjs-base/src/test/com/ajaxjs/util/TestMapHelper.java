package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import org.junit.Test;

import static com.ajaxjs.util.MapHelper.*;

public class TestMapHelper {
	@Test
	public void testToJavaValue() {
		assertTrue((int) toJavaValue("123") == 123);
		assertTrue((boolean) toJavaValue("true") == true);
		assertTrue((boolean) toJavaValue("false") == false);
		assertTrue(toJavaValue("null") == null);
	}

	@Test
	public void testJoin() {

	}
}
