package com.ajaxjs.framework;

/**
 * 获取配置。base 层不提供实现，于是给出一个接口耦合
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface GetConfig {
	/**
	 * 读取配置并转换其为字符串类型。仅对扁平化后的配置有效，所以参数必须是扁平化的 aaa.bbb.ccc 格式。
	 * 
	 * @param key 配置键值
	 * @return 配置内容
	 */
	public String getString(String key);
}
