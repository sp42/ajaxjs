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

public class UserService extends BaseCrudService<User, UserDao> {
	public UserService() {
		setMapper(UserDao.class);
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
		if(user == null || user.getId() == 0)
			throw new ServiceException("非法用户！");
		
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

	public User findByUserName(String userName) {
		return null;
		
	}
}
