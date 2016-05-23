package com.ajaxjs.json;

import java.util.Map;

public interface ToJavaType {
	/**
	 * eval() JS 代码，并把返回值转换为具体的 String 类型
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JS 运算后的返回值，字符串类型的值，也可能是 null 没有返回
	 */
	public String eval_return_String(String code);
	
	/**
	 * eval() JS 代码，并把返回值转换为具体的 int 类型
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JS 运算后的返回值，整形类型的值，也可能是 null 没有返回
	 */
	public int eval_return_Int(String code);
	
	/**
	 * eval() JS 代码，并把返回值转换为具体的 boolean 类型
	 * 
	 * @param code
	 *            JS 代碼
	 * @return JS 运算后的返回值，布尔类型的值，也可能是 null 没有返回
	 */
	public boolean eval_return_Boolean(String code);
	
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
	public Map<String, Object> eval_return_Map(String code, String varName);
	
	/**
	 * 如果需要返回 {a:xx, b:xx} JSON 这样 JSON 的结构，要把这个 JSON 赋予一个全局变量
	 * 输入 "{'a':0, 'b':1}" 字符串返回 Map
	 * @param code
	 *            JS 脚本代码
	 * @return  Java 里的 map
	 */
	public Map<String, Object> eval_return_Map(String code);
	
	/**
	 * 输入 Js Array 代码（字符串），返回 Java Map List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 String[]
	 */
	public String[] eval_return_StringArray(String code);
	
	/**
	 * 输入 Js Array 代码（字符串），返回 Java Map List
	 * 
	 * @param code
	 *            JS 代碼
	 * @return Java 里的 Map<String, Object>[]
	 */
	public Map<String, Object>[] eval_return_MapArray(String code);
}
