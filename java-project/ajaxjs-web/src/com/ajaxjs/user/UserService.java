/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.user;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.ajaxjs.framework.dao.MyBatis;
import com.ajaxjs.framework.exception.DaoException;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.service.BaseCrudService;
import com.ajaxjs.util.LogHelper;

public class UserService extends BaseCrudService<User, UserDao> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserService.class);

	public UserService() {
		setMapper(UserDao.class);
		setTableName("user");
		setUiName("用户");
	}

	/**
	 * 用户登录之后……
	 * 
	 * @param user
	 * @param ip
	 * @throws ServiceException
	 */
	public void afterLogin(User user, String ip) throws ServiceException {
		if (user == null || user.getId() == 0)
			throw new ServiceException("非法用户！");

		int effectedRows = 0;
		try {
			try (SqlSession session = MyBatis.sqlSessionFactory.openSession();) {
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

	public User findByUserName(String userName) throws ServiceException {
		User user = null;

		try (SqlSession session = MyBatis.loadSession(UserDao.class);) {

			UserDao userDao = session.getMapper(UserDao.class);
			user = userDao.findByUserName(userName);

		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return user;
	}
	
	public User findByPhone(String phone) throws ServiceException {
		BaseCrudService.Session<UserDao> session = getSession();
		User user = session.mapper.findByPhone(phone);
		session.session.close();

		return user;
	}

	public User findByUserNameAndPassword(String userName, String password) throws ServiceException {
		User user = null;

		try (SqlSession session = MyBatis.loadSession(UserDao.class);) {
			UserDao userDao = session.getMapper(UserDao.class);
			user = userDao.findByUserNameAndPassword(userName, password);
		} catch (Throwable e) {
			LOGGER.warning(e);
			throw new DaoException(e.getMessage());
		}

		return user;
	}
	
	@Override
	public int create(User user) throws ServiceException {
		if (user.getName() == null) { // 如果没有用户名
			if (user.getPhone() != null) { // 则使用 user_{phone} 作为用户名
				user.setName("user_" + user.getPhone());
			}
		}
		return super.create(user);
	}

	public static boolean isRightUser(User _user_in_db, User request_user) throws ServiceException {
		if (_user_in_db == null)
			throw new ServiceException("用户不存在");
		if (!_user_in_db.getPassword().equals(request_user.getPassword()))
			throw new ServiceException("密码错误");
		return true;
	}
}
