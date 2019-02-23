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

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.SqlFactory;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.annotation.Update;

/**
 * Data Acccess Object for testing.
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@TableName(value = "news", beanClass = News.class)
public interface NewsDao extends IBaseDao<News> {
	final static String tableName = "news";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public News findById(Long id);

	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	public Integer count();

	@Select("SELECT * FROM news LIMIT ?, ?")
	@SqlFactory("getInstance")
	public int count2();

	public static String getInstance(String sql) {
		return "SELECT COUNT(*) FROM news";
	}

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
