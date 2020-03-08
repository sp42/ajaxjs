package com.ajaxjs.ioc;

import static com.ajaxjs.util.resource.AbstractScanner.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestScanner {
	@Test
	public void testFindClassByInterface() {
		assertNotNull(getResourcesFromClasspath(""));
		
		System.out.println(getResourcesFromClass(TestScanner.class, "TestScanner.class"));
//		assertEquals(1, list.size());
	}
}
