package com.ajaxjs.user.service;

import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.dao.UserOauthDao;
import com.ajaxjs.user.model.UserOauth;

@Bean(value = "User_oauthService")
public class UserOauthServiceImpl implements UserOauthService {
	UserOauthDao dao = new DaoHandler<UserOauthDao>().bind(UserOauthDao.class);

	@Override
	public UserOauth findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(UserOauth bean) {
		return dao.create(bean);
	}

	@Override
	public int update(UserOauth bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(UserOauth bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<UserOauth> findPagedList(QueryParams params, int start, int limit) {
		return dao.findPagedList(params, start, limit);
	}

	@Override
	public PageResult<UserOauth> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getTableName() {
		return "user_oauth";
	} 

}