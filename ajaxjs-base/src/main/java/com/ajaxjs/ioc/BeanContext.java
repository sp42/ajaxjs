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

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.ioc.Bean;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;

/**
 * IOC 管理器，单例
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class BeanContext {
	/**
	 * 存放对象
	 */
	public static Map<String, Object> beans = new HashMap<>();



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
