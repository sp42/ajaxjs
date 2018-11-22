package com.ajaxjs.user.service;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.dao.UserLoginLogDao;
import com.ajaxjs.user.model.UserLoginLog;

@Bean(value = "User_login_logService")
public class UserLoginLogServiceImpl implements UserLoginLogService {
	UserLoginLogDao dao = new DaoHandler<UserLoginLogDao>().bind(UserLoginLogDao.class);

	@Override
	public UserLoginLog findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(UserLoginLog bean) {
		return dao.create(bean);
	}

	@Override
	public int update(UserLoginLog bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(UserLoginLog bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<UserLoginLog> findPagedList(QueryParams params, int start, int limit) {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<UserLoginLog> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getTableName() {
		return "user_login_log";
	} 

}