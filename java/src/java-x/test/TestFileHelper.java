package test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.*;

import com.ajaxjs.javatools.file.FileHelper;
import com.ajaxjs.javatools.file.FileHelper.FileChangeListener;
import com.ajaxjs.javatools.file.FileHelper.GetSize;

 
public class TestFileHelper{

	@Test
	public void testCopyToDirectory(){
		FileHelper.copyToDirectory(new File("D:\\myPicture"), new File("E:\\strategy"));
		assertNotNull("");
	}
	@Test
	public void testAppendToFile(){
		FileHelper.appendToFile("c:\\temp\\test.txt", "foo"); 
		assertNotNull("");
	}
	
	@Test
	public void testRmDirectory(){
		FileHelper.rmDirectory(new File("D:\\myPicture")); 
		assertNotNull("");
	}
	
	@Test
	public void testGetSize(){
		GetSize fd = new GetSize();  
        double all = fd.getSize(new File("f:\\电影"));  
        System.out.println("ALL:  " + all + "MB");  
		assertNotNull(all);
	}
	
	@Test
	public void testFileChangeMonitor(){
		FileHelper.monit("c:\\temp", new FileChangeListener() {  
			int i = 0;
			public void fileChanged(File file) {  
				i++;
				String fileName = file.getName();
				// do sth.. 
				System.out.println(fileName);
			}  
		});
	}
}
