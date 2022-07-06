package com.ajaxjs.entity;

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
	public static <T, K> T create(Identity<K> bean, IDataService<T> dao) {
		Object obj = dao.create((T) bean);

		if (obj == null)
			throw new NullPointerException("创建失败");
		else {
			K newly = (K) obj;
			bean.setId(newly);

			return (T) bean;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, K> boolean update(Identity<K> bean, IDataService<T> dao) {
		if (bean.getId() == null)
			throw new IllegalArgumentException("缺少 id 参数，不知道修改哪条记录");

		return dao.update((T) bean);
	}
}
