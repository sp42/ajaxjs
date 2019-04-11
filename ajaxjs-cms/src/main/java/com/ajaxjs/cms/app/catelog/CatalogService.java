package com.ajaxjs.cms.app.catelog;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.IBaseService;

public interface CatalogService extends IBaseService<Catalog> {
	/**
	 * 获取所有分类
	 * 
	 * @param param
	 * @return
	 */
	public List<Catalog> findAll();

	/**
	 * 根据父 id 获取下一层的子分类列表，获取直接一层的分类。
	 * 
	 * @param id 父 id
	 * @return 子分类列表
	 * @throws ServiceException
	 */
	public List<Catalog> findByParentId(int id);

	/**
	 * 根据父 id 获取所有的子id列表（可以不包含父节点），不管多少层
	 * 
	 * @param parentId 父 id
	 * @param isWithParent 是否需要连同父节点一起返回
	 * @return 所有的子id列表
	 */
	public List<Catalog> findAllListByParentId(int parentId, boolean isWithParent);

	/**
	 * 根据父 id 获取所有的子id列表（包含父节点），不管多少层
	 * 
	 * @param parentId 父 id
	 * @return
	 */
	public List<Catalog> findAllListByParentId(int parentId);

	/**
	 * 获取下一级和下下一级，一共只获取这两级
	 * 
	 * @param parentId 父 id
	 * @return
	 */
	public List<Map<String, Object>> findListAndSubByParentId(int parentId);
}
