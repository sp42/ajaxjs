package com.ajaxjs.cms.app.user.service;

import java.util.List;

import com.ajaxjs.cms.app.user.dao.UserAdminDao;
import com.ajaxjs.cms.app.user.model.User;
import com.ajaxjs.framework.service.CommonService;
import com.ajaxjs.framework.service.GlobalLogAop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

@Bean(value = "UserAdminService", aop = { CommonService.class, GlobalLogAop.class })
public class UserAdminServiceImpl implements UserAdminService {
	public static UserAdminDao dao = new DaoHandler().bind(UserAdminDao.class);

	@Override
	public User findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(User bean) {
		return dao.create(bean);
	}

	@Override
	public int update(User bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(User bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<User> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "用户";
	}

	public String getTableName() {
		return "user";
	}

	@Override
	public List<User> findList() {
		return null;
	}
}
