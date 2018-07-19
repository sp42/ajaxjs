package com.ajaxjs.mvc.controller;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.io.resource.ScanClass;
import com.ajaxjs.util.io.resource.Scanner;
import com.ajaxjs.util.reflect.NewInstance;

/**
 * Inner class for collecting IController
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class IControllerScanner extends ScanClass {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onFileAdding(Set target, File resourceFile, String packageJavaName) {
		String className = getClassName(resourceFile, packageJavaName);
		Class<?> clazz = NewInstance.getClassByName(className);

		if (IController.class.isAssignableFrom(clazz)) {
			target.add(clazz);// 添加到集合中去
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onJarAdding(Set target, String resourcePath) {
		Class<?> clazz = NewInstance.getClassByName(resourcePath);

		if (IController.class.isAssignableFrom(clazz)) {
			target.add(clazz);// 添加到集合中去
		}
	}
	

	/**
	 * 扫描控制器
	 * 
	 * @param config
	 *            web.xml 中的配置，已经转为 Map
	 */
	protected static void scannController(Map<String, String> config) {
		if (config != null && config.get("controller") != null) {
			String str = config.get("controller");

			IControllerScanner scanner = new IControllerScanner(); // 定义一个扫描器，专门扫描 IController

			for (String packageName : StringUtil.split(str)) {
				Scanner scaner = new Scanner(scanner);
				@SuppressWarnings("unchecked")
				Set<Class<IController>> IControllers = (Set<Class<IController>>) scaner.scan(packageName);

				for (Class<IController> clz : IControllers)
					ControllerScanner.add(clz);
			}
		} else {
			System.out.println("web.xml 没有配置 MVC 过滤器或者 配置没有定义 controller");
		}
	}
}