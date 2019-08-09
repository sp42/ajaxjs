package com.ajaxjs.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.util.io.FileHelper;

public class TestIoHelper {

	@Test
	public void testCreateFile() {
		assertNotNull(FileHelper.save("c:/temp", "test.txt", "test 你好"));
	}
	
	@Test
	public void testReadFile() {
		assertEquals("test 你好", FileHelper.readFile("c:/temp/test.txt"));
	}

}
