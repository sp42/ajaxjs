package com.ajaxjs.cms.app.catalog;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
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
	
	public final static String pathLike_mysql = " FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = %d ) , '%%'))";
	
	/**
	 * IN 查询用
	 */
	public final static String CATALOG_FIND = "e.catalogId IN (SELECT id " + pathLike_mysql + ")";
	
	/**
	 * 
	 * @param catelogId
	 * @param service
	 * @return
	 */
	public static Function<String, String> setCatalog(int catelogId, Catalogable<?> service) {
		if (catelogId == 0)
			catelogId = service.getDomainCatelogId();
		
		return BaseService.setSqlHandler(String.format(CATALOG_FIND, catelogId));
	}
}
