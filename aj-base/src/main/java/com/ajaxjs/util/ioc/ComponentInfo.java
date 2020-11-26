package com.ajaxjs.util.ioc;

/**
 * 组件信息
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class ComponentInfo {
	/**
	 * 命名空间 = 包名+类型
	 */
	public String namespace;

	/**
	 * 别名，全局唯一
	 */
	public String alias;

	/**
	 * 实例，当为 null 的时候表示单例模式
	 */
	public Object instance;

	/**
	 * 类引用
	 */
	public Class<?> clazz;
}
