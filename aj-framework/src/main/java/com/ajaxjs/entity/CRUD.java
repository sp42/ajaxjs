package com.ajaxjs.entity;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.framework.Identity;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.regexp.RegExpUtils;

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
	 * @param      <T>
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
			if (obj instanceof String)
				obj = Long.parseLong((String) obj);

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

	/**
	 * 
	 * @param         <T>
	 * @param id
	 * @param dao
	 * @param beanClz
	 * @return
	 */
	public static <T extends Identity<Long>> boolean delete(Long id, IDataService<T> dao, Class<T> beanClz) {
		T bean = ReflectUtil.newInstance(beanClz);
		bean.setId(id);

		return dao.delete(bean);
	}

	/**
	 * 取出实体中 id
	 * 
	 * @param dicts
	 * @return
	 */
	public static Long[] getIds(List<? extends Identity<Long>> dicts) {
		if (CollectionUtils.isEmpty(dicts))
			return new Long[0];
		else {
			Long[] ids = new Long[dicts.size()];

			int i = 0;
			for (Identity<Long> bean : dicts)
				ids[i++] = bean.getId();

			return ids;
		}
	}

	/**
	 * 查询列表。如果无记录，返回空列表
	 * 
	 * @param dao DAO
	 * @return 列表
	 */
	public static <T> List<T> findList(IDataService<T> dao) {
		List<T> list = dao.findList();

		return CollectionUtils.isEmpty(list) ? Collections.emptyList() : list;
	}

	/**
	 * 返回下一个序号
	 * 
	 * @param maxId 当前最大的序号
	 * @param width 序号宽度
	 * @return
	 */
	public static String getNextCode(String maxId, int width) {
		String dig = "";

		if (maxId != null) {
			dig = RegExpUtils.regMatch("\\d+$", maxId);
			int i = Integer.parseInt(dig);
			dig = (++i) + "";
		} else
			dig = "1";

		return StrUtil.leftPad(dig, width, "0");
	}
}
