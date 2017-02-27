package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.util.List;

import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.model.PageResult;

/**
 * 业务基类
 * @author xinzhang
 *
 * @param <T>
 * @param <ID>
 */
public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
	/**
	 * 数据访问对象
	 */
	private BaseDao<T, ID> dao;

	/**
	 * 业务名称
	 */
	private String name;

	@Override
	public T findById(ID id) throws ServiceException { 
		if (id instanceof Number && ((Number) id).intValue() < 0)
			throw new IllegalArgumentException("实体 id 不能小于零");
		
		return dao.findById(id);
	}
	
	@Override
	public PageResult<T> find(int start, int limit) throws ServiceException {
		PageCallback<T, BaseDao<T, ID>> pageCallback = new PageCallback<T, BaseDao<T, ID>>(){
			@Override
			public int getTotal(BaseDao<T, ID> dao) {
				return dao.count();
			}

			@Override
			public List<T> getList(int start, int limit, BaseDao<T, ID> dao) {
				return dao.findList(start, limit);
			}
		};
		
		return pageHelper(start, limit, pageCallback);
	}
	
	/**
	 * 分页帮助方法
	 * @param start
	 * @param limit
	 * @param pageCallback
	 * @return 分页对象
	 * @throws ServiceException
	 */
	public PageResult<T> pageHelper(int start, int limit, PageCallback<T, BaseDao<T, ID>> pageCallback) throws ServiceException {
		if (start < 0 || limit < 0)
			throw new IllegalArgumentException("分页参数非法");
	
		PageResult<T> result = new PageResult<>();
		result.setStart(start);
		result.setPageSize(limit);
		result.setTotalCount(pageCallback.getTotal(dao));// 先查询总数
		
		if (result.getTotalCount() > 0) { // 然后执行分页
			result.page();
			result.setRows(pageCallback.getList(start, limit, dao));
		}
		
		return result;
	}
	
	
	public List<T> find() throws ServiceException {
		return find(0, 99999).getRows();
	}
	
	@Override
	public ID create(T bean) throws ServiceException {
		int effectedRows = 0; // 受影响的行数
		return null;
	}

	@Override
	public int update(T bean) throws ServiceException {
		int effectedRows = 0; // 受影响的行数
		//dao.delete(effectedRows);
		return effectedRows;
	}

	@Override
	public boolean delete(ID id) throws ServiceException {
		return dao.delete(id) == 1;// 受影响的行数
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public BaseDao<T, ID> getDao() {
		return dao;
	}

	public void setDao(BaseDao<T, ID> dao) {
		this.dao = dao;
	}
}
