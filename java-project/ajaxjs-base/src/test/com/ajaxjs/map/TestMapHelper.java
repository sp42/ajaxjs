package test.com.ajaxjs.map;

import static com.ajaxjs.map.MapHelper.*;
import static org.junit.Assert.*;

import org.junit.Test;

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
