package com.ajaxjs.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.StringUtil;

public class Json {
	private IEngine engine;
	
	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify(Map<String, ?> map) {
		if (com.ajaxjs.util.Util.isNotNull(map)) {
			List<String> arr = new ArrayList<>();
			for (String key : map.keySet())
				arr.add('\"' + key + "\":" + obj2jsonVaule(map.get(key)));
			
			return '{' + StringUtil.stringJoin(arr, ",") + '}';
		} else return null;
	}

	/**
	 * 将 Simple Object 对象转换成JSON格式的字符串:JAVA-->JS
	 * 
	 * @param obj
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify(Object obj) {
		if (obj == null) return null;
		// 检查是否可以交由 JS 转换的类型
		// if(obj instanceof NativeArray || obj instanceof NativeObject)return
		// navtiveStringify(obj);
	
		List<String> arr = new ArrayList<>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
	
			String key = field.getName();
			if (key.indexOf("this$") != -1)
				continue;
	
			Object _obj = null;
			try {
				_obj = field.get(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	
			arr.add('\"' + key + "\":" + obj2jsonVaule(_obj));
		}
	
		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

	/**
	 * 输出符合 JSON 要求的值
	 * 
	 * @param value
	 *            任意一个 Java 值
	 * @return JSON 形式的值
	 */
	public static String obj2jsonVaule(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof Boolean || value instanceof Number) {
			return value.toString();
		} else if (value instanceof Date) {
			return '\"' + DateTools.formatDate((Date) value, DateTools.commonDateFormat) + '\"';
		} else if (value instanceof Object[]) {
			Object[] arr = (Object[]) value;
			String[] strs = new String[arr.length];
			int i = 0;
			for (Object _value : arr)
				strs[i++] = obj2jsonVaule(_value); // 递归调用
	
			return '[' + StringUtil.stringJoin(strs, ",") + ']';
		} else { // String
			return '\"' + value.toString().replace("\\", "\\\\").replace("\"", "\\\"") + '\"';
		}
	}

}
