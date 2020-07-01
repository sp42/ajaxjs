package com.ajaxjs.util.ioc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.ajaxjs.Version;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

public class EveryClass {
	private static final LogHelper LOGGER = LogHelper.getLog(EveryClass.class);

	private Consumer<String> addResult;

	/**
	 * 用于查找 class 文件的过滤器
	 */
	private final static FileFilter fileFilter = file -> file.isDirectory() || file.getName().endsWith(".class");

	/**
	 * 扫描
	 * 
	 * @param packageName Java 包名
	 * @param addResult
	 */
	public void scan(String packageName, Consumer<String> addResult) {
		this.addResult = addResult;
		packageName = packageName.trim();

		String packageDir = packageName.replace('.', '/');
		Enumeration<URL> resources = getResources(packageDir);
		LOGGER.info("正在扫描包 [{0}]", packageDir);

		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();

			switch (url.getProtocol()) {
			case "file":
				String filePath = Encode.urlDecode(url.getPath());
				findInFile(filePath, packageName);
				break;
			case "jar":
			case "zip":
				findInJar(url, packageDir, packageName);
				break;
			}
		}
	}

	/**
	 * 获取指定目录的资源
	 * 
	 * @param packageDir 包全称
	 * @return 该目录下所有的资源
	 */
	private static Enumeration<URL> getResources(String packageDir) {
		Enumeration<URL> url = null;

		try {
			url = Thread.currentThread().getContextClassLoader().getResources(packageDir);
			Objects.requireNonNull(url, packageDir + "没有这个 Java 目录。");
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		return url;
	}

	/**
	 * 以文件的方式扫描整个包下的文件 并添加到集合中
	 * 
	 * @param packageName 包名
	 * @param filePath    包的物理路径
	 */
	private void findInFile(String filePath, String packageName) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			LOGGER.warning("包{0}下没有任何文件{1}", filePath, packageName);

			return;
		}

//		LOGGER.info("正在扫描包：{0}，该包下面的类正准备被扫描。", packageName);
		for (File file : dir.listFiles(fileFilter)) {
			if (file.isDirectory()) // 如果是目录 则递归继续扫描
				findInFile(file.getAbsolutePath(), packageName + "." + file.getName());
			else
				addResult.accept(getClassName(file, packageName));
		}
	}

	/**
	 * 扫描 jar 包里面的类
	 * 
	 * @param classes
	 * @param url
	 * @param packageDir
	 * @param packageName
	 */
	private void findInJar(URL url, String packageDir, String packageName) {
		JarFile jar = null;
		String fileUrl = url.getFile().replace("!/" + packageDir, "").replace("file:/", "");

		try {
			jar = new JarFile(new File(Version.isWindows ? fileUrl : "/" + fileUrl));
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		Enumeration<JarEntry> entries = jar.entries();

		while (entries.hasMoreElements()) {
			// 获取 jar 里的一个实体 可以是目录 和一些 jar包里的其他文件 如META-INF等文件
			JarEntry entry = entries.nextElement();
			String name = entry.getName();

			// 如果是以/开头的的话获取后面的字符串
			if (name.charAt(0) == '/')
				name = name.substring(1);

			// 如果前半部分和定义的包名相同
			if (name.startsWith(packageDir)) {
				int idx = name.lastIndexOf('/');
				if (idx != -1) {
					packageName = name.substring(0, idx).replace('/', '.'); // 如果以"/"结尾 是一个包，获取包名 把"/"替换成"."

					if (name.endsWith(".class") && !entry.isDirectory()) {
						String className = name.substring(packageName.length() + 1, name.length() - 6);// 去掉后面的".class"
																										// 获取真正的类名
						addResult.accept(packageName + '.' + className);
					}
				}
			}
		}
	}

	/**
	 * 获取 Classpath 根目录下的资源文件
	 * 
	 * @param resource 文件名称，输入空字符串这返回 Classpath 根目录
	 * @param isDecode 是否解码
	 * @return 所在工程路径+资源路径，找不到文件则返回 null
	 */
	public static String getResourcesFromClasspath(String resource, boolean isDecode) {
		URL url = EveryClass.class.getClassLoader().getResource(resource);
		return url2path(url, isDecode);
	}

	/**
	 * 获取当前类目录下的资源文件
	 * 
	 * @param clz      类引用
	 * @param resource 资源文件名
	 * @return 当前类的绝对路径，找不到文件则返回 null
	 */
	public static String getResourcesFromClass(Class<?> clz, String resource) {
		return getResourcesFromClass(clz, resource, true);
	}

	/**
	 * 获取当前类目录下的资源文件
	 * 
	 * @param clz      类引用
	 * @param resource 资源文件名
	 * @param isDecode 是否解码
	 * @return 当前类的绝对路径，找不到文件则返回 null
	 */
	public static String getResourcesFromClass(Class<?> clz, String resource, boolean isDecode) {
		return url2path(clz.getResource(resource), isDecode);
	}

	/**
	 * 获取 Classpath 根目录下的资源文件
	 * 
	 * @param resource 文件名称，输入空字符串这返回 Classpath 根目录
	 * @return 所在工程路径+资源路径，找不到文件则返回 null
	 */
	public static String getResourcesFromClasspath(String resource) {
		return getResourcesFromClasspath(resource, true);
	}

	/**
	 * url.getPath() 返回 /D:/project/a，需要转换一下
	 * 
	 * @param url
	 * @param isDecode 是否解码
	 * @return
	 */
	private static String url2path(URL url, boolean isDecode) {
		if (url == null)
			return null;

		String path;
		if (isDecode) {
			path = Encode.urlDecode(new File(url.getPath()).toString());
		} else {
			path = url.getPath();
			path = path.startsWith("/") ? path.substring(1) : path;
		}

		// path = path.replaceAll("file:\\", "");
		return path;
	}

	/**
	 * Java 类文件 去掉后面的 .class 只留下类名
	 * 
	 * @param file        Java 类文件
	 * @param packageName 包名称
	 * @return 类名
	 */
	public static String getClassName(File file, String packageName) {
		String clzName = file.getName().substring(0, file.getName().length() - 6);

		return packageName + '.' + clzName;
	}
}
