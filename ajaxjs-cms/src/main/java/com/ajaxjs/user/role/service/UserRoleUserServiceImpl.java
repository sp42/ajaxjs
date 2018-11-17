package com.ajaxjs.user.role.service;

import java.util.Map;

import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.role.dao.UserRoleUserDao;

@Bean(value = "UserRoleUserService")
public class UserRoleUserServiceImpl implements UserRoleUserService {
	UserRoleUserDao dao = new DaoHandler<UserRoleUserDao>().bind(UserRoleUserDao.class);

	@Override
	public Map<String, Object> findById(Integer id) throws ServiceException {
		return dao.findById(id);
	}

	@Override
	public Integer create(Map<String, Object> bean) throws ServiceException {
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
		return "后台用户";
	}

	@Override
	public String getTableName() {
		return "user_admin";
	} 

}