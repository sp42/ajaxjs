package com.ajaxjs.mvc.controller;

import java.io.File;
import java.util.Set;

import com.ajaxjs.util.io.resource.ScanClass;
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
}