package com.ajaxjs.javascript;

import java.util.HashMap;
import java.util.Map;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;

//import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Util {

 

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
