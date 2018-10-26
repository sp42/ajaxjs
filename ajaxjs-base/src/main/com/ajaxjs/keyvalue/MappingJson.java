package com.ajaxjs.keyvalue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.util.CollectionUtil;
import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.StringUtil;

/**
 * 转换 JSON
 * 
 * @author Frank Cheung
 *
 */
public class MappingJson {

	/**
	 * 将 Simple Object 对象转换成 JSON 格式的字符串:JAVA-->JS
	 * 
	 * @param obj 输入数据
	 * @return JSON 字符串
	 */
	public static String stringifySimpleObject(Object obj) {
		if (obj == null)
			return null;
	
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
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map 输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyMap(Map<String, ?> map) {
		if (map == null)
			return null;
	
		List<String> arr = new ArrayList<>();
		for (String key : map.keySet())
			arr.add('\"' + key + "\":" + obj2jsonVaule(map.get(key)));
	
		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

	/**
	 * 输入一个 List<Map<String, Object>>，将其转换为 JSON Str
	 * 
	 * @param list 输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyListMap(List<Map<String, Object>> list) {
		if (null == list)
			return null;
	
		String[] str = new String[list.size()];
	
		for (int i = 0; i < list.size(); i++)
			str[i] = stringifyMap(list.get(i));
	
		return "[" + StringUtil.stringJoin(str, ",") + "]";
	}

	/**
	 * 输出符合 JSON 要求的值
	 * 
	 * @param value 任意一个 Java 值
	 * @return JSON 形式的值
	 */
	@SuppressWarnings("unchecked")
	public static String obj2jsonVaule(Object value) {
		
		if (value == null) {
			return "null";
		} else if (value instanceof Double) {
			return value + "";
		} else if (value instanceof Boolean || value instanceof Number) {
			return value.toString();
		} else if (value instanceof Date) {
			return '\"' + DateTools.formatDate((Date) value, DateTools.commonDateFormat) + '\"';
		} else if (value instanceof Map) {
			Map<String, ?> map = (Map<String, ?>) value;
			return map.size() == 0 ? "{}" : MappingJson.stringifyMap(map);
		} else if (value instanceof List) {
			List<?> list = (List<?>) value;
	
			if (list.size() == 0) {
				return "[]";
			} else if (list.get(0) instanceof Integer) {
				List<Integer> intList = (List<Integer>) list;
				StringBuilder sb = new StringBuilder();
	
				for (int i = 0; i < intList.size(); i++) {
					sb.append(intList.get(i));
					if (i != (intList.size() - 1))
						sb.append(", ");
				}
	
				return '[' + sb.toString() + ']';
			} else if (list.get(0) instanceof String) {
				List<String> strList = (List<String>) list;
				StringBuilder sb = new StringBuilder();
	
				for (int i = 0; i < strList.size(); i++) {
					sb.append("\"" + strList.get(i) + "\"");
					if (i != (strList.size() - 1))
						sb.append(", ");
				}
	
				return '[' + sb.toString() + ']';
			} else if (list.get(0) instanceof Map) {
				List<Map<String, ?>> maps = (List<Map<String, ?>>) list;
				StringBuilder sb = new StringBuilder();
	
				for (int i = 0; i < maps.size(); i++) {
					sb.append(MappingJson.stringifyMap(maps.get(i)));
					if (i != (maps.size() - 1))
						sb.append(", ");
				}
				return '[' + sb.toString() + ']';
			} else {
				// 未知类型数组，
				return "[]";
			}
	
			// return stringify((Map<String, ?>)value);
		} else if (value.getClass() == int[].class) {
			String[] strs = CollectionUtil.int_arr2string_arr((int[]) value);
			return '[' + StringUtil.stringJoin(strs) + ']';
		} else if (value instanceof Object[]) {
			Object[] arr = (Object[]) value;
			String[] strs = new String[arr.length];
			int i = 0;
	
			for (Object _value : arr)
				strs[i++] = obj2jsonVaule(_value); // 递归调用
	
			return '[' + StringUtil.stringJoin(strs) + ']';
		} else if (value instanceof BaseModel) {
			return BeanUtil.beanToJson(value);
		} else { // String
			return '\"' + value.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + '\"';
		}
	}
}
