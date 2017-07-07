package com.ajaxjs.js;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Value;
import com.ajaxjs.util.reflect.BeanUtil;

/**
 * json 转为 java 对象的工具类，利用了 JVM 自带的 js 引擎
 * @author xinzhang
 *
 */
public class JsonHelper extends JSON {
	public JsonHelper(){
		
	}
	
	public JsonHelper(String jsonString){
		super(jsonString);
	}
	
	public JsonHelper(ScriptEngine jsengine){
		super(jsengine);
	}
	
	/**
	 * 输入一个 Map，将其转换为 JSON Str
	 * 
	 * @param map
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyMap(Map<String, ?> map) {
		if (map == null)
			return null;

		List<String> arr = new ArrayList<>();
		for (String key : map.keySet())
			arr.add('\"' + key + "\":" + Value.obj2jsonVaule(map.get(key)));
	
		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}
	
	/**
	 * 输入一个 List<Map<String, Object>>，将其转换为 JSON Str
	 * 
	 * @param list
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringifyListMap(List<Map<String, Object>> list) {
		if (null == list)
			return null;
		
		String[] str = new String[list.size()];
		
		for (int i = 0; i < list.size(); i ++)
			str[i] = stringifyMap(list.get(i));
		
		return "[" + StringUtil.stringJoin(str, ",") + "]";
	}
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public static String bean2json(Object bean) {
		return stringifyMap(BeanUtil.bean2Map(bean));
	}
	
	public static <T> T json2bean(String json, Class<T> clz) {
		Map<String, Object> map = new JSON(json).setDeep(true).getMap(null);
		return BeanUtil.map2Bean(map, clz, true);
	}
	
	/**
	 * 借助 js 序列化对象为 json
	 * 明明有 jsonString 为何还要 stringify？
	 * 
	 * @return JSON 字符串
	 */
	@Deprecated
	public String stringify() {
		return eval("JSON.stringify(" + getJsonString() + ");", String.class);
	}
	
	/**
	 * 借助 js 序列化对象为 json
	 * 
	 * @param key
	 *            js 表达式
	 * @return JSON 字符串
	 */
	public String stringify(String key) {
		return eval("JSON.stringify(" + key + ");", String.class);
	}

	/**
	 * 借助 js 序列化对象为 json
	 * stringify() 不能传对象，故使用这方法
	 * @param obj
	 *            NativeArray | NativeObject 均可
	 * @return JSON 字符串
	 */
	public String stringifyObj(Object obj) {
		return call("stringify", String.class, eval("JSON"), obj);
	}

	/**
	 * 将 Simple Object 对象转换成 JSON 格式的字符串:JAVA-->JS
	 * 
	 * @param obj
	 *            输入数据
	 * @return JSON 字符串
	 */
	public static String stringify_object(Object obj) {
		if (obj == null)
			return null;
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
	
			arr.add('\"' + key + "\":" + Value.obj2jsonVaule(_obj));
		}
	
		return '{' + StringUtil.stringJoin(arr, ",") + '}';
	}

	/**
	 * 格式化 JSON，使其美观输出到控制或其他地方 请注意 对于json中原有\n \t 的情况未做过多考虑 得到格式化json数据 退格用\t
	 * 换行用\r TODO 用原生实现
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
			if (level > 0 && '\n' == str.charAt(str.length() - 1))
				str.append(StringUtil.repeatStr("\t", "", level));
	
			switch (c) {
			case '{':
			case '[':
				str.append(c + "\n");
				level++;
				break;
			case ',':
				if (json.charAt(i + 1) == '"')
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

}
