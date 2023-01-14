package com.ajaxjs.fast_doc;

/**
 * 运行配置参数
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class FastDocRun {
	/**
	 * 源码目录
	 */
	public String sourceDir;

	/**
	 * Java 实体类引用集合
	 */
	public Class<?>[] beanClasses;

	/**
	 * Spring MVC 控制器类引用集合
	 */
	public Class<?>[] controllerClasses;

	/**
	 * 报错 JSON 的目录
	 */
	public String jsonDir;
}
