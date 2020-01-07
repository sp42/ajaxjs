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
package com.ajaxjs.framework;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;

/**
 * DAO 基类，包含大量常用的 SQL。
 * 
 * @author Frank Cheung
 *
 * @param <T> 实体
 */
public interface IBaseDao<T> {
	/**
	 * 搜索的占位符
	 */
	public static final String WHERE_REMARK = "1 = 1";

	public static final String WHERE_REMARK_AND = " AND " + WHERE_REMARK;

	/**
	 * 按照 id 字段进行降序
	 */
	public final static String DESCENDING_ID = " ORDER BY id DESC";

	public static final String WHERE_REMARK_ORDER = " WHERE " + WHERE_REMARK + DESCENDING_ID;

	/**
	 * 
	 * @deprecated
	 * @param id
	 * @return
	 */
	@Select(value = "SELECT GROUP_CONCAT(p.id, '|', p.`path`, '|', IFNULL(p.`catalog`, 0), '|', p.`index` SEPARATOR '\", \"') AS pics, e.*, "
			+ "(SELECT `path` FROM attachment_picture p WHERE p.`catalog` = 2 AND owner = e.uid ORDER BY ID DESC LIMIT 1) AS cover"
			+ " FROM  ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ?",

			sqliteValue = "SELECT (p.id || '|' || p.`path` || '|' || IFNULL(p.`catalog`, 0) || '|' || p.`index` ) AS pics, e.*, "
					+ " p.path AS cover"
					+ " FROM ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ? ORDER BY p.id DESC LIMIT 1")
	public T findById_Attachment(Long id);

	// ---------------- find one-------------------

	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param sqlHandler 查找的条件
	 * @return 单个记录
	 */
	@Select("SELECT * FROM ${tableName} e WHERE " + WHERE_REMARK)
	public T find(Function<String, String> sqlHandler);

	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 记录 id
	 * @return 单个记录
	 */
	@Select("SELECT * FROM ${tableName} e WHERE e.id = ?")
	public T findById(Long id);

	// ---------------- find list-------------------

	/**
	 * 查询列表数据
	 * 
	 * @param sqlHandler SQL 处理器。如果你不需要查询条件，可以传入 null
	 * @return 实体列表
	 */
	@Select("SELECT * FROM ${tableName} e WHERE " + WHERE_REMARK)
	public List<T> findList(Function<String, String> sqlHandler);

	/**
	 * 简单分页。注意不用在 SQL 后面加上 LIMIT，系统会自动加的
	 * 
	 * @param start
	 * @param limit
	 * @param sqlHandler SQL 处理器。如果你不需要查询条件，可以传入 null
	 * @return 实体分页列表
	 */
	@Select("SELECT * FROM ${tableName} e " + WHERE_REMARK_ORDER)
	public PageResult<T> findPagedList(int start, int limit, Function<String, String> sqlHandler);

	// ---------------- create、update、delete------------------

	/**
	 * 新建记录
	 * 
	 * @param bean 实体对象
	 * @return 新建记录之序号
	 */
	@Insert
	public Long create(T bean);

	/**
	 * 修改记录
	 * 
	 * @param bean 实体对象
	 * @return 影响的行数，理应 = 1
	 */
	@Update
	public int update(T bean);

	/**
	 * 单个删除
	 * 
	 * @param bean 实体对象
	 * @return 影响的行数
	 */
	@Delete
	public boolean delete(T bean);
}
