package test.com.ajaxjs.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import com.ajaxjs.util.FileUtil;
//import com.ajaxjs.test.util.timer.task_v2.TaskA;

public class TestFileUtil {
//	@Test
//	public void testRead() throws IOException {
//		String content = text.readFile(Util.getClassFolder_FilePath(TaskA.class, "taskconfig.xml"));
//		assertNotNull(content);
//		assertTrue(content.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
//	}

	@Test
	public void testWrite() throws IOException {
		String dir = TestFileUtil.class.getResource("/").getPath();
		String fullpath = dir + File.separator + "bar.txt";
		System.out.println(fullpath);
		FileUtil.save2file(fullpath, "bar");
		// open it
		// String d = Util.getClassFolder_FilePath(TestFile.class, "bar.txt");
		// System.out.println(d);
		String content = FileUtil.readFileAsText(fullpath);
		assertNotNull(content);

		FileUtil.delete(fullpath);
	}
}
