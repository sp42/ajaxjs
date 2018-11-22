package com.ajaxjs.cms.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.orm.dao.QueryParams;

public interface CatalogService extends IService<Catalog, Long> {
	/**
	 * 获取所有分类
	 * 
	 * @param param
	 * @return
	 */
	public List<Catalog> findAll(QueryParams param);

	/**
	 * 根据父 id 获取下一层的子分类列表，获取直接一层的分类。
	 * 
	 * @param id 父 id
	 * @return 子分类列表
	 * @throws ServiceException
	 */
	public List<Catalog> findByParentId(int id) throws ServiceException;

	/**
	 * 根据父 id 获取所有的子id列表（不包含父节点），不管多少层
	 * 
	 * @param parentId 父 id
	 * @param isWithParent 是否需要连同父节点一起返回
	 * @return 所有的子id列表
	 */
	public List<Catalog> getAllListByParentId(int parentId, boolean isWithParent);

	/**
	 * 根据父 id 获取所有的子id列表（包含父节点），不管多少层
	 * 
	 * @param parentId 父 id
	 * @return
	 */
	public List<Catalog> getAllListByParentId(int parentId);

	/**
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Map<String, Object>> getListAndSubByParentId(int parentId);
}