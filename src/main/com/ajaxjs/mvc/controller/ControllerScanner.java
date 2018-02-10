package com.ajaxjs.mvc.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.mvc.annotation.Controller;
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
		topPath = topPath.replaceAll("^/", "");
		if (topPath.contains("/")) {
			LOGGER.info("多项 url");
			String[] arr = topPath.split("/");
			Queue<String> queue = new LinkedList<>(Arrays.asList(arr));

			Action action = findKey(urlMappingTree, queue, "");
			action.controller = NewInstance.newInstance(clz);// 保存的是 控制器 实例。
			// TODO parse class methods
			// TODO parse subpath

		} else {
			urlMappingTree.put(topPath, new Action());
		}
	}

	private static Action findKey(Map<String, Action> urlMappingTree, Queue<String> queue, String path) {
		while (!queue.isEmpty()) {
			String key = queue.poll();

			path += key + ".";
			if (urlMappingTree.containsKey(key)) {
				Action action = urlMappingTree.get(key);
				
				if (queue.isEmpty()) {
					return action;// found it!
				} else if (action.children != null) { // remains sub path to find out
					for (String urlMappingTree2 : action.children.keySet()) {
						Action action2 = findKey(urlMappingTree2, queue, path);
						if (action2 != null)
							return action2;
					}
				} else if (urlMappingTree.get(key).children == null) {
					Map<String, Action> urlMappingTree2 = new HashMap<>();
					
					action.children = new ArrayList<>();
					action.children.add(urlMappingTree2);
					
					Action a2 = findKey(urlMappingTree2, queue, path);
					if(a2 != null)
						return a2;
				} else {
					LOGGER.warning("happened if sth wrong.");
				}
			} else { // new value to be set
				Action action = new Action();
				action.path = path.replaceAll(".$", "");
				urlMappingTree.put(key, action);

				if (queue.isEmpty()) {
					return action;
				} else { // has more sub path
					Map<String, Action> urlMappingTree2 = new HashMap<>();
					action.children = new ArrayList<>();
					action.children.add(urlMappingTree2);

					Action a2 = findKey(urlMappingTree2, queue, path);
					if(a2 != null)
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
