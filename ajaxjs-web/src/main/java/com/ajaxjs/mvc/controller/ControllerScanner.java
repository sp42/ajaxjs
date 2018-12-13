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
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Supplier;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.util.TreeLinked;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.io.resource.ScanClass;
import com.ajaxjs.util.logger.LogHelper;

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
	 * URL 与 Action 之间的映射树。URL Mapping tree.
	 */
	public static Map<String, Action> urlMappingTree = new HashMap<>();

	/**
	 * 解析一个控制器。Parsing a Controller class.
	 * 
	 * @param clz 控制器类 a Controller class
	 */
	public static void add(Class<? extends IController> clz) {
		if (!testClass(clz))
			return;

		String topPath = getRootPath(clz);
		// LOGGER.info("控制器正在解析，This controller \"{0}\" is being parsing", topPath);

		Action action = null;

		if (topPath.contains("/")) {
			action = TreeLinked.findTreeByPath(urlMappingTree, TreeLinked.split2Queue(topPath), "", ControllerScanner::actionFactory, true);
		} else {
			if (urlMappingTree.containsKey(topPath)) {
				action = urlMappingTree.get(topPath);// already there is
			} else {
				action = new Action();
				action.path = topPath; // 需要吗？
				urlMappingTree.put(topPath, action);
			}
		}

		createControllerInstance(clz, action);
		parseSubPath(clz, action);// parse class methods or find out sub-path

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
	 * 
	 * @param path
	 * @return
	 */
	private static Action actionFactory(String path) {
		Action _action = new Action();
		_action.path = path.replaceAll(".$", "");
		return _action;
	}

	/**
	 * 
	 * @param clz 控制器类
	 * @param action
	 */
	private static void createControllerInstance(Class<? extends IController> clz, Action action) {
		if (clz.getAnnotation(Bean.class) != null) { // 如果有 ioc，则从容器中查找
			action.controller = BeanContext.getBeanByClass(clz);
			if (action.controller == null)
				LOGGER.warning("在 IOC 资源库中找不到该类 {0} 的实例，请检查该类是否已经加入了 IOC 扫描？  The IOC library not found that Controller, plz check if it added to the IOC scan.", clz.getName());
		} else {
			action.controller = ReflectUtil.newInstance(clz);// 保存的是 控制器 实例。
		}
	}

	/**
	 * 根据路径信息加入到 urlMapping。Check out all methods which has Path annotation, then
	 * add the urlMapping.
	 * 
	 * @param clz 控制器类
	 * @param action 父亲动作
	 */
	private static void parseSubPath(Class<? extends IController> clz, Action action) {
		for (Method method : clz.getMethods()) {
			Path subPath = method.getAnnotation(Path.class); // 看看这个控制器方法有木有 URL 路径的信息，若有，要处理

			if (subPath != null) {
				String subPathValue = subPath.value();
				subPathValue = subPathValue.replaceAll("^/", ""); // 一律不要前面的 /

				// add sub action starts from parent Node, not the top node
				if (action.children == null)
					action.children = new HashMap<>();

				Action subAction = TreeLinked.findTreeByPath(action.children, TreeLinked.split2Queue(subPathValue), action.path + "/", ControllerScanner::actionFactory, true);
				subAction.controller = action.controller; // the same controller cause the same class over there
				methodSend(method, subAction);
			} else {
				// this method is for class url
				methodSend(method, action); // 没有 Path 信息，就是属于类本身的方法（一般最多只有四个方法 GET/POST/PUT/DELETE）
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static Action find(String path) {
		Queue<String> queue = TreeLinked.split2Queue(path);
		Action action = TreeLinked.findTreeByPath(urlMappingTree, queue, "");

		if (action == null) { // for the controller which is set Path("/"), root controller
			queue = split2Queue2(path);
			action = TreeLinked.findTreeByPath(urlMappingTree, queue, "");
		}

		return action;
	}

	/**
	 * Covernt "aa/bb/cc" to queen.
	 * 
	 * @param path
	 * @return
	 */
	private static Queue<String> split2Queue2(String path) {
		String[] arr = path.split("/");

		if (arr.length == 1) {
			arr = new String[] { "", arr[0] }; // for the case of the root
		}

		return new LinkedList<>(Arrays.asList(arr));
	}

	/**
	 * HTTP 方法对号入座，什么方法就进入到什么属性中保存起来。
	 * 
	 * @param method 控制器方法
	 * @param action Action
	 */
	private static void methodSend(Method method, Action action) {
		if (isSend(GET.class, method, action, () -> action.getMethod)) {
			action.getMethod = method;
			action.getMethodController = action.controller;
		} else if (isSend(POST.class, method, action, () -> action.postMethod)) {
			action.postMethod = method;
			action.postMethodController = action.controller;
		} else if (isSend(PUT.class, method, action, () -> action.putMethod)) {
			action.putMethod = method;
			action.putMethodController = action.controller;
		} else if (isSend(DELETE.class, method, action, () -> action.deleteMethod)) {
			action.deleteMethod = method;
			action.deleteMethodController = action.controller;
		}
	}

	/**
	 * 检测是否已经登记的控制器
	 * 
	 * @param anClz
	 * @param method 控制器方法
	 * @param action
	 * @param getExistMethod
	 * @return
	 */
	private static <T extends Annotation> boolean isSend(Class<T> anClz, Method method, Action action, Supplier<Method> getExistMethod) {
		if (method.getAnnotation(anClz) != null) {
			if (getExistMethod.get() == null)
				return true; // if the Action is empty, allow to add a new method object
			else {
				String[] arr = anClz.toString().split("\\.");
				LOGGER.warning("控制器上的 {0} 的 {1} 方法业已登记，不接受重复登记！", action.path, arr[arr.length - 1]);
				return false;
			}
		} else {
			return false;
		}
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
