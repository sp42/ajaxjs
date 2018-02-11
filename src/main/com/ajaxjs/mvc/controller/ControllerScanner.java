package com.ajaxjs.mvc.controller;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.mvc.annotation.Controller;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.NewInstance;

public class ControllerScanner {
	private static final LogHelper LOGGER = LogHelper.getLog(ControllerScanner.class);

	public static Map<String, Action> urlMappingTree = new HashMap<>();

	public static void add(Class<? extends IController> clz) {
		testClass(clz);

		// the path in class always starts from top 1
		String topPath = clz.getAnnotation(Path.class).value();
		topPath = topPath.replaceAll("^/", ""); // remove the first / so that the array would be right length
		Action action = null;

		LOGGER.info("This controller \"{0}\" is being parsing", topPath);

		if (topPath.contains("/")) {
			action = findKey(urlMappingTree, split2Queue(topPath), "");
		} else {
			if(urlMappingTree.containsKey(topPath)) {
				// already there is 
				action = urlMappingTree.get(topPath);
			} else {
				action = new Action();
				urlMappingTree.put(topPath, action);
			}
		}
		
		//if(action.controller == null)
			action.controller = NewInstance.newInstance(clz);// 保存的是 控制器 实例。
		
		// parse class methods or find out sub-path
		parseSubPath(clz, action);
	}

	public static Action find(String path) {
		Queue<String> queue = split2Queue(path);
		return findKey(urlMappingTree, queue, "");
	}

	/**
	 * Covernt "aa/bb/cc" to queen.
	 * 
	 * @param path
	 * @return
	 */
	private static Queue<String> split2Queue(String path) {
		String[] arr = path.split("/");
		return new LinkedList<>(Arrays.asList(arr));
	}

	/**
	 * Check out all methods which has Path annotation, then add the urlMapping
	 * 
	 * @param clz
	 * @param action
	 */
	private static void parseSubPath(Class<? extends IController> clz, Action action) {
		for (Method method : clz.getMethods()) {
			Path subPath = method.getAnnotation(Path.class); // 看看这个控制器方法有木有 URL 路径的信息，若有，要处理
			if (subPath != null) {
				String subPathValue = subPath.value();
				subPathValue = subPathValue.replaceAll("^/", "");

				// add sub action starts from parent Node, not the top node
				if (action.children == null)
					action.children = new HashMap<>();

				Action subAction = findKey(action.children, split2Queue(subPathValue), action.path + ".");
				subAction.controller = action.controller; // the same controller cause the same class over there
				methodSend(method, subAction);
			} else {
				// this method is for class url
				methodSend(method, action);
			}
		}
	}

	/**
	 * HTTP 方法对号入座，什么方法就进入到什么属性中保存起来。
	 * 
	 * @param method
	 *            控制器方法
	 * @param action
	 *            ActionAndView
	 */
	private static void methodSend(Method method, Action action) {
		if (method.getAnnotation(GET.class) != null) {
			if (testIfEmpty(action.getMethod, action.path, "GET"))
				action.getMethod = method;
		} else if (method.getAnnotation(POST.class) != null) {
			if (testIfEmpty(action.postMethod, action.path, "POST"))
			action.postMethod = method;
		} else if (method.getAnnotation(PUT.class) != null) {
			if (testIfEmpty(action.putMethod, action.path, "PUT"))
			action.putMethod = method;
		} else if (method.getAnnotation(DELETE.class) != null) {
			if (testIfEmpty(action.deleteMethod, action.path, "DELETE"))
			action.deleteMethod = method;
		}
	}

	private static boolean testIfEmpty(Method method, String path, String httpMethod) {
		if (method == null)
			return true;
		else {
			LOGGER.info("控制器上的 {0} 的 {1} 方法业已登记，不接受重复登记！", path, method);
			return false;
		}
	}

	/**
	 * 
	 * @param urlMappingTree
	 *            A Tree contains all urlMappings
	 * @param queue
	 *            The queue of URL
	 * @param path
	 *            for remembering what findKey has travelled, here we don't use url,
	 *            because we want self-adding to match the url, if there is correct
	 * @return the Action that looking for, null if not found
	 */
	private static Action findKey(Map<String, Action> urlMappingTree, Queue<String> queue, String path) {
		while (!queue.isEmpty()) {
			String key = queue.poll(); // remove the first item in the queue and return it
			path += key + ".";

			Action action;
			if (urlMappingTree.containsKey(key)) {
				action = urlMappingTree.get(key);

				if (queue.isEmpty()) {
					return action;// found it!
				} else if (action.children != null) { // remains sub path to find out
					Action action2 = findKey(action.children, queue, path);
					if (action2 != null)
						return action2;
				} else if (action.children == null) {
					action.children = new HashMap<>();
					Action a2 = findKey(action.children, queue, path);
					if (a2 != null)
						return a2;
				} else {
					LOGGER.warning("happened if sth wrong.");
				}
			} else { // new value to be set
				action = new Action();
				action.path = path.replaceAll(".$", "");
				urlMappingTree.put(key, action);

				if (queue.isEmpty()) {
					return action;
				} else { // has more sub path
					action.children = new HashMap<>();
					Action a2 = findKey(action.children, queue, path);
					if (a2 != null)
						return a2;
				}
			}
		}

		return null;
	}

	public static void testClass(Class<? extends IController> clz) {
		if (clz.getAnnotation(Controller.class) == null) {// 获取注解对象
			LOGGER.warning("此非控制器！要我处理干甚！？This is NOT a Controller! 类：" + clz.getName());
			return;
		}

		// 总路径
		Path path = clz.getAnnotation(Path.class);
		if (path == null) {
			LOGGER.warning("不存在任何 Path 信息！No Path info!");
			return;
		}
	}
}
