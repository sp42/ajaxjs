package com.ajaxjs.cms.service;

import java.util.Map;

import com.ajaxjs.cms.dao.DataDictDao;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

@Bean(value = "DataDictService", aop = {CommonService.class,  GlobalLogAop.class })
public class DataDictServiceImpl implements DataDictService {
	DataDictDao dao = new DaoHandler<DataDictDao>().bind(DataDictDao.class);

	@Override
	public Map<String, Object> findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Integer create(Map<String, Object> bean) {
		return dao.create(bean);
	}

	@Override
	public int update(Map<String, Object> bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Map<String, Object> bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams params, int start, int limit) {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "数据字典";
	}

	@Override
	public String getTableName() {
		return DataDictDao.tableName;
	}
}
