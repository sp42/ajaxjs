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

import java.lang.reflect.Modifier;

import javax.ws.rs.Path;

import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;

/**
 * Servlet 启动时进行控制器扫描，解析控制器 Path 路径，然后将其登记起来。Scanner controllers at Servlet
 * starting up
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class ControllerScanner {
	private static final LogHelper LOGGER = LogHelper.getLog(ControllerScanner.class);

	@SuppressWarnings("unchecked")
	public void init() {
		for (Class<?> clz : ComponentMgr.clzs) {
			if (IController.class.isAssignableFrom(clz)) {
				add((Class<? extends IController>) clz);// 添加到集合中去
			}
		}
	}

	/**
	 * 解析一个控制器。Parsing a Controller class.
	 * 
	 * @param clz 控制器类 a Controller class
	 */
	public static void add(Class<? extends IController> clz) {
		if (Modifier.isAbstract(clz.getModifiers())) // 忽略抽象类
			return;

		String topPath = getRootPath(clz);
//		LOGGER.info("控制器正在解析，This controller \"{0}\" is being parsing", topPath);

		Action action = IController.findTreeByPath(IController.urlMappingTree, topPath, "", true);
		action.createControllerInstance(clz);
		action.parseMethod();

		// 会打印控制器的总路径信息，不会打印各个方法的路径，那太细了，日志也会相应地多
		LOGGER.info("控制器已登记成功！The controller \"{0}\" (\"/{1}\") was parsed and registered", clz.toString().replaceAll("class\\s", ""), topPath); // 控制器 {0} 所有路径（包括子路径）注册成功！
	}

	/**
	 * 获取控制器类的根目录设置
	 * 
	 * @param clz 控制器类
	 * @return 根目录
	 */
	public static String getRootPath(Class<? extends IController> clz) {
		Path path = clz.getAnnotation(Path.class); // 总路径

		if (path == null && !Modifier.isAbstract(clz.getModifiers())) { // 检查控制器类是否有 Path
			LOGGER.warning("控制器[{0}]不存在任何 Path 信息，控制器类应该至少设置一个 Path 注解。", clz.toString());
			return null;
		}

		String rootPath = path.value();// 控制器类上定义的 Path 注解总是从根目录开始的。 the path in class always starts from top 1
		return rootPath.replaceAll("^/", ""); // remove the first / so that the array would be right length
	}
}
