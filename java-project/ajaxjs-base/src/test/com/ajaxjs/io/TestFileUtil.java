package test.com.ajaxjs.io;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;

import com.ajaxjs.io.FileUtil;

public class TestFileUtil {
//	String txtfile = Util.getClassFolder_FilePath(TestFileUtil.class, "taskconfig.xml");
	
	String dir = TestFileUtil.class.getResource("/").getPath();
	String fullpath = dir + File.separator + "bar.txt";

	@Test
	public void testCreateRead() {
		// create and update
		new FileUtil().setFilePath(fullpath).setOverwrite(true).setContent("hihi").save().close();
		// read
		String result = new FileUtil().setFilePath(fullpath).read().byteStream2stringStream().close().getContent();
		
		System.out.println(result);
		assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		
		// delete
		new FileUtil().setFilePath(fullpath).delete();
	}
}
