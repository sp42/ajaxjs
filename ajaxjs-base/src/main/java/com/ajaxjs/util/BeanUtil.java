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
package com.ajaxjs.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.js.JsonHelper;

/**
 * 
 * 通过 Introspector
 * 
 * Bean 工具集
 * 
 * @author Sp42 frank@ajaxjs.com
 */
public class BeanUtil extends ReflectUtil {
	/**
	 * BEAN SETXXX
	 * 
	 * @param bean JAVA Bean 对象，也可以是
	 * @param name 属性名称
	 * @param value 要设置的属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		String setMethodName = "set" + firstLetterUpper(name);

		Objects.requireNonNull(bean, bean + "执行：" + setMethodName + " 未发现类");
		Objects.requireNonNull(value, bean + "执行：" + setMethodName + " 未发现参数 value");

		Class<?> clazz = bean.getClass();

		// 要把参数父类的也包括进来
		Method method = getMethodByUpCastingSearch(clazz, setMethodName, value);

		// 如果没找到，那就试试接口的……
		if (method == null)
			method = getDeclaredMethodByInterface(clazz, setMethodName, value);

		// 如果没找到，那忽略参数类型，只要匹配方法名称即可。这会发生在：由于被注入的对象有可能经过了 AOP 的动态代理，所以不能通过上述逻辑找到正确的方法
		if (method == null) {
			method = getSuperClassDeclaredMethod(clazz, setMethodName);
		}

		// 最终还是找不到
		Objects.requireNonNull(method, clazz.getName() + " 找不到目标方法！" + setMethodName);

		executeMethod(bean, method, value);
	}

	/**
	 * 将第一个字母大写
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String firstLetterUpper(String str) {
		// return str.substring(0, 1).toUpperCase() + str.substring(1); // 另外一种写法
		return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
	}

	/**
	 * 根据方法名称来截取属性名称，例如把 getter 的 getXxx() 转换为 xxx 的字段名
	 * 
	 * @param methodName 方法名称
	 * @param action set|get
	 * @return 属性名称
	 */
	public static String getFieldName(String methodName, String action) {
		methodName = methodName.replace(action, "");
		return Character.toString(methodName.charAt(0)).toLowerCase() + methodName.substring(1);
	}

	@FunctionalInterface
	public static interface EachFieldArg {
		public void item(String key, Object value, PropertyDescriptor property);
	}

	/**
	 * 
	 * @param bean
	 * @param fn
	 */
	static void eachField(Object bean, EachFieldArg fn) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String key = property.getName();
				// 得到 property 对应的 getter 方法
				Method getter = property.getReadMethod();
				Object value = getter.invoke(bean);

				fn.item(key, value, property);
			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bean 转为 Map
	 * 
	 * @param bean 实体 bean 对象
	 * @return Map 对象
	 */
	public static <T> Map<String, Object> bean2Map(T bean) {
		Map<String, Object> map = new HashMap<>();

		eachField(bean, (k, v, property) -> {
			if (!k.equals("class")) // 过滤 class 属性
				map.put(k, v);
		});

		return map;
	}

	/**
	 * map 转实体 产品
	 * 
	 * @param map 原始数据
	 * @param clz 实体 bean 的类
	 * @param isTransform 是否尝试转换值
	 * @return 实体 bean 对象
	 */
	public static <T> T map2Bean(Map<String, Object> map, Class<T> clz, boolean isTransform) {
		T bean = newInstance(clz);

		eachField(bean, (key, v, property) -> {
			try {
				if (map.containsKey(key)) {
					Object value = map.get(key);

					// null 是不会传入 bean 的
					if (value != null) {
						Class<?> t = property.getPropertyType(); // Bean 值的类型，这是期望传入的类型，也就 setter 参数的类型

						if (isTransform && value != null && t != value.getClass()) { // 类型相同，直接传入；类型不相同，开始转换
							value = MappingValue.objectCast(value, t);
						}

						property.getWriteMethod().invoke(bean, value);
					}
				}

				// 子对象
				for (String mKey : map.keySet()) {
					if (mKey.contains(key + '_')) {
						Method getter = property.getReadMethod(), setter = property.getWriteMethod();// 得到对应的 setter 方法

						Object subBean = getter.invoke(bean);
						String subBeanKey = mKey.replaceAll(key + '_', "");

						if (subBean != null) {// 已有子 bean
							if (map.get(mKey) != null) // null 值不用处理
								setProperty(subBean, subBeanKey, map.get(mKey));
						} else { // map2bean
							Map<String, Object> subMap = new HashMap<>();
							subMap.put(subBeanKey, map.get(mKey));
							subBean = map2Bean(subMap, setter.getParameterTypes()[0], isTransform);
							setter.invoke(bean, subBean); // 保存新建的 bean
						}
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});

		return bean;
	}

	/**
	 * map 转实体
	 * 
	 * @param map 原始数据
	 * @param clz 实体 bean 的类
	 * @return 实体 bean 对象
	 */
	public static <T> T map2Bean(Map<String, Object> map, Class<T> clz) {
		return map2Bean(map, clz, false);
	}
	
	/**
	 * JSON 字符串转换为 Bean 对象
	 * 
	 * @param json JSON 字符串
	 * @param clz Bean 对象类引用
	 * @return Bean 对象
	 */
	public static <T> T json2bean(String json, Class<T> clz) {
		Map<String, Object> map = JsonHelper.parseMap(json);
		return map2Bean(map, clz, true);
	}

	/**
	 * 传入任意一个 object 对象生成一个指定规格的字符串
	 * 
	 * @param object 任意对象
	 * @return String
	 */
	public static String objectToJson(Object object) {
		StringBuilder json = new StringBuilder();

		if (object == null) {
			json.append("\"\"");
		} else if (object instanceof String || object instanceof Integer || object instanceof Double) {
			json.append("\"").append(object.toString()).append("\"");
		} 

		return json.toString();
	}
}