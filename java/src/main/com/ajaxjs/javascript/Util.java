package com.ajaxjs.javascript;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.util.DateTools;
import com.ajaxjs.util.StringUtil;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

//import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Util {

	/**
	 * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t
	 * 换行用\r
	 * 
	 * @param json
	 *            原 JSON 字符串
	 * @return 格式化后美观的 JSON
	 */
	public static String format(String json) {
		int level = 0;
		StringBuilder str = new StringBuilder();
		
		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);
			if (level > 0 && '\n' == str .charAt(str.length() - 1))
				str.append(StringUtil.repeatStr("\t", "", level));
			
			switch (c) {
				case '{':
				case '[':
					str.append(c + "\n");
					level++;
					break;
				case ',':
					if(json.charAt(i + 1) == '"')
						str.append(c + "\n"); // 后面必定是跟着 key 的双引号，但 其实 json 可以 key 不带双引号的
					
					break;
				case '}':
				case ']':
					str.append("\n");
					level--;
					str.append(StringUtil.repeatStr("\t", "", level));
					str.append(c);
					break;
				default:
					str.append(c);
					break;
			}
		}
	
		return str.toString();
	}

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

	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] toMapArray(Object _obj) { 
		NativeArray arr= (NativeArray)_obj;
		if (arr != null && arr.size() > 0) { // NativeArray.isEmpty 有 bug，
//			Not real Map!
//			return (Map<String, Object>[])arr.toArray(new Map[arr.size()]);

			Map<String, Object>[] _arr = new HashMap[arr.size()]; // java 不支持泛型数组
			int i = 0;
			for (Object obj : arr) { // JDK7 supports directly
				if (obj instanceof NativeObject) {
					_arr[i++] = NativeObject2Map((NativeObject) obj);
				} else if (obj instanceof NativeArray) {
//					LOGGER.info("TODO NativeArray");
				}
			}

			return _arr;
		} else
			return null;
	}	
	
	/**
	 * JS Object 对象转换到 Java Hash 对象
	 * 
	 * @param obj
	 *            JS 里面的 Map
	 * @return Java 里面的 Map
	 */
	public static Map<String, Object> NativeObject2Map(NativeObject obj) {
		Map<String, Object> hash = null;

		if (obj != null) {
			hash = new HashMap<>();

			for (Object id : obj.getAllIds()) {// 遍历对象
				String newId = id.toString();
				Object value = obj.get(newId, obj);
				hash.put(newId, jsValue2java(value));
			}
		}

		return hash;
	}
	
	private static Object jsValue2java(Object value) {
		if (value == null || value instanceof Boolean || value instanceof String) {
			// js 为 null，所以 java hash 也为null
			// nothing but still value;
		} else if (value instanceof Double) {
			value = ((Double) value).intValue();// js number 转换为 INT
		} else if (value instanceof NativeObject) {
			// value = jsValue2java(value);
		} else if (value instanceof NativeArray) {
			value = toMapArray((NativeArray) value); // 这是规则的情况，数组中每个都是对象，而非
														// string/int/boolean
														// @todo
		} else {
			// LOGGER.info("未知 JS 类型：" + value.getClass().getName());
		}
		
		return value;
	}
	
//	@SuppressWarnings("unchecked")
//	public static Map<String, Object>[] toMapArray(Object obj) { 
//		if (!(obj instanceof ScriptObjectMirror)) {
//			throw new IllegalArgumentException("The argument is no ScriptObjectMirror");
//		}
//		
//		ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror)obj;
//		
//	    if (!scriptObjectMirror.isArray()) {  
//	        throw new IllegalArgumentException("ScriptObjectMirror is no array");  
//	    }  
//	  
//	    if (scriptObjectMirror.isEmpty()) {  
//	        return null;  
//	    }  
//	    
//	    Map<String, Object>[] array = new Map[scriptObjectMirror.size()];
//	  
//	    int i = 0;  
//	    for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {  
//	        Object result = entry.getValue();  
//	  
//	        array[i] = (Map<String, Object>)result;  
//	        i++;  
//	    }  
//	  
//	    return array; 
//	}
	
	
//	public static Object[] toArray(ScriptObjectMirror scriptObjectMirror) {  
//	    if (!scriptObjectMirror.isArray()) {  
//	        throw new IllegalArgumentException("ScriptObjectMirror is no array");  
//	    }  
//	  
//	    if (scriptObjectMirror.isEmpty()) {  
//	        return new Object[0];  
//	    }  
//	  
//	    Object[] array = new Object[scriptObjectMirror.size()];  
//	  
//	    int i = 0;  
//	    for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {  
//	        Object result = entry.getValue();  
//	  
//	        if (result instanceof ScriptObjectMirror && ((ScriptObjectMirror) result).isArray()) {  
//	            array[i] = toArray((ScriptObjectMirror) result);  
//	        } else {  
//	            array[i] = result;  
//	        }  
//	  
//	        i++;  
//	    }  
//	  
//	    return array;  
//	}  

}
