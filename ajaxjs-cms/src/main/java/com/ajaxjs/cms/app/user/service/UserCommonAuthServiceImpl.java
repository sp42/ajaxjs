package com.ajaxjs.cms.app.user.service;

import java.util.List;

import com.ajaxjs.cms.app.user.dao.UserCommonAuthDao;
import com.ajaxjs.cms.app.user.model.UserCommonAuth;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

@Bean(value = "User_common_authService")
public class UserCommonAuthServiceImpl implements UserCommonAuthService {
	private static final LogHelper LOGGER = LogHelper.getLog(UserCommonAuthServiceImpl.class);
	public static UserCommonAuthDao dao = new DaoHandler().bind(UserCommonAuthDao.class);

	@Override
	public UserCommonAuth findById(Long id) {
		return dao.findById(id);
	}

	/**
	 * 默认的加盐字符串
	 */
	public static final String salt = "dasdsad312";

	@Override
	public Long create(UserCommonAuth bean) {
		System.out.println(bean.getPassword());
		bean.setPassword(encode(bean.getPassword()));
		bean.setRegisterIp(MvcRequest.getMvcRequest().getIp());

		System.out.println("saved:" + bean.getPassword());
		return dao.create(bean);
	}

	public static String encode(String p) {
//		System.out.println("p:::::::::::::::::::::::::" + p);
//		System.out.println("encode:::::::::::::::::::::::::" + Encode.md5(p + salt));
		return Encode.md5(p.toLowerCase() + salt);
	}

	@Override
	public int update(UserCommonAuth bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(UserCommonAuth bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<UserCommonAuth> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getTableName() {
		return "user_common_auth";
	}

	@Override
	public boolean updatePwd(UserCommonAuth auth, String new_password) throws ServiceException {
		UserCommonAuth newAuth = new UserCommonAuth();
		newAuth.setId(auth.getId());
		newAuth.setPassword(UserCommonAuthServiceImpl.encode(new_password));

		if(auth.getPassword().equalsIgnoreCase(newAuth.getPassword()))
			throw new ServiceException("新密码与旧密码一致，没有修改");
		
		if (update(newAuth) != 0) {
			LOGGER.info("密码修改成功");
			return true;
		}
			
		LOGGER.info("密码修改失败");
		return false;
	}

	@Override
	public List<UserCommonAuth> findList() {
		return null;
	}

}