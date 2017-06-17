package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ajaxjs.util.Value;

public class TestMapHelper {
	@Test
	public void testToJavaValue() {
		assertTrue((int) Value.toJavaValue("123") == 123);
		assertTrue((boolean) Value.toJavaValue("true") == true);
		assertTrue((boolean) Value.toJavaValue("false") == false);
		assertTrue(Value.toJavaValue("null") == null);
	}

	@Test
	public void testJoin() {

	}
}
