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
package com.ajaxjs.framework.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Reflect;

/**
 * 将 MAP 数据通过反射保存到 POJO 中。
 * @author frank
 *
 * @param <T>
 */
public class Map2Pojo<T> {
	private static final LogHelper LOGGER = LogHelper.getLog(Map2Pojo.class);
	
	/**
	 * 实体类型
	 */
	private Class<T> pojoClz;

	/**
	 * 用于数组的分隔符
	 */
	private char diver = ',';
	
	/**
	 * 
	 * @param pojoClz
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
		T pojo = Reflect.newInstance(pojoClz);
		if(pojo == null) return null;

		for (Field f : fields) {
			String key = f.getName(); // 字段名称
			Class<?> t = f.getType(); // 字段期望的类型
			Object value = map.get(key);

			if (value != null) {
				 System.out.println(key + ":" + map.get(key).getClass().getName());

				String methodName = "set" + Reflect.firstLetterUpper(key);

				if (t == boolean.class) {
					System.out.println("methodName：：：：：" + methodName);
					// 布尔型
					methodName = key.replace("is", "");
					methodName = "set" + Reflect.firstLetterUpper(methodName);
					System.out.println("methodName：：：：：" + "set" + Reflect.firstLetterUpper(methodName));
					Reflect.executeMethod(pojo, methodName, t, (boolean) value);
					
				} else if (t == int.class) {
					// 整形
					Reflect.executeMethod(pojo, methodName, t, value);
					
				} else if (t == int[].class) {
					// 复数
					if (value instanceof String) {
						int[] intArr = strArr2intArr(value);
						Reflect.executeMethod(pojo, methodName, t, intArr);
					} else {
						LOGGER.info("what's this!!? " + value);
					}
					
				} else if (t == String[].class) {
					// 复数
					if (value instanceof String[]) {
						Reflect.executeMethod(pojo, methodName, t, value);
					} else if (value instanceof ArrayList) {
						@SuppressWarnings("unchecked")
						ArrayList<String> list = (ArrayList<String>) value;
						String[] arr = new String[list.size()];
						Reflect.executeMethod(pojo, methodName, t, list.toArray(arr));
					} else if (value instanceof String) {
						String str = (String) value;
						Reflect.executeMethod(pojo, methodName, t, str.split(getDiver() + ""));
					} else {
						LOGGER.info("what's this!!?" + value.getClass().getName());
					}
					
				} else if (t == long.class) { 
					// LONG 型
					Reflect.executeMethod(pojo, methodName, t, Long.valueOf(value.toString()));
					
				} else if (t == Date.class) {
					
					if (value instanceof java.sql.Timestamp) {
						long time = ((java.sql.Timestamp) value).getTime();
						Reflect.executeMethod(pojo, methodName, t, new Date(time));
					} else {
						Reflect.executeMethod(pojo, methodName, t, DateTools.Objet2Date(value));
					}
				} else {
					// System.out.println("------------" + t.getName());
					Reflect.executeMethod(pojo, methodName, value);
				}
			}
		}

//		System.out.println(pojo.getName());

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
		for (int i = 0; i < strArr.length; i++) {
			intArr[i] = Integer.parseInt(strArr[i]);
		}
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
	public List<T> map2pojo(List<Map<String, Object>> maps, List<Field> fields) {
		List<T> list = new ArrayList<>();
//		T[] a = (T[])java.lang.reflect.Array.newInstance(pojoClz, maps.size());
//		list.toArray(T[]);
		for (Map<String, Object> map : maps) {
			list.add(map2pojo(map, fields));
		}
		return list;
	}

	/**
	 * 把原始数据 map 转换为实体
	 * 
	 * @param maps
	 *            原始数据
	 * @return 转换后的实体列表
	 */
	public List<T> map2pojo(List<Map<String, Object>> maps) {
		List<Field> fields = Reflect.getDeclaredField(pojoClz);
	
		return map2pojo(maps, fields);
	}
	
	public T map2pojo(Map<String, Object> map) {
		List<Field> fields = Reflect.getDeclaredField(pojoClz);
		return map2pojo(map, fields);
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

	/**
	 * 
	 * @param methodName
	 *            方法名称
	 * @param action
	 *            set|get
	 * @return
	 */
	public static String getFieldName(String methodName, String action) {
		methodName = methodName.replace(action, "");
		methodName = Character.toString(methodName.charAt(0)).toLowerCase() + methodName.substring(1);
		return methodName;
	}

	/**
	 * （这是个简易版）
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> setPojoToMapValue(Object obj) {
		if (obj != null) {
			Map<String, Object> map = new HashMap<>();
	
			for (Method method : obj.getClass().getMethods()) {
				String methodName = method.getName();
				if (methodName.startsWith("get")) {
					Object value = Reflect.executeMethod(obj, method);
	
					if (value != null) {
						map.put(Map2Pojo.getFieldName(methodName, "get"), value);
					}
				}
			}
	
			return map;
		} else {
			LOGGER.warning("Null pointer");
			return null;
		}
	}

	/**
	 * 将 map 中数据填入到 obj 中。obj 是 POJO。 如果 POJO 有这个 setter，那就根据 setter 中 setXxx 获取
	 * xxx，作为 map 的 key 读取 map 的那個 value。
	 * （这是个简易版）
	 * @param map
	 * @param obj
	 */
	public static void setMapValueToPojo_Simple(Map<String, Object> map, Object obj) {
		if (obj != null && map != null) {
			for (Method method : obj.getClass().getMethods()) {
				String methodName = method.getName();
				if (methodName.startsWith("set")) {
					methodName = Map2Pojo.getFieldName(methodName, "set");
					if (map.containsKey(methodName)) {
						Reflect.executeMethod(obj, method, map.get(methodName));
					}
				}
			}
		} else {
			LOGGER.warning("Null pointer");
		}
	}
}
