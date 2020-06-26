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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.ioc.aop.Aop;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * IOC 管理器，单例
 * 
 * @author sp42 frank@ajaxjs.com
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
	public static <T> T getBean(Class<T> clz) {
		if (clz.getAnnotation(Bean.class) == null) {
			IllegalArgumentException e = new IllegalArgumentException(clz + " 这不是一个 ioc 的 bean。This is not a bean object that can be put into IOC.");
			LOGGER.warning(e);
			throw e;
		}

//		String name = clz.getAnnotation(Bean.class).value();
		String name = getBeanId(clz.getAnnotation(Bean.class), clz);
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

			String beanName = getBeanId(annotation, item);

			if (CommonUtil.isEmptyString(beanName))
				beanName = getBeanId(namedAnno, item);

			if (beansClz.containsKey("beanName"))
				LOGGER.warning("相同的 bean name 已经存在" + beanName);

			if (ReflectUtil.hasArgsCon(item)) {
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
					// 获取依赖的 bean 的名称,如果为 null, 则使用字段名称
					String dependenciObj_id = res == null ? field.getAnnotation(Named.class).value() : res.value();
					dependenciObj_id = parseId(dependenciObj_id);

					if (CommonUtil.isEmptyString(dependenciObj_id))
						dependenciObj_id = field.getName(); // 此时 bean 的 id 一定要与 fieldName 一致

					// bean id ＋ 变量名称 ＝ 依赖关系的 key。
					dependencies.put(beanName + "." + field.getName(), dependenciObj_id);
					// LOGGER.info("IOC 创建成功！ " + item);

					String setMethodName = "set" + ReflectUtil.firstLetterUpper(field.getName());
					Method setter = ReflectUtil.getMethod(item, setMethodName, field.getType());
					if (setter == null) {
					}
				}
			}
		}
	}

	/**
	 * 可以从 JSON 配置文件读取依赖对象。这时以 autoWire: 开头指向配置内容，内容即具体 Bean 的 id。
	 * 
	 * @param dependenciObj_id
	 * @return
	 */
	private static String parseId(String dependenciObj_id) {
		if (dependenciObj_id.startsWith("autoWire:")) {
			String str = dependenciObj_id.replaceFirst("autoWire:", "");
			String[] arr = str.split("\\|");
			String extendedId = ConfigService.getValueAsString(arr[0]);

			// 没有扩展，读取默认的
			return extendedId == null ? arr[1] : extendedId;
		}

		return dependenciObj_id;
	}

	/**
	 * 如果有 Named 注解则读取它的值，否则读取类本身的名称，转换为小写
	 * 
	 * @param name Name 注解
	 * @param clz  Bean 类
	 * @return 组件 Id
	 */
	private static String getBeanId(Named name, Class<?> clz) {
		if (name == null || CommonUtil.isEmptyString(name.value()))
			return clz.getSimpleName().toLowerCase();
		else
			return name.value();
	}

	/**
	 * 获取 Bean 的名称，如果没有则取类 SimpleName
	 * 
	 * @param annotation Bean 类注解
	 * @param clz        Bean 类
	 * @return Bean 名称
	 */
	private static String getBeanId(Bean annotation, Class<?> clz) {
		if (annotation == null)
			return null;
		else if (CommonUtil.isEmptyString(annotation.value()))
			return clz.getSimpleName();
		else
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

	/**
	 * 
	 * @param packageName
	 */
	public static void init(String packageName) {
		init(BeanLoader.scanClass(packageName));
	}

	/**
	 * 创建 Bean 实例
	 * 
	 * @param item       bena 的类
	 * @param annotation
	 * @return Bean 实例
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object getBeanInstance(Class<?> item, Bean annotation) {
		Object beanInstance = ReflectUtil.newInstance(item);

		if (annotation != null && !CommonUtil.isNull(annotation.aop())) {
//			LOGGER.info("需要 AOP 处理，类：" + item);

			for (Class<? extends Aop> clz : annotation.aop())
				beanInstance = ReflectUtil.newInstance(clz).bind(beanInstance);
		}

		return beanInstance;
	}

	/**
	 * 扫描依赖关系并注入bean
	 */
	public static void injectBeans() {
		LOGGER.info("扫描依赖关系并注入 bean.");

		dependencies.forEach((k, v) -> {
//			String value = dependencies.get(key);// 依赖对象的值
			String[] split = k.split("\\.");// 数组第一个值表示 bean 对象名称，第二个值为字段属性名称

			Object bean = beans.get(split[0]), argBean = beans.get(v);

			Objects.requireNonNull(bean, split[0] + "执行[" + split[1] + "]未发现类");

			if (argBean == null)
				LOGGER.warning("容器中找不到实例[{0}]。请确定是否为组件添加 @Bean 注解?", v);
			else
				ReflectUtil.setProperty(bean, split[1], argBean);
		});

		isInitialized = true;
	}

	/**
	 * 运行时修改注解
	 * 
	 * @param clazzToLookFor    需要修改的类
	 * @param annotationToAlter 注解类
	 * @param annotationValue   注解的实例，注解也是接口的一种，所以需要接口的实例
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

		beans.forEach((key, bean) -> {
			if (interfaceClz.isAssignableFrom(bean.getClass()))
				list.add((T) bean);
		});

		return list;
	}

	/**
	 * 根据类查找实例列表
	 * 
	 * @param <T> 目标类型
	 * @param clz 类引用
	 * @return 实例对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByClass(Class<T> clz) {
		List<T> list = new ArrayList<>();

		beans.forEach((key, bean) -> {
			if (clz.isInstance(bean))
				list.add((T) bean);
		});

		return list;
	}

	/**
	 * 根据类查找实例
	 * 
	 * @param <T> 目标类型
	 * @param clz 类引用
	 * @return 实例对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByClass(Class<T> clz) {
		for (String id : beans.keySet()) {
			Object bean = beans.get(id);

			if (clz.isInstance(bean))
				return (T) bean;
		}

		return null;
	}

	/**
	 * 扫描某个包下面的所有类，实例化保存到 map，供以后调用
	 * 
	 * @param pack     要扫描的包名
	 * @param giveName key 取名规则，可为 null
	 */
	public static void simplePut(String pack, Function<Class<?>, String> giveName) {
		Set<Class<Object>> set = BeanLoader.scanClass(pack);

		for (Class<Object> clz : set) {
			String name = clz.getName();

			if (clz.isPrimitive() || Modifier.isAbstract(clz.getModifiers()) || clz.isAnnotation() || clz.isInterface() || clz.isArray() || name.indexOf("$") != -1) {
			} else {
				if (giveName != null)
					name = giveName.apply(clz);

				try {
					beans.put(name, clz.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					// 忽略不能实例化的
				}
			}
		}
	}
}
