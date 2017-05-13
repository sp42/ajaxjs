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

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.BaseDaoService;
import com.ajaxjs.framework.service.ServiceException;

public class UserService extends BaseDaoService<User, Long, UserDao> {
	public UserService() {
		initDao(UserDao.class);
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

		int effectedRows = getDao().updateLoginInfo(user.getId(), new Date(), ip);
	
		if (effectedRows <= 0)
				throw new ServiceException("更新会员登录信息出错");
	}

	public User findByUserName(String userName) throws ServiceException {
		return getDao().findByUserName(userName);
	}
	
	public User findByPhone(String phone) throws ServiceException {
		return getDao().findByPhone(phone);
	}

	public User findByUserNameAndPassword(String userName, String password) throws ServiceException {
		return getDao().findByUserNameAndPassword(userName, password);
	}

	public static boolean isRightUser(User _user_in_db, User request_user) throws ServiceException {
		if (_user_in_db == null)
			throw new ServiceException("用户不存在");
		if (!_user_in_db.getPassword().equals(request_user.getPassword()))
			throw new ServiceException("密码错误");
		return true;
	}

	@Override
	public Long create(User user) throws ServiceException {
		if (user.getName() == null) { // 如果没有用户名
			if (user.getPhone() != null) { // 则使用 user_{phone} 作为用户名
				user.setName("user_" + user.getPhone());
			}
		}
		return getDao().create(user);
	}

	@Override
	public boolean delete(User user) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User findById(Long user) throws com.ajaxjs.framework.service.ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<User> findPagedList(QueryParams user) throws com.ajaxjs.framework.service.ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(User user) throws com.ajaxjs.framework.service.ServiceException {
		// TODO Auto-generated method stub
		return 0;
	}
}
