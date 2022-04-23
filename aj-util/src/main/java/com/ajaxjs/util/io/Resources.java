/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.util.io;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.util.StringUtils;

/**
 * 资源工具类
 */
public class Resources {
	/**
	 * 获取 Classpath 根目录下的资源文件
	 * 
	 * @param resource 文件名称，输入空字符串这返回 Classpath 根目录
	 * @param isDecode 是否解码
	 * @return 所在工程路径+资源路径，找不到文件则返回 null
	 */
	public static String getResourcesFromClasspath(String resource, boolean isDecode) {
		URL url = Resources.class.getClassLoader().getResource(resource);
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
	 * @param resource 文件名称，输入空字符串这返回 Classpath 根目录。可以支持包目录，例如
	 *                 com\\ajaxjs\\newfile.txt
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
			path = StringUtils.uriDecode(new File(url.getPath()).toString(), StandardCharsets.UTF_8);
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
