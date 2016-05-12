package test;

import static org.junit.Assert.*;
import org.junit.*;

import com.ajaxjs.javatools.jvm.CompileUtil;
 
public class TestLang{
	private static String JAVA_SOURCE_FILE = "d:/DynamicObject.java";
	private static String JAVA_CLASS_NAME = "DynamicObject";
	private static final String BASEPATH = "d:/tmp";
	
	@Test
	public void testCompile(){
		CompileUtil.defaultCompile(BASEPATH);
		CompileUtil.compilePackage("d:/tmp/src", "d:/tmp/bin","d:/tmp/lib");
		assertNotNull("");
	}
}
