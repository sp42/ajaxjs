package com.ajaxjs.framework.domain;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseBean;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.dao.PageResult;

public class DomainService extends BaseService {
	DomainRepository dao = new Repository().bind(DomainRepository.class);

	@Override
	public IBaseBean findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<IBaseBean> findList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<IBaseBean> findPagedList(int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(IBaseBean bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(IBaseBean bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(IBaseBean bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getUiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShortName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
