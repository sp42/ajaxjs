package com.ajaxjs.javatools.jvm;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.ajaxjs.core.Util;

/**
 * Java 编程编译源代码
 * 
 * @author http://yuancihang.iteye.com/blog/858158
 */
public class CompileUtil {
	private static List<File> sourceList = new ArrayList<>(); // 源文件列表

	/**
	 * 
	 * @param basePath
	 */
	public static synchronized void defaultCompile(String basePath) {
		File f = new File(basePath);
		String sourcePath = f.getAbsolutePath() + File.separator + "src";
		String targetPath = f.getAbsolutePath() + File.separator + "bin";
		String libPath = f.getAbsolutePath() + File.separator + "lib";

		try {
			compilePackage(sourcePath, targetPath, libPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 编译包及其子包
	 * 
	 * @param sourcePath
	 *            String
	 * @param targetPath
	 *            String 编译后的类文件的输出文件夹
	 * @param libPath
	 *            String 放置第三方类库的文件夹
	 */
	public static synchronized void compilePackage(String sourcePath, String targetPath, String libPath) {
		sourceList.clear();
		compileSourceFile(getSourceList(sourcePath), targetPath, libPath);
	}

	/**
	 * 
	 * @param dirPath
	 * @return
	 */
	private static List<File> getSourceList(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new DefaultFilenameFilter("java"));

		for (File f : files) {
			if (f.isDirectory()) {// 如果是文件夹则迭代
				getSourceList(dirPath + File.separator + f.getName());
			} else {
				sourceList.add(f);
			}
		}

		return sourceList;
	}

	/**
	 * 编译单个Java源文件
	 * 
	 * @param fileName
	 *            String
	 * @param targetPath
	 *            String
	 * @param libPath
	 *            String
	 */
	public static void compileSourceFile(final String fileName,
			String targetPath, String libPath) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);

		try {
			Iterable<? extends JavaFileObject> sourcefiles = fileManager
					.getJavaFileObjects(fileName);
			compiler.getTask(null, fileManager, null,
					getCompileOptions(targetPath, libPath), null, sourcefiles)
					.call();
			fileManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param files
	 *            List<File>
	 * @param targetPath
	 *            String 目标文件夹
	 * @param libPath
	 *            String 放置第三方类库的文件夹
	 */
	private static void compileSourceFile(List<File> files, String targetPath, String libPath) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		try {
			Iterable<? extends JavaFileObject> sourcefiles = fileManager .getJavaFileObjectsFromFiles(files);
			compiler.getTask(null, fileManager, null, getCompileOptions(targetPath, libPath), null, sourcefiles) .call();
			fileManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置编译选项
	 * 
	 * @param targetPath
	 *            String
	 * @param libPath
	 *            String
	 * @return
	 */
	private static Iterable<String> getCompileOptions(String targetPath, String libPath) {
		List<String> options = new ArrayList<>();

		if (targetPath != null) {// 设置编译后生成的类文件的输出目录
			options.add("-d");
			options.add(targetPath);
		}

		if (libPath != null) { // 设置用到的第三方类库的目录
			options.add("-classpath");
			options.add(getLibStr(libPath));
		}
		return options;
	}

	/**
	 * 
	 * @param libPath
	 * @return
	 */
	private static String getLibStr(String libPath) {
		StringBuffer sb = new StringBuffer();
		File libDir = new File(libPath);
		File[] jarFiles = libDir.listFiles(new DefaultFilenameFilter("jar"));

		for (File f : jarFiles) {
			if (!f.isDirectory()) {
				sb.append(libPath + File.separator + f.getName() + ";"); // 多个jar文件用分号隔开
			}
		}
		return sb.substring(0, sb.lastIndexOf(";"));
	}

	/**
	 * 
	 * @author
	 * 
	 */
	static class DefaultFilenameFilter implements FilenameFilter {
		private boolean ignoreDir = false; // 是否忽略文件夹,默认不忽略

		private List<String> extList = new ArrayList<>();

		public DefaultFilenameFilter(final String ext) {
			this.extList.add("." + ext);
		}

		public DefaultFilenameFilter(final String... exts) {
			if (Util.isNotNull(exts)) {
				for (String ext : exts)
					this.extList.add("." + ext);
			}
		}

		public DefaultFilenameFilter(final String ext, boolean ignoreDir) {
			this.ignoreDir = ignoreDir;
			this.extList.add("." + ext);
		}

		public void addExt(final String ext) {
			this.extList.add("." + ext);
		}

		public boolean accept(File dir, String name) {
			if (ignoreDir) {
				return new File(dir + File.separator + name).isDirectory() ? false
						: endsWithExt(name);
			} else {
				return endsWithExt(name)
						|| new File(dir + File.separator + name).isDirectory();
			}
		}

		protected boolean endsWithExt(String name) {
			if (!Util.isNotNull(extList))
				return true;

			for (String ext : extList) {
				if (name.endsWith(ext))
					return true;
			}

			return false;
		}
	}
}
