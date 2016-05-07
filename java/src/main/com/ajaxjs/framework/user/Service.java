package com.ajaxjs.framework.user;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.ajaxjs.app.App;
import com.ajaxjs.framework.exception.DaoException;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseService;

public class Service extends BaseService<User, Mapper> {
	public Service() {
		setMapper(Mapper.class);
		setTableName("user");
		setUiName("用户");
	}
	
	/**
	 * 用户登录之后……
	 * @param user
	 * @param ip
	 * @throws ServiceException
	 */
	public void afterLogin(User user, String ip) throws ServiceException {
		int effectedRows = 0;
		
		try {
			try (SqlSession session = App.sqlSessionFactory.openSession();) {
				UserDao dao = session.getMapper(UserDao.class);
				effectedRows = dao.updateLoginInfo(user.getId(), new Date(), ip);
				session.commit();
			}

			if (effectedRows <= 0)
				throw new DaoException("更新会员登录信息出错");
		} catch (Throwable e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		}
	}

	public User findByUserName(String userName) {
		return null;
		
	}
}
