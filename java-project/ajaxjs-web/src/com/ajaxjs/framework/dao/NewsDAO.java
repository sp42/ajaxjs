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
package com.ajaxjs.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.DynamicSqlProvider;
import com.ajaxjs.framework.model.News;
import com.ajaxjs.mvc.model.Query;

public interface NewsDAO extends DAO<News> {
	@Select(selectById)
	@Override
	News selectById(@Param("id") long id, @Param("tablename") String tablename);

	@Select(selectByUUID)
	@Override
	News selectByUUID(@Param("id") String uuid, @Param("tablename") String tablename);

	@SelectProvider(type = DynamicSqlProvider.class, method = "pageCount")
	@Override
	int pageCount(@Param("tablename") String tablename, @Param("query") Query query);

//	 @Results({@Result(column = "name", property = "name", javaType = String.class,
//			jdbcType =JdbcType.VARCHAR),
//	 })
	@SelectProvider(type = DynamicSqlProvider.class, method = "page")
	@Override
	List<News> page(@Param("start") int start, @Param("limit") int limit, @Param("tablename") String tablename, @Param("query") Query query);

	@InsertProvider(type = DynamicSqlProvider.class, method = "create")
// @Insert("INSERT INTO news (name, isOnline) VALUES(#{service.tableName}, #{online})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Override
	int create(News news);

	@UpdateProvider(type = DynamicSqlProvider.class, method = "update")
	@Override
	int update(News news);
}
