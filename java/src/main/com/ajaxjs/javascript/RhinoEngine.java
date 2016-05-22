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
package com.ajaxjs.javascript;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ajaxjs.json.AbstractJsEngine;
import com.ajaxjs.util.LogHelper;

import sun.org.mozilla.javascript.internal.NativeArray;
import sun.org.mozilla.javascript.internal.NativeObject;


/**
 * JS 引擎
 * @author Frank Cheung
 *
 */
public class RhinoEngine extends AbstractJsEngine {
	private static final LogHelper LOGGER = LogHelper.getLog(RhinoEngine.class);

	public RhinoEngine(){
		super("rhino");
	}
	
	public static void main(String[] args) throws ScriptException, IOException {
		new RhinoEngine().eval("new RegExp('\\\\{\\\\}', 'g')");
//		new RhinoEngine().load("C:/project/spring-test/src/com/ajaxjs/framework/config.js");
	}
	
	/**
	 * eval() JS 代码，并把返回值转换为具体的 String 类型
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JS 运算后的返回值，字符串类型的值，也可能是 null 没有返回
	 */
	public String eval_return_String(String code) {
		return eval(code, String.class);
	}

	/**
	 * eval() JS 代码，并把返回值转换为具体的 int 类型
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JS 运算后的返回值，整形类型的值，也可能是 null 没有返回
	 */
	public int eval_return_Int(String code) {
		Double d = eval(code, Double.class);
		if (d > Integer.MAX_VALUE) {
			LOGGER.warning(d + "数值太大，不应用这个方法");
			return 0;
		} else {
			return d.intValue();
		}
	}

	/**
	 * eval() JS 代码，并把返回值转换为具体的 boolean 类型
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JS 运算后的返回值，布尔类型的值，也可能是 null 没有返回
	 */
	public Boolean eval_return_Boolean(String code) {
		return eval(code, Boolean.class);
	}
	
	/**
	 * 输入 "{'a':0, 'b':1}" 字符串返回 Map
	 * 
	 * @param json
	 *            JS 代碼
	 * @return Java 里的 map
	 */
	public Map<String, Object> eval_return_Map(String json) {
		return eval_return_Map("_json = " + json, "_json");
	}

	/**
	 * 输入： var remote_ip_info = {"ret":1,"start":-1,"end":-1}; 指定变量名，返回 map 注意
	 * js 全局变量名冲突
	 * 
	 * @param code
	 *            JS 代碼
	 * @param varName
	 *            變量名
	 * @return Java 里的 map
	 */
	public Map<String, Object> eval_return_Map(String code, String varName) {
		// rhino 不能直接返回 map，如 eval("{a:1}") -->null，必须加变量
		eval(code); // 执行 var xx = {...};
		
		return RhinoMapper.NativeObject2Map(eval(varName, NativeObject.class)); // 提取变量
	}

	/**
	 * 输入 js Array 代码（字符串），返回 Java Map List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 String[]
	 */
	public String[] eval_return_StringArray(String code) {
		NativeArray obj = eval(code, NativeArray.class);
		return RhinoMapper.NativeArray2StringArray(obj);
	}

	/**
	 * 输入 js Array 代码（字符串），返回 Java Map List
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 Map<String, Object>[]
	 */
	public Map<String, Object>[] eval_return_MapArray(String code) {
		NativeArray arr = eval(code, NativeArray.class);
		return RhinoMapper.NativeArray2MapArray(arr);
	}

	
	/**
	 * 序列化 JSON
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JSON
	 */
	public String nativeStringify(String code) {
		return eval_return_String("JSON.stringify(" + code + ");");
	}
	
	@Override
	public Object get(String... namespace) {
		NativeObject obj = (NativeObject) js.get(namespace[0]);

		for (int i = 1; i < namespace.length; i++) {
			try {
				obj = (NativeObject) obj.get(namespace[i]);
			} catch (ClassCastException e) {
				return obj.get(namespace[i]);
			}
		}

		return obj;
	}
}
