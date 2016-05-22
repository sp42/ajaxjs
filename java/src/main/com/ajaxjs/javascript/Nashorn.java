package com.ajaxjs.javascript;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ajaxjs.json.AbstractJsEngine;
import com.ajaxjs.util.LogHelper;

//import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Nashorn extends AbstractJsEngine {
	private static final LogHelper LOGGER = LogHelper.getLog(Nashorn.class);

	/**
	 * 创建一个 Nashorn 运行时
	 */
	static {
		js = new ScriptEngineManager().getEngineByName("nashorn");
		if (js == null) {
			LOGGER.warning("Nashorn is present.");
		}
	}

	public static void main(String[] args) throws ScriptException, IOException {
		Nashorn n = new Nashorn();
		n.load("C:/project/spring-test/src/com/ajaxjs/framework/config.js");
		Object obj = n.eval("g=[1, 2, 3];");
		System.out.println(obj.getClass().getName());

//		ScriptObjectMirror so = (ScriptObjectMirror) obj;
//		System.out.println(so.get(0).getClass().getName());
//		if (so.isArray()) {
//			System.out.println(so);
//			int[] iarr = (int[]) ScriptUtils.convert(so, int[].class);
//		}

	}

	/**
	 * js arr2 java arr
	 * 实际上，如果你不是强迫症，数组 get(0)/get(1)/... 一样可用，无须转换一次。
	 * @param scriptObjectMirror
	 * @return
	 */
//	public static Object[] toArray(ScriptObjectMirror scriptObjectMirror) {
//		if (!scriptObjectMirror.isArray()) {
//			throw new IllegalArgumentException("ScriptObjectMirror is no array");
//		}
//
//		if (scriptObjectMirror.isEmpty()) {
//			return new Object[0];
//		}
//
//		Object[] array = new Object[scriptObjectMirror.size()];
//
//		int i = 0;
//		for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {
//			Object result = entry.getValue();
//
//			if (result instanceof ScriptObjectMirror && scriptObjectMirror.isArray()) {
//				array[i] = toArray((ScriptObjectMirror) result);
//			} else {
//				array[i] = result;
//			}
//
//			i++;
//		}
//
//		return array;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public Object get(String... namespace) {
		Map<String, Object> obj = (Map<String, Object>) js.get(namespace[0]);

		for (int i = 1; i < namespace.length; i++) {
			try {
				obj = (Map<String, Object>) obj.get(namespace[i]);
			} catch (ClassCastException e) {
				return obj.get(namespace[i]);
			}
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> eval_return_Map(String code) {
		Object obj = eval(code);
		return obj == null ? null : (Map<String, Object>)obj;
	}
}
