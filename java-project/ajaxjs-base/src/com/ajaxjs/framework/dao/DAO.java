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

import com.ajaxjs.framework.model.Query;

/**
 * 数据访问对象（Data Access Object），提供数据库的增删改查服务。 子类 DAO 不能继承，只能重新写一遍才可以。
 * 但是注解可以访问字符串，所以这样也是可以得 //@Select(SqlProvider.selectById)
 * 
 * @author frank
 *
 * @param <T>
 *            实体类型
 */
public interface DAO<T> {
	/**
	 * 查询单个记录（按 id:int）。如果找不到则返回 null。
	 * 
	 * @param id
	 *            序号
	 * @param tablename
	 *            查询的表名
	 * @return POJO
	 */
	public T selectById(long id, String tablename);

	/**
	 * 用于注解的 SQL，写在这里比较方便维护
	 */
	public static final String selectById = "SELECT * FROM ${tablename} WHERE id = ${id}";

	/**
	 * 映射单个记录（按 uuid:String）。如果找不到则返回 null。
	 * 
	 * @param uuid
	 *            唯一 id
	 * @param tablename
	 *            查询的表名
	 * @return POJO
	 */
	T selectByUUID(String uuid, String tablename);

	/**
	 * 用于注解的 SQL，写在这里比较方便维护
	 */
	public static final String selectByUUID = "SELECT * FROM ${tablename} WHERE uid = #{uid}";

	/**
	 * 查询符合条件的记录总数。这个方法先于 page() 执行。如果返回 0 则无需执行 page()
	 * 
	 * @param tablename
	 *            查询的表名
	 * @param query
	 *            特定查询对象，不能为 null，否则 MyBatis 无法通过。如果无特定查询对象，可创建空的 Query 类型。
	 * @return 所有的记录一共有多少？
	 */
	public int pageCount(String tablename, Query query);

	/**
	 * 支持分页的查询。执行这个方法之前应先查询符合条件的记录总数，即 pageCount()。
	 * 
	 * @param start
	 *            起始行数
	 * @param limit
	 *            偏量值
	 * @param tablename
	 *            查询的表名
	 * @param query
	 *            特定查询对象，不能为 null，否则 MyBatis 无法通过。如果无特定查询对象，可创建空的 Query 类型。
	 * @return 分页结果
	 */
	public List<T> page(int start, int limit, String tablename, Query query);

	/**
	 * 新建记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 新建记录之 id
	 */
	public int create(T bean);

	/**
	 * 修改记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数
	 */
	public int update(T bean);
}
