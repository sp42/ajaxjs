package com.ajaxjs.mvc.controller;

import java.io.File;
import java.util.Set;

import com.ajaxjs.io.resource.ScanClass;
import com.ajaxjs.util.reflect.NewInstance;

public class IControllerScanner extends ScanClass<IController> {
	@SuppressWarnings("unchecked")
	@Override
	public void onFileAdding(Set<Class<IController>> target, File resourceFile, String packageJavaName) {
		String className = getClassName(resourceFile, packageJavaName);
		Class<?> clazz = NewInstance.getClassByName(className);

		if (IController.class.isAssignableFrom(clazz)) {
			target.add((Class<IController>) clazz);// 添加到集合中去
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onJarAdding(Set<Class<IController>> target, String resourcePath) {
		Class<?> clazz = NewInstance.getClassByName(resourcePath);

		if (IController.class.isAssignableFrom(clazz)) {
			target.add((Class<IController>)clazz);// 添加到集合中去
		}
	}

}