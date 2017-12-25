package com.ajaxjs.simpleApp.service;

import java.util.List;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.simpleApp.dao.CatalogDao;
import com.ajaxjs.simpleApp.model.Catalog;

public class CatalogServiceImpl implements CatalogService {
	CatalogDao dao = new DaoHandler<CatalogDao>().bind(CatalogDao.class);

	@Override
	public Catalog findById(Long id) throws ServiceException {
		return null;
	}

	@Override
	public Long create(Catalog bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(Catalog bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Catalog bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Catalog> findPagedList(QueryParams params) throws ServiceException {
		return null;
	}

	@Override
	public PageResult<Catalog> findPagedList(int start, int limit) throws ServiceException {
		return null;
	}

	@Override
	public String getName() {
		return "catalog";
	}

	@Override
	public String getTableName() {
		return "类别";
	}

	@Override
	public List<Catalog> findAll(QueryParams param) {
		return dao.findAll(param);
	}

}