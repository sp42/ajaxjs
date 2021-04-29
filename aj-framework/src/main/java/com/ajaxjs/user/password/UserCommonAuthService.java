package com.ajaxjs.user.password;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.MvcRequest;

@Component
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
	public String getTableName() {
		return "user_common_auth";
	}

	/**
	 * 更新用户密码
	 * 
	 * @param auth
	 * @param newPassword
	 * @return 是否修改成功
	 */
	public boolean updatePwd(UserCommonAuth auth, String newPassword) {
		UserCommonAuth newAuth = new UserCommonAuth();
		newAuth.setId(auth.getId());
		newAuth.setPassword(encode(newPassword));

		if (auth.getPassword().equalsIgnoreCase(newAuth.getPassword()))
			throw new UnsupportedOperationException("新密码与旧密码一致，没有修改");

		if (update(newAuth) != 0) {
			LOGGER.debug("密码修改成功");

			return true;
		}

		LOGGER.debug("密码修改失败");
		return false;
	}
}