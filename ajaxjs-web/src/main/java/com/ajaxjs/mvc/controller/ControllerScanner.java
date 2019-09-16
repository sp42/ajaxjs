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
package com.ajaxjs.mvc.controller;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Set;

import javax.ws.rs.Path;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.resource.ScanClass;

/**
 * Servlet 启动时进行控制器扫描，解析控制器 Path 路径，然后将其登记起来。Scanner controllers at Servlet
 * starting up
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class ControllerScanner extends ScanClass<IController> {
	private static final LogHelper LOGGER = LogHelper.getLog(ControllerScanner.class);

	@Override
	public void onFileAdding(Set<Class<IController>> target, File resourceFile, String packageJavaName) {
		String className = getClassName(resourceFile, packageJavaName);
		onJarAdding(target, className);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onJarAdding(Set<Class<IController>> target, String resourcePath) {
		Class<?> clazz = ReflectUtil.getClassByName(resourcePath);

		if (IController.class.isAssignableFrom(clazz)) {
			target.add((Class<IController>) clazz);// 添加到集合中去
		}
	}

	/**
	 * 解析一个控制器。Parsing a Controller class.
	 * 
	 * @param clz 控制器类 a Controller class
	 */
	public static void add(Class<? extends IController> clz) {
		if (!testClass(clz))
			return;

		String topPath = getRootPath(clz);
		 LOGGER.info("控制器正在解析，This controller \"{0}\" is being parsing", topPath);

		Action action = IController.findTreeByPath(IController.urlMappingTree, topPath, "", true);
		action.createControllerInstance(clz);
		action.parseMethod();

		// 会打印控制器的总路径信息，不会打印各个方法的路径，那太细了，日志也会相应地多
		LOGGER.info("控制器已登记成功！The controller \"{0}\" (\"/{1}\") was parsed and registered", clz.toString().replaceAll("class\\s", ""), topPath); // 控制器 {0} 所有路径（包括子路径）注册成功！
	}

	/**
	 * 检查控制器类是否有 Path 注解。Test a class if it can be parsed.
	 * 
	 * @param clz 应该是一个控制器类 Should be a IController.
	 * @return true 表示为是一个控制器类。 true if it's ok.
	 */
	private static boolean testClass(Class<? extends IController> clz) {
		if (Modifier.isAbstract(clz.getModifiers())) // 忽略抽象类
			return false;

		Path path = clz.getAnnotation(Path.class); // 总路径
		if (path == null && !Modifier.isAbstract(clz.getModifiers())) {
			LOGGER.warning("{0} 不存在任何 Path 信息！No Path info!", clz.toString());
			return false;
		}

		return true;
	}

	/**
	 * 获取控制器类的根目录设置
	 * 
	 * @param clz 控制器类
	 * @return 根目录
	 */
	private static String getRootPath(Class<? extends IController> clz) {
		Path a = clz.getAnnotation(Path.class);
		Objects.requireNonNull(a, "控制器类应该至少设置一个 Path 注解。");
		String rootPath = a.value();// 控制器类上定义的 Path 注解总是从根目录开始的。 the path in class always starts from top 1
		
		return rootPath.replaceAll("^/", ""); // remove the first / so that the array would be right length
	}
	
	/**
	 * 扫描控制器
	 * 
	 * @param config web.xml 中的配置，已经转为 Map
	 */
	public static void scannController(String config) {
		ControllerScanner scanner;// 定义一个扫描器，专门扫描 IController

		for (String packageName : CommonUtil.split(config)) {
			scanner = new ControllerScanner();
			Set<Class<IController>> IControllers = scanner.scan(packageName);

			for (Class<IController> clz : IControllers)
				add(clz);
		}
	}
}
