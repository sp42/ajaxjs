package com.ajaxjs.cms.service;

import java.util.List;

import com.ajaxjs.cms.dao.CatalogDao;
import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;

public class CatalogService extends BaseDaoService<Catalog, Long, CatalogDao> implements IService<Catalog, Long> {
	public CatalogService() {
		setName("catalog");
		setUiName("类别");
		initDao(CatalogDao.class);
	}

	@Override
	public Catalog findById(Long id) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(Catalog bean) throws ServiceException {
		return getDao().create(bean);
	}

	@Override
	public int update(Catalog bean) throws ServiceException {
		return getDao().update(bean);
	}

	@Override
	public boolean delete(Catalog bean) throws ServiceException {
		return getDao().delete(bean);
	}

	@Override
	public PageResult<Catalog> findPagedList(QueryParams parame) throws ServiceException {
		return getDao().findPagedList(parame);
	}
	
	public List<Catalog> getListByParentId(int pId) {
		return getDao().getListByParentId(pId);
	}

}
