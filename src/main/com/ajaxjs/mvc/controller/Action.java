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
	public Method getMethod;
	public Method postMethod;
	public Method putMethod;
	public Method deleteMethod;
	
	/**
	 * 下级路径集合
	 */
	Map<String, Action> children;
}
