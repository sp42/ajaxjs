package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestIoHelper {

	@Test
	public void testCreateFile() {
		assertNotNull(IoHelper.createFile("c:/temp", "test.txt", "test 你好"));
	}
	
	@Test
	public void testReadFile() {
		assertEquals("test 你好", IoHelper.readFile("c:/temp/test.txt"));
	}

}
