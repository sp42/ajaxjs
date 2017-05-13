/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, User 2.0 (the "License");
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

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface UserDao extends IDao<User, Long> {
	final static String tableName = "user";
	
	@Select("SELECT * FROM " + tableName + " WHERE status = 1 AND id = ?")
	@Override
	public User findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value="SELECT * FROM catalog")
	@Override
	public PageResult<User> findPagedList(QueryParams parame);
	
	@Insert(tableName=tableName)
	@Override
	public Long create(User bean);

	@Update(tableName=tableName)
	@Override
	public int update(User bean);

	@Delete(tableName=tableName)
	@Override
	public boolean delete(User bean);
	
	@Update("UPDATE user SET lastLoginDate = ?, ip = ? WHERE id = ?")
	int updateLoginInfo(long id, Date lastLoginDate, String ip);
	
	/**
	 * 根据用户名码查找用户
	 * @param userName
	 * @return
	 */
	@Select("SELECT * FROM user WHERE name = ?")
	User findByUserName(String userName);
	
	/**
	 * 根据手机号码查找用户
	 * @param phone
	 * @return
	 */
	@Select("SELECT * FROM user WHERE phone = ? LIMIT 1")
	User findByPhone(String phone);
	
	@Select("SELECT * FROM user WHERE name = ? AND password = ?")
	User findByUserNameAndPassword(String userName, String password);
}
