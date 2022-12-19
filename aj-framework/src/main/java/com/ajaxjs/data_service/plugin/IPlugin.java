package com.ajaxjs.data_service.plugin;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.data_service.DataServiceConstant;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 数据服务的插件，对应 CRUD 若干情况，类似于 AOP 模式，有 before/after
 * 
 * https://www.jb51.net/article/75932.htm
 * 
 * @author Frank Cheung
 *
 */
public interface IPlugin {
	static final LogHelper LOGGER = LogHelper.getLog(IPlugin.class);

	/**
	 * 前期初始化
	 * 
	 * @param type
	 * @param ctx
	 * @return true 表示执行，false 表示不继续执行
	 */
	default boolean onRequest(DataServiceConstant.CRUD type, ServiceContext ctx) {
		return true;
	}

	/**
	 * 操作之前的动作
	 * 
	 * @return true 表示执行，false 表示不继续执行
	 */
	default boolean before(DataServiceConstant.CRUD type, ServiceContext ctx) {
		return true;
	}

	/**
	 * 操作之后的动作，已经获取了数据
	 * 
	 * @param type
	 * @param ctx
	 * @param result
	 */
	default void after(DataServiceConstant.CRUD type, ServiceContext ctx, Object result) {

	}

	/**
	 * 
	 * @param plugins
	 * @param type
	 * @param ctx
	 * @return true 表示执行，false 表示不继续执行
	 */
	public static boolean before(List<IPlugin> plugins, DataServiceConstant.CRUD type, ServiceContext ctx) {
		if (CollectionUtils.isEmpty(plugins))
			return true;

		for (IPlugin plugin : plugins) {
			if (!plugin.before(type, ctx))
				return false;
		}

		return true;
	}

	public static void after(List<IPlugin> plugins, DataServiceConstant.CRUD type, ServiceContext ctx, Object result) {
		if (CollectionUtils.isEmpty(plugins))
			return;

		try { // after 不应该影响本来的流程
			for (IPlugin plugin : plugins)
				plugin.after(type, ctx, result);
		} catch (Throwable e) {
			LOGGER.warning(e);
		}
	}
}
