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
package com.ajaxjs.util.resource;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

import com.ajaxjs.util.ReflectUtil;

/**
 * Speical for Java Class Scanning
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ScanClass<T> extends AbstractScanner<Class<T>> {

	/**
	 * 用于查找 class 文件的过滤器
	 */
	public final static FileFilter fileFilter = file -> file.isDirectory() || file.getName().endsWith(".class");

	@Override
	public FileFilter getFileFilter() {
		return fileFilter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onFileAdding(Set<Class<T>> target, File resource, String packageName) {
		Class<T> clazz = (Class<T>) ReflectUtil.getClassByName(getClassName(resource, packageName));
		target.add(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onJarAdding(Set<Class<T>> target, String resource) {
		Class<T> clazz = (Class<T>) ReflectUtil.getClassByName(resource);
		target.add(clazz);
	}

	/**
	 * 输入包名，获取所有的 classs
	 * 
	 * @param packageName 包名
	 * @return 结果
	 */
	public static Set<Class<Object>> scanClass(String packageName) {
		return new ScanClass<Object>().scan(packageName);
	}

	/**
	 * 输入多个包名，获取所有的 class。多个 set 可以用 addAll 合并之
	 * 
	 * @param packages 包名
	 * @return 结果
	 */
	public static Set<Class<Object>> scanClass(String... packages) {
		Set<Class<Object>> clzes = null;
		ScanClass<Object> scanner = new ScanClass<>();

		for (String packageJavaName : packages) {
			if (clzes == null) {
				clzes = scanner.scan(packageJavaName);
			} else {
				clzes.addAll(clzes);
			}
		}

		return clzes;
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
