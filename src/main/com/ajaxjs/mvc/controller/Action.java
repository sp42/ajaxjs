package com.ajaxjs.mvc.controller;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * A action = controller + methods
 * 
 * @author admin
 *
 */
public class Action {
	/**
	 * 完整路径
	 */
	public String path;
	
	/**
	 * 控制器实例，方便反射时候跳用
	 */
	public IController controller;
	
	/**
	 * 该路径的 get 请求时对应的控制器方法
	 */
	public Method getMethod;
	
	/**
	 * 该路径的 get 请求时对应的控制器方法
	 */
	public Method postMethod;
	
	/**
	 * 该路径的 put 请求时对应的控制器方法
	 */
	public Method putMethod;
	
	/**
	 * 该路径的 delete 请求时对应的控制器方法
	 */
	public Method deleteMethod;
	
	/**
	 * 下级路径集合
	 */
	public Map<String, Action> children;
}
