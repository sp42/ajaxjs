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
package com.ajaxjs.framework.testcase;

import java.util.List;

import com.ajaxjs.framework.News;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.SqlFactory;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

/**
 * Data Acccess Object for testing.
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface NewsDao extends IDao<News, Long> {
	final static String tableName = "news";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public News findById(Long id);

	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	public int count();

	@Select(value = "SELECT * FROM news")
	@SqlFactory("getInstance")
	public int count2();

	public static String getInstance() {
		return "SELECT * FROM news";
	}
	
	@Select(value = "SELECT * FROM " + tableName)
	@SqlFactory(clz = QueryParams.class, value = "addWhere")
	public int count3();

	@Select("SELECT * FROM news LIMIT ?, ?")
	public List<News> findList(int start, int limit);

	@Select(value = "SELECT * FROM news")
	public PageResult<News> findPagedList(int start, int limit);

	@Select("SELECT * FROM news ORDER BY createDate LIMIT 0, 10")
	public List<News> findTop10News();

	@Insert("INSERT INTO news (status, name) VALUES (?, ?)")
	public Long createBySql(int status, String name);

	@Insert(tableName = tableName)
	@Override
	public Long create(News bean);

	@Update(tableName = tableName)
	@Override
	public int update(News bean);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(News bean);
}
