package com.ajaxjs.cms.user.service;

import java.util.List;

import com.ajaxjs.cms.user.UserCommonAuth;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

@Bean(value = "User_common_authService")
public class UserCommonAuthService extends BaseService<UserCommonAuth>  {
	private static final LogHelper LOGGER = LogHelper.getLog(UserCommonAuthService.class);
	
	public static UserCommonAuthDao dao = new Repository().bind(UserCommonAuthDao.class);

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
	public String getTableName() {
		return "user_common_auth";
	}

	/**
	 * 更新用户密码
	 * 
	 * @param userId
	 * @param password
	 * @param new_password
	 * @return
	 */
	public boolean updatePwd(UserCommonAuth auth, String new_password) throws ServiceException {
		UserCommonAuth newAuth = new UserCommonAuth();
		newAuth.setId(auth.getId());
		newAuth.setPassword(UserCommonAuthService.encode(new_password));

		if (auth.getPassword().equalsIgnoreCase(newAuth.getPassword()))
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

	public static interface UserCommonAuthDao extends IBaseDao<UserCommonAuth> {
		final static String tableName = "user_common_auth";

		@Select("SELECT * FROM " + tableName + " WHERE userId = ?")
		public UserCommonAuth findByUserId(Long id);

		@Delete("DELETE FROM " + tableName + " WHERE userId = ?")
		public boolean deleteByUserId(Long userId);
	}
}