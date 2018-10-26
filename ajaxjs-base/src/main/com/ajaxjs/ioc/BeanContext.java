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
package com.ajaxjs.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.ajaxjs.keyvalue.BeanUtil;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.io.resource.ScanClass;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.NewInstance;

/**
 * IOC 管理器，单例
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class BeanContext {
	private static final LogHelper LOGGER = LogHelper.getLog(BeanContext.class);

	/**
	 * 是否已初始化
	 */
	private static boolean isInitialized = false;

	/**
	 * 存放对象
	 */
	public static Map<String, Object> beans = new HashMap<>();

	/**
	 * 存放对象类
	 */
	public static Map<String, Class<?>> beansClz = new HashMap<>();

	/**
	 * 记录依赖关系
	 */
	private static Map<String, String> dependencies = new HashMap<>();

	/**
	 * 获取对象
	 * 
	 * @param id 对象标识
	 * @return 对象
	 */
	public static Object getBean(String id) {
		return beans.get(id);
	}

	/**
	 * 按照类给出的 对象id（Bean 注解指定的 id） 查找实例
	 * 
	 * @param clz 含有 Bean 注解的类对象
	 * @return 该类的实例，如果没有返回 null
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBeanByClass(Class<T> clz) {
		if (clz.getAnnotation(Bean.class) == null) {
			IllegalArgumentException e = new IllegalArgumentException(clz + " 这不是一个 ioc 的 bean。This is not a bean object that can be put into IOC.");
			LOGGER.warning(e);
			throw e;
		}

		String name = clz.getAnnotation(Bean.class).value();
		Object obj = getBean(name);

		return obj == null ? null : (T) obj;
	}

	/**
	 * 扫描注解、创建 bean 对象、记录依赖关系
	 * 
	 * @param classes 扫描到的类集合
	 */
	public static void init(Set<Class<Object>> classes) {
		if (isInitialized)
			LOGGER.warning("IOC 已经初始化。IOC System is already initialized.");

		if (CommonUtil.isNull(classes)) {
			LOGGER.warning("IOC 传入的类为空！请检查包名是否正确。 Non classes passed.");
			return;
		}

		for (Class<Object> item : classes) {
			Bean annotation = item.getAnnotation(Bean.class); // 查找匹配的注解
			Named namedAnno = item.getAnnotation(Named.class);
			
			if (annotation == null && namedAnno == null)
				continue; // 不是 bean 啥都不用做

			// LOGGER.info("IOC 扫描：" + item);
			String beanName = getBeanId(annotation, item);
			
			if(CommonUtil.isEmptyString(beanName)) {
				beanName = getBeanId(namedAnno, item);
			}

			if (NewInstance.hasArgsCon(item)) {
				beansClz.put(beanName, item);
			} else {
				beans.put(beanName, getBeanInstance(item, annotation));// 实例化 bean，并将其保存，BEAN 名称作为键值
			}

			// 记录依赖关系
			for (Field field : item.getDeclaredFields()) {
				Resource res = field.getAnnotation(Resource.class);
				Inject inject = field.getAnnotation(Inject.class);
				
				if (inject == null && res == null)
					continue; // 没有要注入的字段，跳过
				else {
					/*
					 * 要查找哪一个 bean？就是说依赖啥对象？以什么为依据？我们说是那个 bean 的 id。首先你可以在 Resource
					 * 注解中指定，如果这觉得麻烦，可以不在注解指定，直接指定变量名即可（就算不通过注解指定，都可以利用 反射 获取字段名，作为依赖的凭据，效果一样）
					 */
					String dependenciObj_id = res == null  ? field.getAnnotation(Named.class).value() : res.value();// 获取依赖的 bean 的名称,如果为 null, 则使用字段名称
					
					if (CommonUtil.isEmptyString(dependenciObj_id))
						dependenciObj_id = field.getName(); // 此时 bean 的 id 一定要与 fieldName 一致
					
					// bean id ＋ 变量名称 ＝ 依赖关系的 key。
					dependencies.put(beanName + "." + field.getName(), dependenciObj_id);
					// LOGGER.info("IOC 创建成功！ " + item);
				}
			}
		}
	}

	private static String getBeanId(Named namedAnno, Class<?> clz) {
		if (namedAnno == null || CommonUtil.isEmptyString(namedAnno.value())) {
			return clz.getSimpleName().toLowerCase();
		} else
			return namedAnno.value();
	}

	/**
	 * 
	 * @param annotation
	 * @param clz
	 * @return
	 */
	private static String getBeanId(Bean annotation, Class<?> clz) {
		if (annotation == null)
			return null;
		else if(CommonUtil.isEmptyString(annotation.value())) {
			return clz.getSimpleName();
		} else
			return annotation.value();
	}

	/**
	 * Initialize the package.
	 * 
	 * @param packageNames The package name in Java.
	 */
	public static void init(String... packageNames) {
		for (String packageName : packageNames)
			init(packageName);
	}

	public static void init(String packageName) {
		init(ScanClass.scanClass(packageName));
	}

	/**
	 * 创建 Bean 实例
	 * 
	 * @param item bena 的类
	 * @param annotation
	 * @return Bean 实例
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object getBeanInstance(Class<?> item, Bean annotation) {
		Object beanInstance = NewInstance.newInstance(item);

		if (annotation != null && !CommonUtil.isNull(annotation.aop())) {
//			LOGGER.info("需要 AOP 处理，类：" + item);

			for (Class<? extends Aop> clz : annotation.aop())
				beanInstance = NewInstance.newInstance(clz).bind(beanInstance);
		}

		return beanInstance;
	}

	/**
	 * 扫描依赖关系并注入bean
	 */
	public static void injectBeans() {
		LOGGER.info("扫描依赖关系并注入 bean...");

		for (String key : dependencies.keySet()) {
			String value = dependencies.get(key);// 依赖对象的值
			String[] split = key.split("\\.");// 数组第一个值表示 bean 对象名称，第二个值为字段属性名称

			BeanUtil.setProperty(beans.get(split[0]), split[1], beans.get(value));
		}

		isInitialized = true;
	}

	/**
	 * 运行时修改注解
	 * 
	 * @param clazzToLookFor 需要修改的类
	 * @param annotationToAlter 注解类
	 * @param annotationValue 注解的实例，注解也是接口的一种，所以需要接口的实例
	 */
	@SuppressWarnings("unchecked")
	public static void alterAnnotationOn(Class<?> clazzToLookFor, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) {
		Map<Class<? extends Annotation>, Annotation> map = null;

		try {
			Field annotations = Class.class.getDeclaredField("annotations");
			annotations.setAccessible(true);
			map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(clazzToLookFor);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			LOGGER.warning(e);
		}

		if (map != null) {
			map.put(annotationToAlter, annotationValue);
			// map.put(Greet2.class, new DynamicGreetings2("KungFu Panda2"));
		}
	}

	/**
	 * 根据接口查找目标对象
	 * 
	 * @param interfaceClz 接口类
	 * @return 目标对象的集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findBeanByInterface(Class<T> interfaceClz) {
		List<T> list = new ArrayList<>();

		for (String key : beans.keySet()) {
			Object obj = beans.get(key);
			if (interfaceClz.isAssignableFrom(obj.getClass()))
				list.add((T) obj);
		}

		return list;
	}
}
