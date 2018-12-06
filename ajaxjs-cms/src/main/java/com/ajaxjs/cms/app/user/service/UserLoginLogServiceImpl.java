package com.ajaxjs.cms.app.user.service;

import java.util.List;

import com.ajaxjs.cms.app.user.dao.UserLoginLogDao;
import com.ajaxjs.cms.app.user.model.UserLoginLog;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "User_login_logService")
public class UserLoginLogServiceImpl implements UserLoginLogService {
	UserLoginLogDao dao = new DaoHandler().bind(UserLoginLogDao.class);

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

	@Override
	public List<UserLoginLog> findList() {
		return null;
	} 

}