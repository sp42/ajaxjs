package com.util.java_compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Springboot动态生成一个项目中没有的类（class对象）
 * 
 * 这两天新接到一个需求，是这样。从页面上文本写一个拦截器，然后上传这个拦截器的源码，生成对象并调用对象的方法
 * https://blog.csdn.net/CodingCreator/article/details/104353667
 * 
 * @author: Bobby
 * @create: 2020-02-11 15:29
 * @description:动态执行
 **/
public class DynamicLoader {

	/**
	 * auto fill in the java-name with code, return null if cannot find the public
	 * class
	 *
	 * @param javaSrc source code string
	 * @return return the Map, the KEY means ClassName, the VALUE means bytecode.
	 */
	public static Map<String, byte[]> compile(String javaSrc) {
		Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
		Matcher matcher = pattern.matcher(javaSrc);
		if (matcher.find())
			return compile(matcher.group(1) + ".java", javaSrc);

		return null;
	}

	public static Map<String, byte[]> compile(String javaName, String javaSrc) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);

		try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
			JavaFileObject javaFileObject = manager.makeStringSource(javaName, javaSrc);
			JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));

			if (task.call())
				return manager.getClassBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}