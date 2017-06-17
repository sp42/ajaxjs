package com.ajaxjs.js;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;

import com.ajaxjs.util.StringUtil;
import com.ajaxjs.util.Value;

/**
 * json 转为 java 对象的工具类，利用了 JVM 自带的 js 引擎
 * @author xinzhang
 *
 */
public class JsonHelper extends JsEngineWrapper {
	public JsonHelper() {
		super();
	}
	
	public JsonHelper(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public JsonHelper(ScriptEngine jsengine) {
		super(jsengine);
	}

	/**
	 * json 字符串，通常从接口返回的
	 */
	private String jsonString;
	
	/**
	 * 是否在 list 将里面的 map 所包含的 double 都转为 int？
	 * 如果设为 false 则性能好很多，但就不方便了，只适合没有数字类型的 json 字段
	 * 另外，如果要转换类似 [{a:'hello'}, 123, true] 这样不是 map 的 list，也要将本项设为 false
	 */
	private boolean isAutoDouble2IntInList = true;
	
	/**
	 * rhino 不能直接返回 map，如 eval("{a:1}")-->null，必须加变量，例如 执行 var xx = {...};
	 * 
	 * @param key
	 *            js 命名空间 JSON Path，可以带有 . 分隔符的，如 aa.bb.cc
	 * @param clazz
	 *            目标类型
	 * @return 目标对象
	 */
	public <T> T accessJsonMember(String key, Class<T> clazz) {
		T result = null;

		String jsCode = "var obj = " + getJsonString();

		eval(jsCode);
			
		if (key == null) {	// 直接返回变量
			jsCode = "obj;";
			result = eval(jsCode, clazz);
		} else {
//			if (key.contains(".")) {
				jsCode = "obj." + key + ";";
				result = eval(jsCode, clazz);
//			} else {
//				jsCode = "obj['" + key + "'];";// TODO 这是多余的？
//				result = eval(jsCode, clazz);
//			}
		} 
			
		return result;
	}

	/**
	 * 读取 json 里面的 map
	 * 
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return Map 对象
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String key) {
		Map<String, Object> map = (Map<String, Object>)accessJsonMember(key, Map.class);
	
		return double2int4Map(map);
	}
	
	/**
	 * 读取 json 里面的 list，list 里面每一个都是 map
	 * 
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return 包含 Map 的列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getList(String key) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) accessJsonMember(key, List.class);
		
		if(isAutoDouble2IntInList()) 
			list = double2int(list);
		
		return list; 
	} 

	/**
	 * js 的数字类型是 double，如果在 List<Map> 里面的话需要遍历之，取出其中的 double 变为 int
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> double2int(List<Map<String, Object>> list) {
		List<Map<String, Object>> _list = new ArrayList<>();

		for (Map<String, Object> map : list) {
			_list.add(double2int4Map(map));
		}

		return _list;
	}

	/**
	 * js 的数字类型是 double，如果在 map 里面的话需要遍历之，取出其中的 double 变为 int
	 * @param map
	 * @return
	 */
	private static Map<String, Object> double2int4Map(Map<String, Object> map) {
		// 转换为真正的 map
		Map<String, Object> realMap = new HashMap<>();
		
		for (String _key : map.keySet()) {
			// js double -->int
			Object obj = map.get(_key);

			if (obj instanceof Double) {
				realMap.put(_key, Value.double2int((Double) map.get(_key)));
			} else {
				realMap.put(_key, map.get(_key));
			}
		}

		return realMap;
	}
	
	/**
	 * 读取 json 里面的 list，list 里面每一个都是 String
	 * 
	 * @param key
	 *            JSON Path，可以带有 aa.bb.cc
	 * @return 包含 String 的列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key) {
		return (List<String>) accessJsonMember(key, List.class);
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
					str.append(c + "\n"); // 后面必定是跟着 key 的双引号，但 其实 json 可以 key
											// 不带双引号的
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

	public String getJsonString() {
		return jsonString;
	}

	public JsonHelper setJsonString(String jsonString) {
		this.jsonString = jsonString;
		return this;
	}

	public boolean isAutoDouble2IntInList() {
		return isAutoDouble2IntInList;
	}

	public void setAutoDouble2IntInList(boolean isAutoDouble2IntInList) {
		this.isAutoDouble2IntInList = isAutoDouble2IntInList;
	}
}
