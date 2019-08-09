package com.ajaxjs.util.io.resource;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import org.junit.Test;

import com.ajaxjs.util.resource.AbstractScanner;
import com.ajaxjs.util.resource.ScanClass;

public class TestResource {
	@Test
	public void testScanClass() {
		Set<Class<Object>> classes = ScanClass.scanClass("com.ajaxjs.util.io.resource");
		
		assertEquals(6, classes.size());
	}

	/**
	 * 用于查找 class 文件的过滤器
	 */
	private final static FileFilter anyFileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return true;
		}
	};

	class ScanAnything extends AbstractScanner<Object> {
		@Override
		public FileFilter getFileFilter() {
			return anyFileFilter;
		}

		@Override
		public void onFileAdding(Set<Object> target, File resourceFile, String packageJavaName) {
			target.add(resourceFile);
		}

		@Override
		public void onJarAdding(Set<Object> target, String resourcePath) {
			target.add(resourcePath);

		}
	}

	@Test
	public void testScanAnything() {
		Set<Object> anything = new ScanAnything().scan("com.ajaxjs.util.io");
		
		// 有内部类
		assertEquals(19, anything.size());

	}
}