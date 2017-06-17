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
package com.ajaxjs.util.map;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.reflect.BeanUtil;
import com.ajaxjs.util.reflect.Reflect;
import com.ajaxjs.util.reflect.ReflectNewInstance;

/**
 * 将 map 数据通过反射保存到 pojo（bean） 中。
 * 
 * @author frank
 *
 * @param <T>
 *            实体类型
 */
public class Map2Pojo<T> extends BeanUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(Map2Pojo.class);
	
	/**
	 * 实体类型类对象
	 */
	private Class<T> pojoClz;

	/**
	 * 用于数组的分隔符
	 */
	private char diver = ',';
	
	/**
	 * 
	 * @param pojoClz
	 *            实体类型类对象
	 */
	public Map2Pojo(Class<T> pojoClz) {
		this.pojoClz = pojoClz;
	} 
	
	/**
	 * 把单个原始数据 map 转换为单个实体
	 * 
	 * @param map
	 *            原始数据
	 * @param pojo
	 *            实体
	 * @param fields
	 *            反射出来的字段信息
	 */
	private T map2pojo(Map<String, Object> map, List<Field> fields) {
		T pojo = ReflectNewInstance.newInstance(pojoClz);
		if(pojo == null) return null;

		for (Field f : fields) {
			String key = f.getName(); // 字段名称
			Class<?> t = f.getType(); // 字段期望的类型
			Object value = map.get(key);

			if (value != null) {
//				System.out.println(key + ":" + map.get(key).getClass().getName());

				String methodName = "set" + firstLetterUpper(key);

				if (t == boolean.class) {
//					System.out.println("methodName：：：：：" + methodName);
					// 布尔型
					methodName = key.replace("is", "");
					methodName = "set" + firstLetterUpper(methodName);
//					System.out.println("methodName：：：：：" + "set" + Reflect.firstLetterUpper(methodName));
					
					if(value instanceof String) {
						value = (String)value;
						if(value.equals("yes") || value.equals("true") || value.equals("1")) {
							value = true;
						}
						
						if(value.equals("no") || value.equals("false") || value.equals("0")) {
							value = false;
						}
					}
					
					executeMethod(pojo, methodName, t, (boolean) value);
					
				} else if (t == int.class || t == Integer.class) {
					if(value.getClass() == String.class) 
						value = Integer.parseInt(value.toString());

					// 整形
					executeMethod(pojo, methodName, t, value);
					
				} else if (t == int[].class || t == Integer[].class) {
					// 复数
					if (value instanceof String) {
						int[] intArr = strArr2intArr(value);
						executeMethod(pojo, methodName, t, intArr);
					} else {
						LOGGER.info("what's this!!? " + value);
					}
					
				} else if (t == String.class) {
					// 字符型
					Reflect.executeMethod(pojo, methodName, t, value.toString());
				} else if (t == String[].class) {
					// 复数
					if (value instanceof String[]) {
						Reflect.executeMethod(pojo, methodName, t, value);
					} else if (value instanceof ArrayList) {
						@SuppressWarnings("unchecked")
						ArrayList<String> list = (ArrayList<String>) value;
						String[] arr = new String[list.size()];
						
						executeMethod(pojo, methodName, t, list.toArray(arr));
					} else if (value instanceof String) {
						String str = (String) value;
						executeMethod(pojo, methodName, t, str.split(getDiver() + ""));
					} else {
						LOGGER.info("what's this!!?" + value.getClass().getName());
					}
					
				} else if (t == long.class || t == Long.class) { 
					// LONG 型
					executeMethod(pojo, methodName, t, Long.valueOf(value.toString()));
					
				} else if (t == Date.class) {
					
					if (value instanceof java.sql.Timestamp) {
						long time = ((java.sql.Timestamp) value).getTime();
						executeMethod(pojo, methodName, t, new Date(time));
					} else {
						executeMethod(pojo, methodName, t, DateTools.Objet2Date(value));
					}
				} else {
					// System.out.println("------------" + t.getName());
					executeMethod(pojo, methodName, value);
				}
			}
		}

		return pojo;
	}

	/**
	 * '["1", "2", ...]' --> [1, 2, ...]
	 * @param value 
	 * @return
	 */
	private int[] strArr2intArr(Object value) {
		String str = (String) value;
		// 当它们每一个都是数字的字符串形式
		String[] strArr = str.split(getDiver() + "");
		int[] intArr = new int[strArr.length];
		
		for (int i = 0; i < strArr.length; i++) 
			intArr[i] = Integer.parseInt(strArr[i]);
		
		return intArr;
	}

	/**
	 * 把原始数据 map 转换为实体
	 * 
	 * @param maps
	 *            原始数据
	 * @param fields
	 *            反射出来的字段信息
	 * @return 转换后的实体列表
	 */
//	public List<T> map2pojo(List<Map<String, Object>> maps, List<Field> fields) {
//		List<T> list = new ArrayList<>();
////		T[] a = (T[])java.lang.reflect.Array.newInstance(pojoClz, maps.size());
////		list.toArray(T[]);
//		
//		for (Map<String, Object> map : maps) 
//			list.add(map2pojo(map, fields));
//		
//		return list;
//	}

	/**
	 * 把原始数据 maps 转换为实体
	 * 
	 * @param maps
	 *            原始数据
	 * @return 转换后的实体列表
	 */
	public List<T> map2pojo(List<Map<String, Object>> maps) {
		List<Field> fields = getDeclaredField(pojoClz);
		List<T> list = new ArrayList<>();
//		T[] a = (T[])java.lang.reflect.Array.newInstance(pojoClz, maps.size());
//		list.toArray(T[]);
		
		for (Map<String, Object> map : maps) 
			list.add(map2pojo(map, fields));
		
		return list;
		
//		return map2pojo(maps, Reflect.getDeclaredField(pojoClz));
	}
	
	/**
	 * 把原始数据 map 转换为实体
	 * 
	 * @param map
	 *            原始数据
	 * @return 转换后的实体
	 */
	public T map2pojo(Map<String, Object> map) {
		return map2pojo(map, getDeclaredField(pojoClz));
	}

	/**
	 * @return {@link #diver}
	 */
	public char getDiver() {
		return diver;
	}

	/**
	 * @param diver {@link #diver}
	 */
	public void setDiver(char diver) {
		this.diver = diver;
	}
}
