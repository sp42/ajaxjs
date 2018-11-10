/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.io.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.ajaxjs.Version;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 资源访问器。扫描过程是递归的
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 */
public abstract class AbstractScanner<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractScanner.class);

	/**
	 * The non-repeat set of result.
	 */
	private Set<T> result = new LinkedHashSet<>();

	/**
	 * 文件筛选器
	 * 
	 * @return 文件筛选器
	 */
	abstract public FileFilter getFileFilter();

	/**
	 * Fire this function when resource found in file system.
	 * 
	 * @param target          The target collection
	 * @param resourceFile    The full path of resource
	 * @param packageJavaName The name of package in Java
	 */
	abstract public void onFileAdding(Set<T> target, File resourceFile, String packageJavaName);

	/**
	 * Fire this function when resource found in JAR file.
	 * 
	 * @param target       The target collection
	 * @param resourcePath The full path of resource
	 */
	abstract public void onJarAdding(Set<T> target, String resourcePath);

	/**
	 * 扫描
	 * 
	 * @param packageJavaName 包名
	 * @return 扫描结果
	 */
	public Set<T> scan(String packageJavaName) {
		packageJavaName = packageJavaName.trim();

		String packageDirName = packageJavaName.replace('.', '/');
		Enumeration<URL> resources = getResources(packageDirName);

		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();

			switch (url.getProtocol()) {
			case "file":
				String filePath = Encode.urlDecode(url.getPath());
				findInFile(filePath, packageJavaName);
				break;
			case "jar":
			case "zip":
				findInJar(url, packageDirName, packageJavaName);
				break;
			}
		}

		LOGGER.info("正在扫描包名：{0}，结果数量：{1}", packageDirName, result.size());
		return result;
	}

	/**
	 * 获取指定目录的资源
	 * 
	 * @param packageDirName 包全称
	 * @return 该目录下所有的资源
	 */
	private static Enumeration<URL> getResources(String packageDirName) {
		try {
			Enumeration<URL> url = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			if (url == null)
				throw new NullPointerException(packageDirName + "没有这个 Java 目录。");

			return url;
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 以文件的方式扫描整个包下的文件 并添加到集合中
	 * 
	 * @param packageName 包名
	 * @param filePath    包的物理路径
	 */
	private void findInFile(String filePath, String packageJavaName) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			LOGGER.warning("包{0}下没有任何文件{1}", filePath, packageJavaName);
			return;
		}

//		LOGGER.info("正在扫描包：{0}，该包下面的类正准备被扫描。", packageJavaName);

		for (File file : dir.listFiles(getFileFilter())) {
			if (file.isDirectory()) { // 如果是目录 则递归继续扫描
				findInFile(file.getAbsolutePath(), packageJavaName + "." + file.getName());
			} else {
				onFileAdding(result, file, packageJavaName);
			}
		}
	}

	/**
	 * 扫描 jar 包里面的类
	 * 
	 * @param classes
	 * @param url
	 * @param packageDirName
	 * @param packageJavaName
	 */
	private void findInJar(URL url, String packageDirName, String packageJavaName) {
		JarFile jar = null;
		String fileUrl = url.getFile().replace("!/" + packageDirName, "").replace("file:/", "");

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
			if (name.startsWith(packageDirName)) {
				int idx = name.lastIndexOf('/');
				if (idx != -1) {
					packageJavaName = name.substring(0, idx).replace('/', '.'); // 如果以"/"结尾 是一个包，获取包名 把"/"替换成"."

					if (name.endsWith(".class") && !entry.isDirectory()) {
						String className = name.substring(packageJavaName.length() + 1, name.length() - 6);// 去掉后面的".class"
																											// 获取真正的类名
						onJarAdding(result, packageJavaName + '.' + className);
					}
				}
			}
		}
	}

	/**
	 * 获取当前类所在的目录下的一个资源 Returns the filepath under this clazz. u can warp this path
	 * by new File.
	 * 
	 * @param cls              类
	 * @param resourceFileName 资源文件名
	 * @return 资源路径
	 */
	public static String getResourceFilePath(Class<?> cls, String resourceFileName) {
		return Encode.urlDecode(cls.getResource(resourceFileName).getPath());
	}

	/**
	 * Returns the filepath under this clazz. u can warp this path by new File.
	 * 
	 * @param clz Class you want to location
	 * @return the path of class where is
	 */
	public static String getResourcesByClass(Class<?> clz) {
		return clz.getResource("").getPath();
	}
}
