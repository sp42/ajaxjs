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
package com.ajaxjs.util.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.util.Value;

/**
 * Bean 工具集
 * @author sp42
 *
 */
public class BeanUtil extends Reflect {
	
	/**
	 * BEAN SETXXX
	 * 
	 * @param bean
	 *            JAVA Bean 对象，也可以是
	 * @param name
	 *            属性名称
	 * @param value
	 *            要设置的属性值
	 */
	public static void setProperty(Object bean, String name, Object value) {
		String setMethodName = "set" + firstLetterUpper(name);

		if (bean == null)
			throw new NullPointerException("执行：" + setMethodName + " 未发现类");
		if (value == null)
			throw new NullPointerException("执行：" + setMethodName + " 未发现参数 value");

		Class<?> clazz = bean.getClass(); 

		// 要把父类的也包括进来
		Method method = getDeclaredMethod(clazz, setMethodName, value);

		// 如果没找到，那就试试接口的……
		if (method == null)
			method = getDeclaredMethodByInterface(clazz, setMethodName, value);

		// 最终还是找不到
		if (method == null) 
			throw new NullPointerException(clazz.getName() + " 找不到目标方法！" + setMethodName);
		
		executeMethod(bean, method, value);
	}

	/**
	 * 将第一个字母大写
	 * 
	 * @param str
	 *            字符串
	 * @return 字符串
	 */
	public static String firstLetterUpper(String str) {
		// return str.substring(0, 1).toUpperCase() + str.substring(1); // 另外一种写法
		return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
	}

	/**
	 * 根据方法名称来截取属性名称，例如把 getter 的 getXxx() 转换为 xxx 的字段名
	 * @param methodName
	 *            方法名称
	 * @param action
	 *            set|get
	 * @return
	 */
	public static String getFieldName(String methodName, String action) {
		methodName = methodName.replace(action, "");
		return Character.toString(methodName.charAt(0)).toLowerCase() + methodName.substring(1);
	}
	
	/**
	 * Bean 转为 Map
	 * 
	 * @param bean
	 *            实体 bean 对象
	 * @return
	 */
	public static <T> Map<String, Object> bean2Map(T bean) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			
			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String key = property.getName();
				
				// 过滤 class 属性
				if (!key.equals("class")) {
					// 得到 property 对应的 getter 方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					map.put(key, value);
				}
			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
			
		return map;
	}

	/**
	 * map 转实体
	 * 
	 * @param map
	 *            原始数据
	 * @param clz
	 *            实体 bean 的类
	 * @return 实体 bean 对象
	 */
	public static <T> T map2Bean(Map<String, Object> map, Class<T> clz, boolean isTransform) {
		T bean = ReflectNewInstance.newInstance(clz);
		
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clz);
	
			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String key = property.getName();
	
				if (map.containsKey(key)) {
					Object value = map.get(key);
					
					Class<?> t = property.getPropertyType(); // Bean 值的类型，这是期望传入的类型，也就 setter 参数的类型
					if(isTransform && t != value.getClass())  // 类型相同，直接传入；类型不相同，开始转换
						value = Value.objectCast(value, t);
					
					Method setter = property.getWriteMethod();// 得到对应的 setter 方法
					setter.invoke(bean, value);
				}
			}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public static <T> T map2Bean(Map<String, Object> map, Class<T> clz) {
		return map2Bean(map, clz, false);
	}


}
