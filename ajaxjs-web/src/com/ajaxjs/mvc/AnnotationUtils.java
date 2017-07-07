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
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.ioc.BeanContext;
import com.ajaxjs.util.reflect.ReflectNewInstance;

/**
 * 扫描注解的工具类
 * 
 * @author frank
 *
 */
public class AnnotationUtils {
	private static final LogHelper LOGGER = LogHelper.getLog(AnnotationUtils.class);

	/**
	 * 收集所有的控制器于此
	 */
	public static Map<String, ActionAndView> controllers = new HashMap<>();

	/**
	 * 从指定的包里面扫描 IController 的功能（扫描路径在 web.xml 里配）
	 * @param clz
	 */
	public static void scan(Class<? extends IController> clz) {
		if (clz.getAnnotation(Controller.class) == null) {// 获取注解对象
			LOGGER.warning("此非控制器！要我处理干甚！？This is NOT a Controller!");
			return;
		} 
		
		// 总路径
		Path path = clz.getAnnotation(Path.class);
		if (path == null) {
			LOGGER.warning("不存在任何 Path 信息！No Path info!");
			return;
		} 
		
		if(controllers.containsKey(path.value())) {
			LOGGER.warning("重复的 URL Mapping: {0}，请检查代码的 控制器 {1} 是否重复？", path.value(), clz.getName());
			return;
		} 
		
		// 开始解析控制器……
		ActionAndView cInfo = new ActionAndView();

		// 保存的是 控制器 实例。
		if(BeanContext.isIOC_Bean(clz)) { // 如果有 ioc，则从容器中查找
			cInfo.controller = (IController)BeanContext.me().getBeanByClass(clz);
		} else {
			cInfo.controller = ReflectNewInstance.newInstance(clz);
		}
		
		for (Method method : clz.getMethods()) {
			Path subPath = method.getAnnotation(Path.class); // 看看这个控制器方法有木有 URL 路径的信息，若有，要处理
			
			if (subPath != null) { 
				String subPathValue = subPath.value();
				ActionAndView subPath_Info;
				
				// 有子路径
				if (cInfo.subPath.containsKey(subPathValue)) { // 已经有这个 subPath，那么就是增加其他 HTTP 方法的意思（GET/POST……）
					subPath_Info = cInfo.subPath.get(subPathValue);
					methodSend(method, subPath_Info);
				} else {
					subPath_Info = new ActionAndView();// 如果没有子路径则创建之
					methodSend(method, subPath_Info);
					cInfo.subPath.put(subPathValue, subPath_Info);// 保存这个子路径 ActionAndView

					LOGGER.info("子路径信息：{0}/{1}", path.value(), subPathValue);
				}
			} else {
				// 没有 Path 信息，就是属于类本身的方法（一般最多只有四个方法 GET/POST/PUT/DELETE）……
				methodSend(method, cInfo);
			}
		}
		
		controllers.put(path.value(), cInfo); /* 解析完毕，保存 ActionAndView （已经包含控制器了）到 hash。 save the controller instance, path as key */
		LOGGER.info("控制器 {0} 所有路径（包括子路径）注册成功！", path.value());
	}

	/**
	 * HTTP 方法对号入座，什么方法就进入到什么属性中保存起来。
	 * @param method
	 * @param cInfo
	 */
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
	 * @param targetClz
	 * @return
	 */
	public static <T> List<Class<T>> scanController(String packageName, Class<T> targetClz) {
		List<Class<T>> clz = new ArrayList<>();

		Enumeration<URL> dirs = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
				String filePath = StringUtil.urlDecode(url.getFile()).trim();// 获取包的物理路径
				findAndAddClassesInPackageByFile(packageName, filePath, clz, targetClz);// 以文件的方式扫描整个包下的文件 并添加到集合中
			}
		}

		return clz;
	}

	/**
	 * 
	 * @param packageName
	 * @param filePath
	 * @param clz
	 * @param targetClz
	 */
	@SuppressWarnings("unchecked")
	private static <T> void findAndAddClassesInPackageByFile(String packageName, String filePath, List<Class<T>> clz, Class<T> targetClz) {
		File dir = new File(filePath); // 获取此包的目录 建立一个File
		if (!dir.exists() || !dir.isDirectory()) { // 如果不存在或者 也不是目录就直接返回
			LOGGER.warning("用户定义包名 {0} 下没有任何文件", packageName);
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

				Class<?> clazz = ReflectNewInstance.getClassByName(className);

				boolean isController = isController(clazz);
				// 添加到集合中去
				if (clazz != null && isController)
					clz.add((Class<T>) clazz);
			}
		}
	}

	/**
	 * 判断传入的类是否一个控制器，是的话返回 true，否则为 false。
	 * @param clazz 类
	 * @return
	 */
	private static boolean isController(Class<?> clazz) {
		for (Class<?> clz : clazz.getInterfaces()) {
			if (clz == IController.class)
				return true;
		}
		return false;
	}
}
