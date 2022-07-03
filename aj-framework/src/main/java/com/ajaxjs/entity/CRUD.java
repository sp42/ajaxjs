package com.ajaxjs.entity;

import java.io.Serializable;

import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.framework.Identity;

/**
 * 通用的 CRUD 业务方法
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class CRUD {
	/**
	 * 创建实体
	 * 
	 * @param <T>
	 * @param bean
	 * @param dao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T create(Identity bean, IDataService<T> dao) {
		Serializable newly = dao.create((T) bean);
		if (newly == null)
			throw new NullPointerException("创建失败");

		bean.setId(newly);

		return (T) bean;
	}

	@SuppressWarnings("unchecked")
	public static <T> boolean update(Identity bean, IDataService<T> dao) {
		if (bean.getId() == null)
			throw new IllegalArgumentException("缺少 id 参数，不知道修改哪条记录");

		return dao.update((T) bean);
	}
}
