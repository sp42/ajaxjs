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

	/**
	 * 实体别名必须为 entry
	 */
	public final static String selectCover = "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catalog = 1 ORDER BY p1.id DESC LIMIT 0, 1)";

	/**
	 * 简单关联 catelog 表，注意表名称 alies 为 gc
	 */
	public final static String catelog_simple_join = " LEFT JOIN general_catalog gc ON gc.id = entry.catalogId ";

	public final static String pathLike_mysql = " FROM general_catalog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catalog WHERE id = ? ) , '%'))";
//	public final static String pathLike_sqlite = " FROM general_catalog WHERE `path` LIKE ( (SELECT `path` FROM general_catalog WHERE id = ? ) || '/%')";
	public final static String pathLike_sqlite = " FROM general_catalog WHERE `path` LIKE ( (SELECT `path` FROM general_catalog WHERE id = ? ) || '%')";

	/**
	 * 用于 catelogId 查询的，通常放在 LEFT JOIN 后面还需要，WHERE e.catelog = c.id。 还需要预留一个
	 * catelogId 的参数 另外也可以用 IN 查询
	 */
	public final static String catelog_finById = " (SELECT id, name " + pathLike_mysql + ") AS c ";
	
	public final static String catelog_finById_sqlite = "(SELECT id AS catalogId, name AS catalogName " + pathLike_sqlite + ") AS c";

	/**
	 * IN 查询用
	 */
	public final static String catelog_find = "(SELECT id " + pathLike_mysql + ")";
	
	public final static String catelog_find_sqlite = "(SELECT id " + pathLike_sqlite + ")";

	// ---------------- find one-------------------
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param sqlHandler 查找的条件
	 * @return Bean
	 */
	@Select("SELECT * FROM ${tableName} WHERE " + WHERE_REMARK)
	public T find(Function<String, String> sqlHandler);

	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 记录 id
	 * @return Bean
	 */
	@Select("SELECT * FROM ${tableName} WHERE id = ?")
	public T findById(Long id);

	/**
	 * 查询单个记录，带有类别的、封面的。如果找不到则返回 null
	 * 
	 * @param id 记录 id
	 * @return Bean
	 */
	@Select(value = "SELECT entry.*, gc.name AS catelogName, " + selectCover + " AS cover FROM ${tableName} entry" + catelog_simple_join + " WHERE entry.id = ?")
	public T findById_catelog_avatar(Long id);

	/**
	 * 适合附件列表 TODO
	 * 
	 * @param id
	 * @return
	 */
	@Select(value = "SELECT GROUP_CONCAT(p.id, '|', p.`path`, '|', IFNULL(p.`catalog`, 0), '|', p.`index` SEPARATOR '\", \"') AS pics, e.*, "
			+ "(SELECT `path` FROM attachment_picture p WHERE p.`catalog` = 2 AND owner = e.uid ORDER BY ID DESC LIMIT 1) AS cover"
			+ " FROM  ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ?",

			sqliteValue = "SELECT (p.id || '|' || p.`path` || '|' || IFNULL(p.`catalog`, 0) || '|' || p.`index` ) AS pics, e.*, " + " p.path AS cover"
					+ " FROM ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ? ORDER BY p.id DESC LIMIT 1")
	public T findById_Attachment(Long id);

	// ---------------- find list-------------------
	/**
	 * 查询列表数据
	 * 
	 * @return 实体列表
	 */
	@Select("SELECT * FROM ${tableName} WHERE " + WHERE_REMARK)
	public List<T> findList(Function<String, String> sqlHandler);
	
	/**
	 * 简单分页。注意不用在 SQL 后面加上 LIMIT，系统会自动加的
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT * FROM ${tableName} WHERE 1 = 1 ORDER BY id DESC")
	public PageResult<T> findPagedList(int start, int limit, Function<String, String> sqlHandler);

	/**
	 * 可分类的，可分页的列表
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select(value = "SELECT entry.*, c.name AS catelogName FROM ${tableName} entry INNER JOIN " + catelog_finById
			+ "ON entry.`catelogId` = c.id  WHERE 1 = 1 ORDER BY id DESC", countSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find + " AND 1 = 1",

			sqliteValue = "SELECT id, name, createDate, updateDate, entry.catelogId, catelogName FROM ${tableName} entry INNER JOIN " + catelog_finById_sqlite
					+ " ON entry.`catelogId` = c.catelogId  WHERE 1 = 1 ORDER BY id DESC", sqliteCountSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find_sqlite
							+ " AND 1 = 1")
	public PageResult<T> findPagedListByCatelogId(int catelogId, int start, int limit, Function<String, String> sqlHandler);

	/**
	 * 带封面图，可分类的，可分页的列表
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select(value = "SELECT entry.*, c.name AS catelogName, " + selectCover + " AS cover FROM ${tableName} entry INNER JOIN " + catelog_finById
			+ "ON entry.`catelogId` = c.id  WHERE 1 = 1 ORDER BY id DESC", countSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find + " AND 1 = 1",

			sqliteValue = "SELECT entry.*, catelogName, " + selectCover + " AS cover FROM ${tableName} entry INNER JOIN " + catelog_finById_sqlite
					+ " ON entry.`catelogId` = c.catelogId WHERE 1 = 1 ORDER BY id DESC", sqliteCountSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find_sqlite
							+ " AND 1 = 1")
	public PageResult<T> findPagedListByCatalogId_Cover(int catelogId, int start, int limit, Function<String, String> sqlHandler);

	// ---------------- create、update、delete------------------

	/**
	 * 新建记录
	 * 
	 * @param bean POJO 对象
	 * @return 新建记录之序号
	 */
	@Insert
	public Long create(T bean);

	/**
	 * 修改记录
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数，理应 = 1
	 */
	@Update
	public int update(T bean);

	/**
	 * 单个删除
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数
	 */
	@Delete
	public boolean delete(T bean);
}
