package test.com.ajaxjs.util.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Test;

import com.ajaxjs.util.io.FileUtil;

import static com.ajaxjs.util.io.FileUtil.*;

public class TestFileUtil {
	String dir = TestFileUtil.class.getResource("/").getPath();
	String fullpath = dir + File.separator + "bar.txt";

	@Test
	public void testCreateRead() {
		// create and update
		new FileUtil().setFilePath(fullpath).setOverwrite(true).setContent("hihi").save().close();
		// read
		String result = new FileUtil().setFilePath(fullpath).read().byteStream2stringStream().close().getContent();

		assertTrue(result.startsWith("hihi"));

		save(fullpath, "hihi2");
		assertTrue(openAsText(fullpath).startsWith("hihi2"));

		// delete
		new FileUtil().setFilePath(fullpath).delete();
	}

	@Test
	public void testGetFileName() {
		assertEquals("bar.java", getFileName("c:/foo/bar.java"));
	}

	@Test
	public void testGetFileSuffix() {
		assertEquals("java", getFileSuffix("c:/foo/bar.java"));
	}

	@Test
	public void testGetMime() {
		assertEquals("text/html", getMime("C:\\foo\\bar.htm"));
	}

	@Test
	public void testGetDirNameByDate() {
		assertTrue(getDirNameByDate().startsWith("\\" + Calendar.getInstance().get(Calendar.YEAR)));
	}

	@Test
	public void testCreateFile() {
		try {
			assertNotNull(createFile(fullpath, true));
			new FileUtil().setFilePath(fullpath).delete();
		} catch (IOException e) {
		}
	}
	
	@Test
	public void testZip() {
		toZip("C:\\temp\\ajaxjs-security", "C:\\temp\\dd.zip");
	}
}
