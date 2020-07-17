package com.ajaxjs.user.service;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.controller.MvcRequest;

@Component("User_common_authService")
public class UserCommonAuthService extends BaseService<UserCommonAuth> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserCommonAuthService.class);
	
	@TableName(value = "user_common_auth", beanClass = UserCommonAuth.class)
	public static interface UserCommonAuthDao extends IBaseDao<UserCommonAuth> {
		@Select("SELECT * FROM ${tableName} WHERE userId = ?")
		public UserCommonAuth findByUserId(Long id);

		@Delete("DELETE FROM ${tableName} WHERE userId = ?")
		public boolean deleteByUserId(Long userId);
	}

	public static UserCommonAuthDao dao = new Repository().bind(UserCommonAuthDao.class);
	
	{
		setUiName("用户口令");
		setShortName("UserCommonAuth");
		setDao(dao);
	}

	@Override
	public UserCommonAuth findById(Long id) {
		return dao.findById(id);
	}

	/**
	 * 默认的加盐字符串
	 */
	public static final String SALT = "dasdsad312";

	@Override
	public Long create(UserCommonAuth bean) {
		bean.setPassword(encode(bean.getPassword()));
		
		try {
			bean.setRegisterIp(MvcRequest.getMvcRequest().getIp());
		} catch (Exception e) {
			// 测试环境不能获取 ip
		}

		return super.create(bean);
	}

	public static String encode(String p) {
//		LOGGER.info("提交密码:::::::::::::::::::::::::" + p.toLowerCase());
//		LOGGER.info("保存密码:::::::::::::::::::::::::" + Encode.md5(p.toLowerCase() + SALT));
		return Encode.md5(p.toLowerCase() + SALT);
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
		return dao.findPagedList(start, limit, null);
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
	 * @return 是否修改成功
	 */
	public boolean updatePwd(UserCommonAuth auth, String new_password) throws ServiceException {
		UserCommonAuth newAuth = new UserCommonAuth();
		newAuth.setId(auth.getId());
		newAuth.setPassword(encode(new_password));

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
}