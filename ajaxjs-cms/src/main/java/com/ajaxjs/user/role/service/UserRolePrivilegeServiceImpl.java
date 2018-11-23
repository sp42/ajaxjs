package com.ajaxjs.user.role.service;

import java.util.Map;

import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;
import com.ajaxjs.user.role.dao.UserRolePrivilegeDao;

@Bean(value = "UserRolePrivilegeService", aop = {CommonService.class,  GlobalLogAop.class })
public class UserRolePrivilegeServiceImpl implements UserRolePrivilegeService {
	UserRolePrivilegeDao dao = new DaoHandler<UserRolePrivilegeDao>().bind(UserRolePrivilegeDao.class);

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
		return "权限";
	}

	@Override
	public String getTableName() {
		return "user_admin_privilege";
	} 

}