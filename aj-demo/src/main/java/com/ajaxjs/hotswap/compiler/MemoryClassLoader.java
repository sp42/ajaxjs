package com.ajaxjs.hotswap.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Load class from byte[] which is compiled in memory.
 *
 * @author michael
 */
public class MemoryClassLoader extends URLClassLoader {

	Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

	public MemoryClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}

	public void addClassBytes(Map<String, byte[]> classBytes) {
		this.classBytes.putAll(classBytes);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] buf = classBytes.get(name);
		if (buf == null) {
			return super.findClass(name);
		}
		classBytes.remove(name);
		return defineClass(name, buf, 0, buf.length);
	}

	/**
	 * 直接编译一个类文件
	 *
	 * @param name
	 * @param classBytes
	 * @return
	 */
	public Class<?> defineClass(String name, byte[] classBytes) {
		return defineClass(name, classBytes, 0, classBytes.length);
	}
}
