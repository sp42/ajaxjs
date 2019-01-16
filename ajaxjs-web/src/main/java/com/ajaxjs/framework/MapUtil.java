package com.ajaxjs.framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.keyvalue.MappingValue;
import com.ajaxjs.util.CommonUtil;

public class MapUtil {
	/**
	 * 转换
	 * @param map
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> as(Map<?, ?> map, Class<T> clz) {
		Map<String, T> _map = new HashMap<>();

		for (Object key : map.keySet()) {
			_map.put(key.toString(), (T) map.get(key));
		}

		return _map;
	}

	public static Map<String, Object> to(Map<?, ?> map, Function<String[], Object> strArrHandler) {
		Map<String, Object> _map = new HashMap<>();

		for (Object key : map.keySet()) {
			Object value = map.get(key);
			if (value instanceof String[]) {
				value = strArrHandler == null ? Arrays.toString((String[]) value) : strArrHandler.apply((String[]) value);
			}

			_map.put(key.toString(), value);
		}

		return _map;
	}

	public static Map<String, Object> toRealValue(Map<?, ?> map) {
		Map<String, Object> _map = new HashMap<>();

		for (Object key : map.keySet()) {
			Object value = MappingValue.toJavaValue(map.get(key).toString());

			_map.put(key.toString(), value);
		}

		return _map;
	}

	static <T> String eachList(List<T> list, Function<T, String> fn) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < list.size(); i++) {
			sb.append(fn.apply(list.get(i)));
			if (i != (list.size() - 1))
				sb.append(", ");
		}

		return '[' + sb.toString() + ']';
	}

	static <T> String jsonArr(T[] o, Function<T, String> fn) {
		if (o.length == 0)
			return "[]";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < o.length; i++) {
			sb.append(fn.apply((T) o[i]));
			if (i != (o.length - 1))
				sb.append(", ");
		}

		return '[' + sb.toString() + ']';
	}

	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return '\"' + obj.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + '\"';
		} else if (obj instanceof Double) {
			return obj + "";
		} else if (obj instanceof Boolean || obj instanceof Number) {
			return obj.toString();
		} else if (obj instanceof Date) {
			return '\"' + CommonUtil.SimpleDateFormatFactory(CommonUtil.commonDateFormat).format((Date) obj) + '\"';

		} else if (obj.getClass() == Integer[].class) {
			return jsonArr((Integer[]) obj, v -> v + "");
		} else if (obj.getClass() == int[].class) {
			Integer[] arr = Arrays.stream((int[]) obj).boxed().toArray(Integer[]::new);
			return jsonArr(arr, v -> v + "");
		} else if (obj instanceof Long[]) {
			return jsonArr((Long[]) obj, v -> v.toString());
		} else if (obj instanceof long[]) {
			Long[] arr = Arrays.stream((long[]) obj).boxed().toArray(Long[]::new);
			return jsonArr(arr, v -> v.toString());
		} else if (obj instanceof String[]) {
			return jsonArr((String[]) obj, v -> "\"" + v + "\"");
		} else if (obj instanceof Map) {
			return stringifyMap((Map<?, ?>) obj);
		} else if (obj instanceof Map[]) {
			return jsonArr((Map<?, ?>[]) obj, MapUtil::stringifyMap);

		} else if (obj instanceof BaseModel) {
			return BeanUtil.beanToJson((BaseModel) obj);
		} else if (obj instanceof BaseModel[]) {
			return jsonArr((BaseModel[]) obj, BeanUtil::beanToJson);

		} else if (obj instanceof List) {
			List<?> list = (List<?>) obj;

			if (list.size() > 0) {
				if (list.get(0) instanceof Integer) {
					return toJson(list.toArray(new Integer[list.size()]));
				} else if (list.get(0) instanceof String) {
					return toJson(list.toArray(new String[list.size()]));
				} else if (list.get(0) instanceof Map) { // Map 类型的输出
					return toJson(list.toArray(new Map[list.size()]));
				} else if (list.get(0) instanceof BaseModel) { // Bean
					return toJson(list.toArray(new BaseModel[list.size()]));
				}
			} else {
				return "[]";
			}
		} else if (obj instanceof Object[]) {
			return jsonArr((Object[]) obj, v -> toJson(v));
		} else if (obj instanceof Object) {
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

				arr.add('\"' + key + "\":" + toJson(_obj));
			}

			return '{' + String.join(",", arr) + '}';
		} else {
			throw new RuntimeException("不支持数据类型");
		}

		return null;
	}

	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map 输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyMap(Map<?, ?> map) {
		if (map == null)
			return null;

		if (map.size() == 0)
			return "{}";

		List<String> arr = new ArrayList<>();
		for (Object key : map.keySet())
			arr.add('\"' + key.toString() + "\":" + toJson(map.get(key)));

		return '{' + String.join(",", arr) + '}';
	}

	/**
	 * 输出到 JSON 文本时候的换行
	 * 
	 * @param str
	 * @return
	 */
	public static String jsonString_covernt(String str) {
		return str.replace("\r\n", "\\n");
	}
}
