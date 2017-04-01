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
package com.ajaxjs.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.ajaxjs.util.reflect.ReflectNewInstance;

/**
 * 扫描特定类型的类
 * @author frank
 *
 * @param <T>
 */
public class ClassScaner<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(ClassScaner.class);

	/**
	 * 目标类型
	 */
	private Class<T> targetClz;

	/**
	 * 
	 * @param targetClz
	 *            目标类型
	 */
	public ClassScaner(Class<T> targetClz) {
		this.targetClz = targetClz;
	}

	/**
	 * 类集合
	 */
	private List<Class<T>> classes;

	/**
	 * 扫描某个包下面类
	 * 
	 * @param packageName
	 *            包名
	 * @return 类集合
	 */
	public List<Class<T>> scan(String packageName) {
		LOGGER.info("扫描包名：" + packageName);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> dirs = null;
		classes = new ArrayList<>();// 新的一个容器
		String packageDirName = packageName.replace('.', '/').trim();// 将包名转换为文件路径

		try {
			dirs = classLoader.getResources(packageDirName);
		} catch (IOException e) {
			LOGGER.warning(e);
		}
		
		if (dirs == null)
			return null;

		while (dirs.hasMoreElements()) {
			URL url = dirs.nextElement();
			if ("file".equals(url.getProtocol())) {
				// 获取包的物理路径
				String filePath = StringUtil.urlDecode(url.getFile()).trim();
				// 以文件的方式扫描整个包下的文件 并添加到集合中
				findAndAddClassesInPackageByFile(packageName.trim(), filePath);
			}
		}

		return classes;
	}

	/**
	 * 
	 * @param packageName
	 *            包名
	 * @param filePath
	 *            包的物理路径
	 */
	@SuppressWarnings("unchecked")
	private void findAndAddClassesInPackageByFile(String packageName, String filePath) {
		File dir = new File(filePath); // 获取此包的目录 建立一个File
		if (!dir.exists() || !dir.isDirectory()) { // 如果不存在或者 也不是目录就直接返回
			LOGGER.warning("用户定义包名{0}下没有任何文件", packageName);
			return;
		}
		LOGGER.info("正在dirFiles类：" + packageName);

		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirFiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
				return (file.isDirectory()) || file.getName().endsWith(".class");
			}
		});
		

		for (File file : dirFiles) { // 循环所有文件
			if (file.isDirectory()) { // 如果是目录 则递归继续扫描
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath());
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				className = (packageName + '.' + className).trim();
				Class<?> clazz = ReflectNewInstance.getClassByName(className);

				LOGGER.info("正在检查类：" + className);
				// 添加到集合中去
				if (clazz != null && targetClz.isAssignableFrom(clazz)) {
					classes.add((Class<T>) clazz);
				}
			}
		}
	}

	public Class<T> getTargetClz() {
		return targetClz;
	}

	public void setTargetClz(Class<T> targetClz) {
		this.targetClz = targetClz;
	}

	public List<Class<T>> getClasses() {
		return classes;
	}

	public void setClasses(List<Class<T>> classes) {
		this.classes = classes;
	}
}