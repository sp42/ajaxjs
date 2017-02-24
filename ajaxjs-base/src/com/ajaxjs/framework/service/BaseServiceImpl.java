package com.ajaxjs.framework.service;

import java.io.Serializable;
import java.util.List;

import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.exception.BusinessException;
import com.ajaxjs.framework.exception.DaoException;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.PageResult;

public class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {
	private BaseDao<T, ID> dao;
	
	public BaseDao<T, ID> getDao() {
		return dao;
	}

	public void setDao(BaseDao<T, ID> dao) {
		this.dao = dao;
	}

	private String name;

	@Override
	public T findById(ID id) throws ServiceException { 
		try {
			if (id instanceof Number && ((Number) id).intValue() < 0)
				throw new IllegalArgumentException("实体 id 不能小于零");
		} catch (Throwable e) {
			throw new BusinessException(e.getMessage());
		}

		T entry = null;
		
		try {
			entry = dao.findById(id);
		} catch (Throwable e) {
			throw new DaoException(e.getMessage());
		}
		
		return entry;
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
	
	 
	public PageResult<T> pageHelper(int start, int limit, PageCallback<T, BaseDao<T, ID>> pageCallback) throws ServiceException {
		try {
			if (start < 0 || limit < 0)
				throw new IllegalArgumentException("分页参数非法");
		} catch (Throwable e) {
			throw new BusinessException(e.getMessage());
		}
		
		PageResult<T> result = new PageResult<>();
		result.setStart(start);
		result.setPageSize(limit);
		
		try { 
			result.setTotalCount(pageCallback.getTotal(dao));// 先查询总数
			
			if (result.getTotalCount() > 0) { // 然后执行分页
				result.page();
				result.setRows(pageCallback.getList(start, limit, dao));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		}
		
		return result;
	}
	
	public List<T> find() throws ServiceException {
		PageResult<T> entries = find(0, 99999);
		return entries.getRows();
	}
	
	@Override
	public ID create(T bean) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(T bean) throws ServiceException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(ID id) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int count(BaseDao<T, ID> dao) throws ServiceException {
		// TODO Auto-generated method stub
		return 0;
	}

}
