package com.ajaxjs.web.simple_admin;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;

public class NewsServiceImpl implements NewsService {

	@Override
	public Catalog findById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(Catalog bean) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Catalog bean) throws ServiceException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(Catalog bean) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PageResult<Catalog> findPagedList(QueryParams params) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<Catalog> findPagedList(int start, int limit) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "新闻";
	}

	@Override
	public String getTableName() {
		return "news";
	}

}
