/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.util.ioc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 扫描器，参考了 Spring 实现
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class Scanner {
	private static final LogHelper LOGGER = LogHelper.getLog(Scanner.class);

	/**
	 * 类加载器
	 */
	private ClassLoader classLoader;

	/**
	 * 扫描包下面的类，返回包集合
	 * 
	 * @param packageName
	 *            包名称，请注意是包名称，不用写类全称，相等于文件夹
	 * @return 包集合
	 */
	public Set<Class<?>> scanPackage(String packageName) {
		return scanPackage(packageName, null);
	}

	/**
	 * 扫描包下面的类，返回包集合
	 * 
	 * @param packageName
	 *            包名称，请注意是包名称，不用写类全称，相等于文件夹
	 * @param _classes
	 *            支持多个包
	 * @return 包集合
	 */
	public Set<Class<?>> scanPackage(String packageName, Set<Class<?>> _classes) {
		classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> dirs = null;

		try {
			dirs = classLoader.getResources(packageName.replace('.', '/'));// 将包名转换为文件路径
		} catch (IOException e) {
			LOGGER.warning(e);
		}

		boolean recursive = true;// 是否循环搜索包
		Set<Class<?>> classes = _classes == null ? new LinkedHashSet<Class<?>>() : _classes;// 存放扫描到的类

		if (dirs != null) {
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				if ("file".equals(url.getProtocol())) {
					// 获取包的物理路径
					String filePath = StringUtil.urlDecode(url.getFile());

					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				}
			}

			return classes;
		}

		return null;
	}

	/**
	 * 以文件的形式来获取包下的所有 Class
	 * 
	 * @param packageName
	 *            包名
	 * @param packagePath
	 *            包的物理路径
	 * @param recursive
	 *            是否递归扫描
	 * @param classes
	 *            类集合
	 */
	private void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
			Set<Class<?>> classes) {
		File dir = new File(packagePath); // 获取此包的目录 建立一个File
		if (!dir.exists() || !dir.isDirectory()) { // 如果不存在或者 也不是目录就直接返回
			LOGGER.warning("用户定义包名 {0} 下没有任何文件", packageName);
			return;
		}

		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirFiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || file.getName().endsWith(".class");
			}
		});

		for (File file : dirFiles) { // 循环所有文件
			if (file.isDirectory()) { // 如果是目录 则递归继续扫描
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				Class<?> clazz = null;

				try {
					clazz = classLoader.loadClass(packageName + '.' + className);
					// 添加到集合中去
				} catch (ClassNotFoundException e) {
					LOGGER.warning("添加用户自定义视图类错误 找不到类 {0} 的.class文件", className);
				}

				if (clazz != null)
					classes.add(clazz);
			}
		}
	}
}
