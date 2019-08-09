package com.ajaxjs.cms.utils.codegenerators;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.ajaxjs.util.resource.AbstractScanner;

public class ScanTpl extends AbstractScanner<Object> {
	private final static FileFilter fileFilter = new FileFilter() {
		@Override
		public boolean accept(File file) {
			return file.isDirectory() || file.getName().endsWith(".txt");
		}
	};

	@Override
	public FileFilter getFileFilter() {
		return fileFilter;
	}

	@Override
	public void onFileAdding(Set<Object> target, File resourceFile, String packageJavaName) {
		// target.add(resourceFile);
	}

	@Override
	public void onJarAdding(Set<Object> target, String resourcePath) {
		target.add(resourcePath);
	}

	public static void test() throws IOException {
		URL url = ScanTpl.class.getProtectionDomain().getCodeSource().getLocation();
		File file = new File(url.getFile()); // jar 包磁盘路径
		System.out.println(file.getCanonicalPath());
		JarFile jar = new JarFile(file);
		Enumeration<JarEntry> i = jar.entries();

		while (i.hasMoreElements()) {
			JarEntry e = i.nextElement();
			if (e.getName().startsWith("META-INF/asset/jsp/system/CodeGenerator/template")) {
				System.out.println(e.getName());
			}
		}

		jar.close();
	}
}
