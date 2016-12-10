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
package com.ajaxjs.util.json;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.ajaxjs.util.StringUtil;

public class JsLib {
	
	/**
	 * // 新建一个 js mapper 专用于 json 转换，以免影响 其他 js runtime
	 */
	private static ScriptEngine engine = JSON.engineFactory();

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
				"        return self.apply(scope || this, args);", "    };", "};",
				 "    /** ",
				 "     * 日期格式化。详见博客文章：http://blog.csdn.net/zhangxin09/archive/2011/01/01/6111294.aspx ",
				 "     * e.g: new Date().format(\"yyyy-MM-dd hh:mm:ss\") ",
				 "     * @param   {String} format ",
				 "     * @return  {String} ",
				 "    */  ",
				 "    Date.prototype.format = function (format) {  ",
				 "        var $1, o = {  ",
				 "            \"M+\": this.getMonth() + 1,      // 月份，从0开始算  ",
				 "            \"d+\": this.getDate(),           // 日期  ",
				 "            \"h+\": this.getHours(),          // 小时  ",
				 "            \"m+\": this.getMinutes(),        // 分钟  ",
				 "            \"s+\": this.getSeconds(),        // 秒钟  ",
				 "                                            // 季度 quarter  ",
				 "            \"q+\": Math.floor((this.getMonth() + 3) / 3),  ",
				 "            \"S\": this.getMilliseconds() // 千秒  ",
				 "        };  ",
				 "        var key, value;  ",
				 "      ",
				 "        if (/(y+)/.test(format)) {  ",
				 "            $1 = RegExp.$1,   ",
				 "            format = format.replace($1, String(this.getFullYear()).substr(4 - $1));  ",
				 "        }  ",
				 "      ",
				 "        for (key in o) { // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。  ",
				 "            if (new RegExp(\"(\" + key + \")\").test(format)) {  ",
				 "                $1      = RegExp.$1,  ",
				 "                value   = String(o[key]),  ",
				 "                value   = $1.length == 1 ? value : (\"00\" + value).substr(value.length),  ",
				 "                format  = format.replace($1, value);  ",
				 "            }  ",
				 "        }  ",
				 "        return format;  ",
				 "    }  "		
		
		};
		baseJavaScriptCode = StringUtil.stringJoin(str, "\n");

		/**
		 * bean 转换到 Json 用的脚本，在 js 里面做比较方便
		 */
		final String[] a = {
				 "    /** ",
				 "     * 日期格式化。详见博客文章：http://blog.csdn.net/zhangxin09/archive/2011/01/01/6111294.aspx ",
				 "     * e.g: new Date().format(\"yyyy-MM-dd hh:mm:ss\") ",
				 "     * @param   {String} format ",
				 "     * @return  {String} ",
				 "    */  ",
				 "    Date.prototype.format = function (format) {  ",
				 "        var $1, o = {  ",
				 "            \"M+\": this.getMonth() + 1,      // 月份，从0开始算  ",
				 "            \"d+\": this.getDate(),           // 日期  ",
				 "            \"h+\": this.getHours(),          // 小时  ",
				 "            \"m+\": this.getMinutes(),        // 分钟  ",
				 "            \"s+\": this.getSeconds(),        // 秒钟  ",
				 "                                            // 季度 quarter  ",
				 "            \"q+\": Math.floor((this.getMonth() + 3) / 3),  ",
				 "            \"S\": this.getMilliseconds() // 千秒  ",
				 "        };  ",
				 "        var key, value;  ",
				 "      ",
				 "        if (/(y+)/.test(format)) {  ",
				 "            $1 = RegExp.$1,   ",
				 "            format = format.replace($1, String(this.getFullYear()).substr(4 - $1));  ",
				 "        }  ",
				 "      ",
				 "        for (key in o) { // 如果没有指定该参数，则子字符串将延续到 stringvar 的最后。  ",
				 "            if (new RegExp(\"(\" + key + \")\").test(format)) {  ",
				 "                $1      = RegExp.$1,  ",
				 "                value   = String(o[key]),  ",
				 "                value   = $1.length == 1 ? value : (\"00\" + value).substr(value.length),  ",
				 "                format  = format.replace($1, value);  ",
				 "            }  ",
				 "        }  ",
				 "        return format;  ",
				 "    }  "	,
			"/**",
			 " * Java 类型转换为 js 类型",
			 " * @param v",
			 " * @returns",
			 " */",
			 "function java_value2js_value(v) {",
			 "	if (v == null)",
			 "		return null;",
			 "	if(typeof v == 'boolean')return v;",
			 "	if (v.getClass) {",
			 "		switch (v.getClass()) {",
			 "		case java.util.Date:",
			 "			return new Date(v).format('yyyy-MM-dd hh:mm:ss');",
			 "			break;",
			 "		case java.lang.Boolean:",
			 "			return v == 0 || v == false ? true : false;",
			 "			break;",
			 "		case java.lang.String:",
			 "			return String(v);",
			 "			break;",
			 "		case java.lang.Integer:",
			 "		case java.lang.Long:",
			 "		case java.lang.Number:",
			 "			return Number(v);",
			 "			break;",
			 "		default:",
			 "if(v.getClass().isArray()){if(v[0] == null)return [];if(v[0].getClass() == java.lang.String){",
			 "var arr =[]; for(var i = 0; i < v.length; i++){arr.push(new String(v[i]));};return arr;}}",
			 " else if(v[0].getClass() == java.lang.Integer || v[0].getClass() == java.lang.Long){var arr =[]; for(var i = 0; i < v.length; i++){arr.push(new Number(v[i]));};return arr;}",
			 "else",
			 "			return String(v);",
			 "		}",
			 "	} else if (Number(v) == v) {",
			 "		return Number(v);",
			 "	} else if (Boolean(v) == v) {",
			 "		if(v==0)return false; if(v==1)return true; return Boolean(v);",
			 "	} else {",
			 "		return null;",
			 "	}",
			 "}",
			 "var hasGetterReg = /^(get|is)/;",
			 "function pojo2json(pojo) {",
			 "	var obj = {};",
			 "	for ( var k in pojo) {",
			 "		var value, hasGetter = hasGetterReg.test(k);",
			 "		if (hasGetter && k != \'getClass\') {",
			 "			if(!pojo[k] || typeof(pojo[k]) != \'function\')continute;"
			 + "try{value = pojo[k]();}catch(e){println(k);}",
			 "			var fieldName;",
			 "			if (k[0] == 'g') {",
			 "				fieldName = k.replace(\'get\', \'\');",
			 "			} else {",
			 "				fieldName = k.replace(\'is\', \'\');",
			 "			}",
			 "			// 第一个字母转为小写",
			 "			fieldName = fieldName.charAt(0).toLowerCase() + fieldName.slice(1);",
			 "			obj[fieldName] = java_value2js_value(value);",
			 "		}",
			 "	}",
			 "	return obj;",
			 "}",
			 "function test(list) {",
			 "	var j = list.size(), str = new Array(j);",
			 "	for (var i = 0; i < j; i++) {",
			 "		var pojo = list.get(i);",
			 "		str[i] = pojo2json(pojo);",
			 "	}",
			 "	return JSON.stringify(str);",
			 "}; function singlePojo(pojo){return JSON.stringify(pojo2json(pojo));}"
		};
		
		final String jsStr = StringUtil.stringJoin(a, "\n");
		
		try {
			engine.eval(jsStr);
		} catch (ScriptException e) {
			System.err.println("插入 java2js 的脚本错误");
			e.printStackTrace();
		}
	}

	/**
	 * 多个 pojo 转换为 json
	 * 
	 * @param list
	 *            pojo 列表
	 * @return json 字符串
	 */
	public static String list(List<?> list) {
		return list == null || list.isEmpty() || list.size() == 0 ? "null"
				: new JsonHelper(engine).call("test", String.class, null, list);
	}

	/**
	 * 单个 pojo 转换为 json
	 * 
	 * @param obj
	 *            pojo、bean
	 * @return json 字符串
	 */
	public static String singlePojo(Object obj) {
		return new JsonHelper(engine).call("singlePojo", String.class, null, obj);
	}
}
