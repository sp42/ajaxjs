package com.ajaxjs.js;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;
import sun.org.mozilla.javascript.internal.Undefined;

/**
 * 用处不大了
 * @author xinzhang
 *
 */
public class RhinoJsObjectCovernt implements JsObjectCovernt {
	
	@Override
	public Object get(ScriptEngine engine, String... namespace) {
		NativeObject obj = (NativeObject) engine.get(namespace[0]);

		for (int i = 1; i < namespace.length; i++) {
			try {
				obj = (NativeObject) obj.get(namespace[i]);
			} catch (ClassCastException e) {
				// LOGGER.warning(e);
				return obj.get(namespace[i]);
			}
		}

		return obj;
	}
	
	/**
	 * 
	 * 不推荐使用该方法
	 * 
	 * @param value
	 *            Js 里面的值
	 * @return Java 里面的值
	 */
	public static Object jsValue2java(Object value) {
		if (value == null || value instanceof Boolean || value instanceof String || value instanceof Undefined) {
			// js 为 null，所以 java hash 也为null
			// nothing but still value;
		} else if (value instanceof Double) {
			value = JsEngineWrapper.double2int((Double) value);// js number 转换为 short
		} else if (value instanceof NativeObject) {
			value = jsValue2java(value);
		} else if (value instanceof NativeArray) {
			value = (List<?>) value; // 这是规则的情况，数组中每个都是对象，而非
										// string/int/boolean
										// TODO
		} else {
			System.out.println("未知 JS 类型：" + value.getClass().getName());
		}

		return value;
	}
	
	public Map<String, Object> eval_return_Map(JsEngineWrapper engine, String code, String varName) {
		engine.eval(code, null); 

		return NativeObject2Map(engine.eval(varName, NativeObject.class)); // 提取变量
	}

	/**
	 * 输入 Js Array 代码（字符串），返回 Java String List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 String[]
	 */
	public String[] eval_return_StringArray(JsEngineWrapper engine, String code) {
		NativeArray obj = engine.eval(code, NativeArray.class);
		return NativeArray2StringArray(obj);
	}

	/**
	 * 输入 Js Array 代码（字符串），返回 Java Map List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 Map<String, Object>[]
	 */
	public Map<String, Object>[] eval_return_MapArray(JsEngineWrapper engine, String code) {
		NativeArray arr = engine.eval(code, NativeArray.class);
		return NativeArray2MapArray(arr);
	}

	public Map<String, Object>[] eval_return_MapArray(NativeArray arr) {
		return NativeArray2MapArray(arr);
	}
		
	/**
	 * JS Object 对象转换到 Java Hash 对象
	 * 
	 * @param obj
	 *            JS 里面的 Map
	 * @return Java 里面的 Map
	 */
	public static Map<String, Object> NativeObject2Map(NativeObject obj) {
		if (obj == null)
			return null;

		Map<String, Object> map = new HashMap<>();
		for (Object id : obj.getAllIds()) {// 遍历对象
			String newId = id.toString();
			Object value = obj.get(newId, obj);
			map.put(newId, jsValue2java(value));
		}

		return map;
	}
	
	/**
	 * JS Array 对象转换到 Java String Array 对象
	 * 
	 * @param arr
	 *            JS 里面的数组，为 String 组成
	 * @return Java 里面的 Map[]
	 */
	public static String[] NativeArray2StringArray(NativeArray arr) {
		if (arr == null || arr.size() < 1) return null;

		return (String[]) arr.toArray(new String[arr.size()]);
	}
	
	/**
	 * JS Array 对象转换到 Java 任意 Array 对象 NativeArray arr = eval(code,
	 * NativeArray.class); return Mapper.NativeArray2Map(arr, Map.class);
	 * 
	 * @param arr
	 *            JS 数组
	 * @param clazz
	 *            要转换的目标类型
	 * @return 已转换的数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] NativeArrayAs(NativeArray arr, Class<T> clazz) {
		if (arr == null || arr.size() < 1) return null;

		return (T[]) arr.toArray((T[]) Array.newInstance(clazz, arr.size()));
	}

	/**
	 * JS Array 对象转换到 Java Map Array 对象
	 * 
	 * @param arr
	 *            JS 里面的数组，为 Map 组成
	 * @return Java 里面的 Map[]
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] NativeArray2MapArray(NativeArray arr) {
//		if (!com.ajaxjs.util.Util.isNotNull(arr))// NativeArray.isEmpty 有 bug，
//			return null;
		if (arr == null || arr.size() < 1) return null;
		 
		// Not real Map!
		// return (Map<String, Object>[])arr.toArray(new Map[arr.size()]);

		Map<String, Object>[] _arr = new HashMap[arr.size()]; // java 不支持泛型数组
		
		int i = 0;
		for (Object obj : arr) { // JDK7 supports directly
			if (obj instanceof NativeObject) {
				_arr[i++] = NativeObject2Map((NativeObject) obj);
			} else if (obj instanceof NativeArray) {
				System.out.println("TODO NativeArray");
			}
		}

		return _arr;
	}
}
