/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.sql.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.SqlFactory;
import com.ajaxjs.sql.annotation.Update;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;

/**
 * Data Access Object for testing.
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface NewsDaoMap extends IBaseDao<Map<String, Object>> {
	final static String tableName = "news";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Map<String, Object> findById(Long id);

	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	public int count();

	@SqlFactory("getInstance")
	public int count2();

	public static String getInstance() {
		return "SELECT * FROM " + tableName;
	}

	@Select("SELECT * FROM " + tableName + " LIMIT ?, ?")
	public List<Map<String, Object>> findList(int start, int limit);

	@Select(value = "SELECT * FROM " + tableName)
	public PageResult<Map<String, Object>> findPagedList(int start, int limit);

	@Select("SELECT * FROM " + tableName + " ORDER BY createDate LIMIT 0, 10")
	public List<Map<String, Object>> findTop10News();

	@Insert("INSERT INTO " + tableName + " (status, name) VALUES (?, ?)")
	public Long createBySql(int status, String name);

	@Insert(tableName = tableName)
	@Override
	public Long create(Map<String, Object> bean);

	@Update(tableName = tableName)
	@Override
	public int update(Map<String, Object> bean);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Map<String, Object> bean);
}
