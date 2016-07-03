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
package com.ajaxjs.mvc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.StringUtil;

public class AnnotationUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(AnnotationUtils.class);

	/**
	 * 
	 */
	public static Map<String, ActionAndView> controllers = new HashMap<>();

	public static void scan(Class<? extends IController> clz) {
		Controller controller = clz.getAnnotation(Controller.class);

		if (controller != null) {
			ActionAndView cInfo = new ActionAndView();
			cInfo.controller = Reflect.newInstance(clz);

			for (Method method : clz.getMethods()) {
				Path subPath = method.getAnnotation(Path.class);

				if (subPath != null) {
					String subPathValue = subPath.value();
					ActionAndView subPath_Info;

					// 有子路径
					if (cInfo.subPath.containsKey(subPathValue)) { // 已经有这个
																	// subPath
						subPath_Info = cInfo.subPath.get(subPathValue);
						methodSend(method, subPath_Info);
					} else {
						subPath_Info = new ActionAndView();
						methodSend(method, subPath_Info);

						cInfo.subPath.put(subPathValue, subPath_Info);
					}
				} else {
					// 类本身……
					methodSend(method, cInfo);
				}
			}

			// 总路径
			Path path = clz.getAnnotation(Path.class);
			if (path != null) {
				controllers.put(path.value(), cInfo); // save the controller
														// instance, path as key
				LOGGER.info(path.value() + " 登记路径成功！");
			} else {
				LOGGER.warning("No Path info!");
			}
		} else {
			LOGGER.warning("This is NOT a Controller!");
		}
	}

	private static void methodSend(Method method, ActionAndView cInfo) {
		if (method.getAnnotation(GET.class) != null) {
			cInfo.GET_method = method;
		} else if (method.getAnnotation(POST.class) != null) {
			cInfo.POST_method = method;
		} else if (method.getAnnotation(PUT.class) != null) {
			cInfo.PUT_method = method;
		} else if (method.getAnnotation(DELETE.class) != null) {
			cInfo.DELETE_method = method;
		}

	}
	


	/**
	 * 扫描某个包下面类
	 * @param packageName
	 * @return
	 */
	public static <T> List<Class<T>> scanController(String packageName, Class<T> targetClz) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> dirs = null;
		List<Class<T>> clz = new ArrayList<>();
		String packageDirName = packageName.replace('.', '/').trim();// 将包名转换为文件路径

		try {
			dirs = classLoader.getResources(packageDirName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (dirs == null)
			return null;

		while (dirs.hasMoreElements()) {
			URL url = dirs.nextElement();
			if ("file".equals(url.getProtocol())) {
				// 获取包的物理路径
				String filePath = StringUtil.urlDecode(url.getFile()).trim();
				// 以文件的方式扫描整个包下的文件 并添加到集合中
				findAndAddClassesInPackageByFile(packageName, filePath, clz, targetClz);
			}
		}

		return clz;
	}

	@SuppressWarnings("unchecked")
	private static <T> void findAndAddClassesInPackageByFile(String packageName, String filePath, List<Class<T>> clz, Class<T> targetClz) {
		File dir = new File(filePath); // 获取此包的目录 建立一个File
		if (!dir.exists() || !dir.isDirectory()) { // 如果不存在或者 也不是目录就直接返回
			System.err.println("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}

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
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), clz, targetClz);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				className = (packageName + '.' + className).trim();

				Class<?> clazz = Reflect.getClassByName(className);

				boolean isController = isController(clazz);
				// 添加到集合中去
				if (clazz != null && isController)
					clz.add((Class<T>) clazz);
			}
		}
	}

	private static boolean isController(Class<?> clazz) {
		for (Class<?> clz : clazz.getInterfaces()) {
			if (clz == IController.class)
				return true;
		}
		return false;
	}

}
