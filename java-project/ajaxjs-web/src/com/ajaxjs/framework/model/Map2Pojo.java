package com.ajaxjs.framework.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.util.Reflect;

/**
 * 将 MAP 数据通过反射保存到 POJO 中。
 * @author frank
 *
 * @param <T>
 */
public class Map2Pojo<T extends BaseModel> {
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
				// System.out.println(key + ":" +
				// map.get(key).getClass().getName());

				String methodName = "set" + Reflect.firstLetterUpper(key);

				if (t == boolean.class) {
					// 布尔型
					methodName = key.replace("is", "");
					methodName = "set" + Reflect.firstLetterUpper(methodName);
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
						System.out.println("what's this!!?" + value);
					}
					
				} else if (t == String[].class) {
					// 复数
					if (value instanceof String) {
						String str = (String) value;
						Reflect.executeMethod(pojo, methodName, t, str.split(getDiver() + ""));
					} else {
						System.out.println("what's this!!?" + value);
					}
					
				} else if (t == long.class) { 
					// LONG 型
					Reflect.executeMethod(pojo, methodName, t, Long.valueOf(value.toString()));
					
				} else if (t == Date.class) {
					
					if (value instanceof java.sql.Timestamp) {
						long time = ((java.sql.Timestamp) value).getTime();
						Reflect.executeMethod(pojo, methodName, t, new Date(time));
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
	 * 
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
