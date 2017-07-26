/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.util.ioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.util.reflect.BeanUtil;
import com.ajaxjs.util.reflect.ReflectNewInstance;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.collection.CollectionUtil;

/**
 * IOC 管理器，单例
 * 
 * @author Frank Cheung frank@ajaxjs.com
 */
public class BeanContext {
	/**
	 * 单例对象
	 */
	private static BeanContext me = new BeanContext();

	/**
	 * 是否已初始化
	 */
	private boolean isInitialized = false;

	/**
	 * 存放 bean
	 */
	private Map<String, Object> beans = new HashMap<>();

	/**
	 * 记录依赖关系
	 */
	private Map<String, String> dependencies = new HashMap<>();

	/**
	 * 获取实体对象
	 * 
	 * @return BeanContext 实例
	 */
	public static BeanContext me() {
		return me;
	}

	/**
	 * 获取 bean 对象
	 * 
	 * @param name
	 *            beanName
	 * @return bean 对象
	 */
	public Object getBean(String name) {
		return beans.get(name);
	}

	/**
	 * 
	 * @param clz
	 * @return
	 */
	public Object getBeanByClass(Class<?> clz) {
		if (!isIOC_Bean(clz))
			throw new NullPointerException(clz + " 这不是一个 ioc 的 bean。");
		String name = clz.getAnnotation(Bean.class).value();

		return beans.get(name);
	}

	/**
	 * 检查那个类是否有 Bean 注解
	 * @param clz 类
	 * @return true 表示为 Bean
	 */
	public static boolean isIOC_Bean(Class<?> clz) {
		return clz.getAnnotation(Bean.class) != null;
	}

	/**
	 * 初始化各对象及依赖
	 * 
	 * @param classes
	 *            扫描到的类集合
	 */
	public void init(Set<Class<?>> classes) {
		if (isInitialized || CollectionUtil.isNull(classes)) {
			System.out.println("IOC 传入的类为空！请检查包名是否正确");
			return;
		}

		createBeansAndScanDependencies(classes);// 创建 bean,扫描依赖关系
		injectBeans();// 注入依赖
		isInitialized = true;
	}

	/**
	 * 扫描注解,创建对象,记录依赖关系
	 * 
	 * @param classes
	 *            类集合
	 */
	private void createBeansAndScanDependencies(Set<Class<?>> classes) {
		Iterator<Class<?>> iterator = classes.iterator();
		boolean isFoundResource = false; // 是否找到了 Resource

		while (iterator.hasNext()) {
			Class<?> item = iterator.next();

			Bean annotation = item.getAnnotation(Bean.class); // 查找匹配的注解

			if (annotation == null)
				continue; // 不是 bean 啥都不用做

			String beanName = annotation.value();

			// 实例化 bean，并将其保存，BEAN 名称作为键值
			beans.put(beanName, ReflectNewInstance.newInstance(item));

			// 记录依赖关系
			for (Field field : item.getDeclaredFields()) {
				Resource resAnnot = field.getAnnotation(Resource.class);
				if (resAnnot == null)
					continue; // 没有要注入的字段，跳过

				isFoundResource = true;

				/*
				 * 要查找哪一个 bean？就是说依赖啥对象？以什么为依据？我们说是那个 bean 的 id。首先你可以在 Resource
				 * 注解中指定，如果这觉得麻烦，可以不在注解指定，直接指定变量名即可（就算不通过注解指定，都可以利用 反射
				 * 获取字段名，作为依赖的凭据，效果一样）
				 */
				String dependenciObj_id = resAnnot.value();// 获取依赖的 bean 的名称,如果为
															// null,则使用字段名称
				if (StringUtil.isEmptyString(dependenciObj_id))
					dependenciObj_id = field.getName(); // 此时 bean 的 id 一定要与
														// fieldName 一致

				// bean id ＋ 变量名称 ＝ 依赖关系的 key。
				dependencies.put(beanName + "." + field.getName(), dependenciObj_id);
			}
		}

		if (!isFoundResource)
			throw new RuntimeException("没有 Resource！一次注入都没有！！");
	}

	/**
	 * 扫描依赖关系并注入bean
	 */
	private void injectBeans() {
		for (String key : dependencies.keySet()) {
			String value = dependencies.get(key);// 依赖对象的值
			String[] split = key.split("\\.");// 数组第一个值表示 bean 对象名称,第二个值为字段属性名称

			BeanUtil.setProperty(beans.get(split[0]), split[1], beans.get(value));
		}
	}
}
