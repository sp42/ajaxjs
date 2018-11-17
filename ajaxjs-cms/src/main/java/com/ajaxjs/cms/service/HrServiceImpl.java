package com.ajaxjs.cms.service;

import java.util.Map;

import com.ajaxjs.cms.dao.HrDao;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

@Bean(value = "HrService", aop = { CommonService.class })
public class HrServiceImpl implements HrService {
	HrDao dao = new DaoHandler<HrDao>().bind(HrDao.class);

	@Override
	public Map<String, Object> findById(Long id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Long create(Map<String, Object> bean) throws ServiceException {
		return dao.create(bean);
	}

	@Override
	public int update(Map<String, Object> bean) throws ServiceException {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Map<String, Object> bean) throws ServiceException {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams params, int start, int limit) throws ServiceException {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) throws ServiceException {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "招聘";
	}

	@Override
	public String getTableName() {
		return "hr";
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, QueryParams param, int start, int limit) {
		return dao.findPagedListByCatalogId(catalogId, param, start, limit);
	}
}
