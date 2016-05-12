package com.ajaxjs.javatools.jvm;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.ajaxjs.core.Reflect;

public class CompileString {
    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        String className = "CalculatorTest";
        StringSourceJavaObject so = new StringSourceJavaObject(  
        		"CalculatorTest",  
        		"public class CalculatorTest {"  
        				+ " public int multiply(int multiplicand, int multiplier) {"  
        				+ " System.out.println(multiplicand);"  
        				+ " System.out.println(multiplier);"  
        				+ " return multiplicand * multiplier;" + " }" + "}");  
        
        Iterable<? extends JavaFileObject> files = Arrays.asList(so);  
        Iterable<String> options =  Arrays.asList("-d", CompileString.class.getResource("/").getPath());  
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, files);  
    
		if (task.call()) {
			Class<?> clazz = Reflect.getClassByName(className);
			Object instance = Reflect.newInstanceByClassName(className);
			Method m = Reflect.getDeclaredMethod(clazz, "multiply", int.class, int.class);
			int result = (int)Reflect.executeMethod(instance, m, new Object[] { 3, 2 });
			System.out.println(result);
		}
        
    }
    
    static class StringSourceJavaObject extends SimpleJavaFileObject {
		private final String content;
		
		public StringSourceJavaObject(String name, String content) {
			super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.content = content;
		}
		
		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return content;
		}
	}
}
