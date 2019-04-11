package com.ajaxjs.cms.app.catelog;

import java.util.List;

import com.ajaxjs.framework.PageResult;

/**
 * 可分类的服务
 * 
 * @author Frank Cheung
 *
 * @param <T> 实体类型
 */
public interface Catalogable<T> {
	/**
	 * 获取指定类别的数据
	 * 
	 * @param catelogId 类别 id
	 * @return
	 */
	public List<T> findListByCatelogId(int catelogId);

	/**
	 * 获取指定类别的数据，这是可分页的
	 * 
	 * @param catelogId 类别 id
	 * @param start
	 * @param limit
	 * @return
	 */
	public PageResult<T> findPagedListByCatelogId(int catelogId, int start, int limit);

	/**
	 * 返回实体最顶层的域 id
	 * 
	 * @return 域id
	 */
	public int getDomainCatelogId();
}
