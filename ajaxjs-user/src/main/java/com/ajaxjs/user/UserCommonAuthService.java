package com.ajaxjs.user;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

@Bean("User_common_authService")
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
	public static final String salt = "dasdsad312";

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