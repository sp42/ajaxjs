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
package com.ajaxjs.json;

import java.io.IOException;
import java.util.Map;

import javax.script.ScriptException;

import com.ajaxjs.util.LogHelper;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;

//import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class Nashorn extends AbstractJsEngine {
	private static final LogHelper LOGGER = LogHelper.getLog(Nashorn.class);

	/**
	 * 创建一个 Nashorn 运行时
	 */
	public Nashorn() {
		super("nashorn");
	}
	

	public static void main(String[] args) throws ScriptException, IOException {
		Nashorn n = new Nashorn();
		n.load("C:/project/spring-test/src/com/ajaxjs/framework/config.js");
		Object obj = n.eval("g=[1, 2, 3];");
		System.out.println(obj.getClass().getName());

		ScriptObjectMirror so = (ScriptObjectMirror) obj;
		System.out.println(so.get(0).getClass().getName());
		if (so.isArray()) {
			System.out.println(so);
//			int[] iarr = (int[]) ScriptUtils.convert(so, int[].class);
		}

	}


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

	@Override
	public Map<String, Object> eval_return_Map(String code, String varName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] toMapArray(Object obj) { 
		if (!(obj instanceof ScriptObjectMirror)) {
			throw new IllegalArgumentException("The argument is no ScriptObjectMirror");
		}
		
		ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror)obj;
		
	    if (!scriptObjectMirror.isArray()) {  
	        throw new IllegalArgumentException("ScriptObjectMirror is no array");  
	    }  
	  
	    if (scriptObjectMirror.isEmpty()) {  
	        return null;  
	    }  
	    
	    Map<String, Object>[] array = new Map[scriptObjectMirror.size()];
	  
	    int i = 0;  
	    for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {  
	        Object result = entry.getValue();  
	  
	        array[i] = (Map<String, Object>)result;  
	        i++;  
	    }  
	  
	    return array; 
	}
	

	/**
	 * js arr2 java arr
	 * 实际上，如果你不是强迫症，数组 get(0)/get(1)/... 一样可用，无须转换一次。
	 * @param scriptObjectMirror
	 * @return
	 */
	public static Object[] toArray(ScriptObjectMirror scriptObjectMirror) {
		if (!scriptObjectMirror.isArray()) {
			throw new IllegalArgumentException("ScriptObjectMirror is no array");
		}

		if (scriptObjectMirror.isEmpty()) {
			return new Object[0];
		}

		Object[] array = new Object[scriptObjectMirror.size()];

		int i = 0;
		for (Map.Entry<String, Object> entry : scriptObjectMirror.entrySet()) {
			Object result = entry.getValue();

			if (result instanceof ScriptObjectMirror && scriptObjectMirror.isArray()) {
				array[i] = toArray((ScriptObjectMirror) result);
			} else {
				array[i] = result;
			}

			i++;
		}

		return array;
	}


	@Override
	public String[] eval_return_StringArray(String code) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, Object>[] eval_return_MapArray(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
