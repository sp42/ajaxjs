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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Util;
import com.ajaxjs.util.FileUtil;
import com.ajaxjs.util.LogHelper;

/**
 * JS 引擎公共基类
 *
 */
public abstract class AbstractJsEngine implements IEngine, ToJavaType, ToJsType {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractJsEngine.class);

	/**
	 * JS 运行时
	 */
	public ScriptEngine js;

	/**
	 * 创建一个 JS 运行时
	 * 
	 * @param js
	 *            JS 运行时
	 */
	public AbstractJsEngine(ScriptEngine js) {
		this.js = js;
	}

	/**
	 * 创建一个 JS 运行时
	 * 
	 * @param jsEngineName
	 *            JS 运行时名称
	 */
	public AbstractJsEngine(String jsEngineName) {
		js = new ScriptEngineManager().getEngineByName(jsEngineName);
		if (js == null) {
			LOGGER.warning(jsEngineName + " is not present.");
		}
	}

	@Override
	public void load(String[] paths) {
		String code;

		for (String path : paths) {
			LOGGER.info("加载 js: {0} 文件", path);

			try {
				code = FileUtil.readFileAsText(path);
			} catch (IOException e) {
				LOGGER.warning("加载文件 " + path + "的时候，磁盘找不到该文件！", e);
				return;
			}

			eval(code);
		}
	}

	@Override
	public void load(String path) {
		load(new String[] { path });
	}

	@Override
	public void load(Class<?> cls, String js) {
		load(Util.getClassFolder_FilePath(cls, js));
	}

	@Override
	public <T> T eval(String code, Class<T> clazz) {
		if (StringUtil.isEmptyString(code))
			throw new UnsupportedOperationException("JS 代码不能为空！");

		Object obj = null;

		try {
			obj = js.eval(code);
		} catch (ScriptException e) {
			LOGGER.warning("脚本eval()运算发生异常！eval 代码：" + code, e);
		}

		if (obj != null) {
			// return Util.TypeConvert(js.eval(code), clazz); // 为什么要执行多次？
			T _obj = Util.TypeConvert(obj, clazz);
			return _obj;
		} else
			return null;
	}

	@Override
	public Object eval(String code) {
		return eval(code, Object.class);
	}

	@Override
	public <T> T call(String method, Class<T> clazz, Object binding, Object... args) {
		Invocable inv = (Invocable) js; // Invocable 接口是
										// ScriptEngine可选实现的接口。（多态）
		Object result = null;

		try {
			result = binding != null ? inv.invokeMethod(binding, method, args) : inv.invokeFunction(method, args);
		} catch (NoSuchMethodException e) {
			LOGGER.warning("脚本引擎没有{0}() 这个方法", method);
		} catch (ScriptException e) {
			LOGGER.warning("向脚本引擎调用脚本方法异常！方法名称:" + method, e);
		}

		return Util.TypeConvert(result, clazz);
	}

	@Override
	public void put(String varName, Object obj) {
		js.put(varName, obj);
	}
	
	@Override
	public String eval_return_String(String code) {
		return eval(code, String.class);
	}

	@Override
	public int eval_return_Int(String code) {
		Double d = eval(code, Double.class);
		if (d > Integer.MAX_VALUE) {
			LOGGER.warning(d + "数值太大，不应用这个方法");
			return 0;
		} else {
			return d.intValue();
		}
	}

	@Override
	public boolean eval_return_Boolean(String code) {
		return eval(code, Boolean.class);
	}
	
	@Override
	public Map<String, Object> eval_return_Map(String json) {
		return eval_return_Map("_json = " + json, "_json");
	}
	
	@Override
	public String JSON_Stringify(String code) {
		return eval_return_String("JSON.stringify(" + code + ");");
	}
	
	@Override
	public String JSON_Stringify(Object obj) {
		return call("stringify", String.class, eval("JSON"), obj);
	}

	/**
	 * 基础的 JavaScript 工具函数
	 */
	public static final String baseJavaScriptCode;

	static {
		final String[] str = { "if(!String.prototype.format){	", "	String.prototype.format = function () {",
				"		var str = this; ",
				"		if(arguments[0] instanceof java.lang.String || typeof(arguments[0]) == \'string\'|| typeof(arguments[0]) == \'number\'){",
				"			for(var i = 0, j = arguments.length; i < j; i++) {",
				"				str = str.replace(new RegExp('\\\\{' + i +'\\\\}', 'g'), String(arguments[i]));}",
				"		}else{   ", "			for(var i in arguments[0]){",
				"				str = str.replace(new RegExp('\\\\{' + i +'\\\\}', 'g'), String(arguments[0][i])); // 大小写敏感",
				"		}}", "		", "		return str;", "	};", "}",
				"// 函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128", "bf = {};",
				"bf.Function_delegate = function () {",
				"    var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = \'function\';",
				"    return function(){",
				"        var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;",
				"        // mission one:", "        for (var i = 0; i < Length; i++)",
				"            if (arguments[i])args[i] = arguments[i]; // 拷贝参数",
				"        args.length = Length; // 在 MS jscript下面，arguments作为数字来使用还是有问题，就是length不能自动更新。修正如左:",
				"        // mission two:", "        for (var i = 0, j = args.length; i < j; i++) {",
				"            var _arg = args[i];",
				"            if (_arg && typeof _arg == fnToken && _arg.late == true)",
				"                args[i] = _arg.apply(scope || this, args);", "        }",
				"        return self.apply(scope || this, args);", "    };", "};" };
		baseJavaScriptCode = StringUtil.stringJoin(str, "\n");
	}
}
