package com.ajaxjs.util.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import com.ajaxjs.util.ioc.EveryClass;

public class TestFileHelper {
	String dir = EveryClass.getResourcesFromClass(TestFileHelper.class, "");
	String fullpath = dir + File.separator + "bar.txt";

	@Test
	public void testCreateRead() {
		// create and update
		FileHelper.saveText(fullpath, "hihi");

		// read
		String result = FileHelper.openAsText(fullpath);

		assertTrue(result.startsWith("hihi"));

		FileHelper.saveText(fullpath, "hihi2");
		assertTrue(FileHelper.openAsText(fullpath).startsWith("hihi2"));

		// delete
		FileHelper.delete(fullpath);
	}

	@Test
	public void testGetFileName() {
		assertEquals("bar.java", FileHelper.getFileName("c:/foo/bar.java"));
	}

	@Test
	public void testGetFileSuffix() {
		assertEquals("java", FileHelper.getFileSuffix("c:/foo/bar.java"));
	}

	@Test
	public void testGetDirNameByDate() {
		assertTrue(FileHelper.getDirNameByDate().startsWith("\\" + Calendar.getInstance().get(Calendar.YEAR)));
	}

	@Test
	public void testCreateFile() {
		try {
			assertNotNull(FileHelper.createFile(fullpath, true));
			FileHelper.delete(fullpath);
		} catch (IOException e) {
		}
	}

	@Test
	public void testZip() {
		ZipHelper.zip("C:\\temp\\ajaxjs-security", "C:\\temp\\dd.zip");
	}
}
