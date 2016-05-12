package com.ajaxjs.test.util.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import com.ajaxjs.util.IO.text;
import com.ajaxjs.util.Util;
 

public class TestFile {
	@Test
	public void testRead() throws IOException{
		String content = text.readFile(Util.getClassFolder_FilePath(TaskA.class, "taskconfig.xml"));
		assertNotNull(content);
		assertTrue(content.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
	}
	@Test
	public void testWrite() throws IOException{
		String dir = TestFile.class.getResource("/").getPath();
		String fullpath = dir + File.separator + "bar.txt";
		System.out.println(fullpath);
		text.save2file(fullpath, "bar");
		// open it
//		String d = Util.getClassFolder_FilePath(TestFile.class, "bar.txt");
//		System.out.println(d);
		String content = text.readFile(fullpath);
		assertNotNull(content);
		
		Base.delete(fullpath);
	}
}
