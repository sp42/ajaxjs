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

import com.ajaxjs.util.Util;
import com.ajaxjs.util.Reflect;
import com.ajaxjs.util.StringUtil;

/**
 * @author Frank
 *
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
	 * 初始化各对象及依赖
	 * 
	 * @param classes
	 *            扫描到的类集合
	 */
	public void init(Set<Class<?>> classes) {
		if (isInitialized || !Util.isNotNull(classes))
			return;

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

			if (annotation != null) {
				String beanName = annotation.value();
				
				// 实例化 bean，并将其保存，BEAN 名称作为键值
				beans.put(beanName, Reflect.newInstance(item));
				
				// 记录依赖关系
				for (Field field : item.getDeclaredFields()) {
					Resource fieldAnnotation = field.getAnnotation(Resource.class);
					
					if (fieldAnnotation != null) {// 获取依赖的 bean 的名称,如果为  null,则使用字段名称
						isFoundResource = true;
						String resourceName = fieldAnnotation.value();
						
						if (StringUtil.isEmptyString(resourceName))
							resourceName = field.getName();
						dependencies.put(beanName + "." + field.getName(), resourceName);
					}
				}
			}
		}
		
		if(!isFoundResource) throw new RuntimeException("没有 Resource！");
	}

	/**
	 * 扫描依赖关系并注入bean
	 */
	private void injectBeans() {
		for (String key : dependencies.keySet()) {
			String value = dependencies.get(key);// 依赖对象的值
			String[] split = key.split("\\.");// 数组第一个值表示 bean 对象名称,第二个值为字段属性名称

			Reflect.setProperty(beans.get(split[0]), split[1], beans.get(value));
		}
	}
}
