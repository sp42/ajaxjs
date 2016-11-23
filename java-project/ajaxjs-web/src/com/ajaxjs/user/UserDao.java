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

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.DynamicSqlProvider;

public interface UserDao extends DAO<User> {
	@Select(selectById)
	@Override
	User selectById(@Param("id") long id, @Param("tablename") String tablename);
	
	@InsertProvider(type = DynamicSqlProvider.class, method = "create")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Override
	int create(User user);
	
	@Update("UPDATE SET user loginDate = ${loginDate}, ip = ${ip} WHERE id = ${id}")
	int updateLoginInfo(long id, Date loginDate, String ip);
	
	@Select("SELECT * FROM user WHERE name = '${userName}'")
	User findByUserName(@Param("userName") String userName);
	
	@Select("SELECT * FROM user WHERE name = '${userName}' AND password = '${password}'")
	User findByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);
}
