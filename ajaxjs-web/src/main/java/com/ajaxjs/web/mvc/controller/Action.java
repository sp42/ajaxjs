/**
 * Copyright 2015 sp42 frank@ajaxjs.com Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.web.mvc.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.logger.LogHelper;

/**
 * A action = controller + methods
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Action {
	private static final LogHelper LOGGER = LogHelper.getLog(Action.class);

	/**
	 * 完整路径
	 */
	public String path;

	/**
	 * 下级路径集合
	 */
	public Map<String, Action> children;

	/**
	 * 控制器实例，方便反射时候调用（如果 HTTP 对应控制器没有，则读取这个）
	 */
	public IController controller;

	// 下面是不同 HTTP 方法的控制器
	public IController getMethodController;

	public IController postMethodController;

	public IController putMethodController;

	public IController deleteMethodController;

	/**
	 * 该路径的 get 请求时对应的控制器方法
	 */
	public Method getMethod;

	/**
	 * 该路径的 get 请求时对应的控制器方法
	 */
	public Method postMethod;

	/**
	 * 该路径的 put 请求时对应的控制器方法
	 */
	public Method putMethod;

	/**
	 * 该路径的 delete 请求时对应的控制器方法
	 */
	public Method deleteMethod;

	/**
	 * 创建控制器实例
	 * 
	 * @param clz 控制器类
	 */
	public void createControllerInstance(Class<? extends IController> clz) {
		if (clz.getAnnotation(Component.class) != null) { // 如果有 ioc，则从容器中查找
			controller = ComponentMgr.get(clz);

			if (controller == null)
				LOGGER.warning("在 IOC 资源库中找不到该类 {0} 的实例，请检查该类是否已经加入了 IOC 扫描？  The IOC library not found that Controller, plz check if it added to the IOC scan.", clz.getName());
		} else
			controller = ReflectUtil.newInstance(clz);// 保存的是 控制器 实例。
	}

	/**
	 * 根据路径信息加入到 urlMapping。Check out all methods which has Path annotation, then
	 * add the urlMapping.
	 */
	public void parseMethod() {
		Class<? extends IController> clz = controller.getClass();
		String topPath = null;

		for (Method method : clz.getMethods()) {
			Path subPath = method.getAnnotation(Path.class); // 看看这个控制器方法有木有 URL 路径的信息，若有，要处理

			if (subPath != null) {
				String subPathValue = subPath.value();

				if (subPathValue.startsWith("/")) { // 根目录开始
					subPathValue = subPathValue.replaceAll("^/", ""); // 一律不要前面的 /

					if (subPathValue.contains("{root}")) { // 顶部路径
						if (topPath == null)
							topPath = getRootPath(clz);

						subPathValue = subPathValue.replaceAll("\\{root\\}", topPath);
					}

					Action subAction = IController.findTreeByPath(IController.urlMappingTree, subPathValue, "", true);
					subAction.controller = controller;
					subAction.methodSend(method);

				} else {
					subPathValue = subPathValue.replaceAll("^/", ""); // 一律不要前面的 /

					// add sub action starts from parent Node, not the top node
					if (children == null)
						children = new HashMap<>();

					Action subAction = IController.findTreeByPath(children, subPathValue, path + "/", true);
					subAction.controller = controller; // the same controller cause the same class over there
					subAction.methodSend(method);
				}
			} else {
				// this method is for class url
				methodSend(method); // 没有 Path 信息，就是属于类本身的方法（一般最多只有四个方法 GET/POST/PUT/DELETE）
			}
		}
	}

	/**
	 * HTTP 方法对号入座，什么方法就进入到什么属性中保存起来。
	 * 
	 * @param method 控制器方法
	 */
	private void methodSend(Method method) {
		if (isSend(GET.class, method, getMethod)) {
			getMethod = method;
			getMethodController = controller;
		} else if (isSend(POST.class, method, postMethod)) {
			postMethod = method;
			postMethodController = controller;
		} else if (isSend(PUT.class, method, putMethod)) {
			putMethod = method;
			putMethodController = controller;
		} else if (isSend(DELETE.class, method, deleteMethod)) {
			deleteMethod = method;
			deleteMethodController = controller;
		}
	}

	/**
	 * 检测是否已经登记的控制器
	 * 
	 * @param anClz
	 * @param method         控制器方法
	 * @param action
	 * @param getExistMethod
	 * @return
	 */
	private <T extends Annotation> boolean isSend(Class<T> anClz, Method method, Method existMethod) {
		if (method.getAnnotation(anClz) != null) {
			if (existMethod == null)
				return true; // if the Action is empty, allow to add a new method object
			else {
				String[] arr = anClz.toString().split("\\.");
				LOGGER.warning("控制器上的[{0}]的 [{1}]方法已在[{2}]登记，不接受[{3}]的重复登记。", path, arr[arr.length - 1], method, existMethod);
				return false;
			}
		} else
			return false;
	}

	/**
	 * 根据 httpMethod 请求方法返回控制器类身上的方法。
	 * 
	 * @param method HTTP 请求的方法
	 * @return 控制器方法
	 */
	public Method getMethod(String method) {
		switch (method.toUpperCase()) {
		case "GET":
			return getMethod;
		case "POST":
			return postMethod;
		case "PUT":
			return putMethod;
		case "DELETE":
			return deleteMethod;
		}

		return null;
	}

	/**
	 * 根据 httpMethod 请求方法返回控制器
	 * 
	 * @param method HTTP 请求的方法
	 * @return 控制器
	 */
	public IController getController(String method) {
		switch (method.toUpperCase()) {
		case "GET":
			return getMethodController;
		case "POST":
			return postMethodController;
		case "PUT":
			return putMethodController;
		case "DELETE":
			return deleteMethodController;
		default:
			return controller;
		}
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
