package com.ajaxjs.config;

import java.util.Map;

import javax.servlet.ServletContext;

/**
 * 配置业务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface ConfigService {
	/**
	 * 加载配置
	 * 
	 * @param ctx
	 */
	public void init(ServletContext ctx);
	
	/**
	 * 加载 JSON 配置
	 * 
	 * @param cfgPath 配置文件所在路径
	 */
	public void load(String cfgPath);

	/**
	 * 后台编辑 JSON 时候所需要的数据：一个是配置本身，另外是配置说明（Scheme）
	 */
	public String[] loadEdit();

	/**
	 * 保存配置
	 * 
	 * @param map 前端提交过来的配置数据
	 * @param ctx Web 上下文，配置保存在这里
	 */
	public void saveAllconfig(Map<String, Object> map, ServletContext ctx);

	/**
	 * 所有的配置保存在这个 config 中
	 */
	public Map<String, Object> getConfig();

	/**
	 * 所有的配置保存在这个 config 中（扁平化处理过的）
	 */
	public Map<String, Object> getFlatConfig();

	/**
	 * 是否加载成功
	 */
	public boolean isLoaded();
}
