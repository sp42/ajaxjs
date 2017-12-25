package com.ajaxjs.simpleApp.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.dao.DaoHandler;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.jdbc.PageResult;
import com.ajaxjs.simpleApp.dao.NewsDao;

public class HrServiceImpl implements NewsService {
	NewsDao dao = new DaoHandler<NewsDao>().bind(NewsDao.class);

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
	public PageResult<Map<String, Object>> findPagedList(QueryParams params) throws ServiceException {
		return dao.findPagedList(params);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) throws ServiceException {
		return dao.findPagedList(new QueryParams(start, limit));
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
	public List<Map<String, Object>> getTop5() {
		return dao.getTop5();
	}

}
