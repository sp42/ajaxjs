package com.ajaxjs.cms.app.catelog;

import java.util.List;

import com.ajaxjs.orm.dao.PageResult;

/**
 * 可分类的服务
 * 
 * @author Frank Cheung
 *
 * @param <T> 实体类型
 */
public interface Catelogable<T> {
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

	/**
	 * 用于 catelogId 查询的，通常放在 LEFT JOIN 后面还需要，WHERE e.catelog = c.id。 
	 * 还需要预留一个 catelogId 的参数
	 * 另外也可以用 IN 查询
	 * 
	 */
	public final static String getByCatelogId = " (SELECT id, name FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AS c ";
}
