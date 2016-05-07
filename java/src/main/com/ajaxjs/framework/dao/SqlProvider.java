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
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 静态的 SQL，定义于此。动态生成的 SQL，请参见 SQLProvider.java
 * Data Manipulation
 * @author frank
 *
 */
public interface SqlProvider {
 
	/**
	 * 根据 id 删除一条记录
	 * 
	 * @param from
	 *            表名
	 * @param id
	 *            id 序号
	 * @return 若成功返回大于零
	 */
	@Delete("Delete FROM ${from} WHERE id = ${id}")
	public int deleteById(@Param("from") String from, @Param("id") long id);

	/**
	 * 根据 uuid 删除一条记录
	 * 
	 * @param from
	 *            表名
	 * @param uuid
	 *            唯一序号
	 * @return 若成功返回大于零
	 */
	@Delete("Delete FROM ${from} WHERE uid = ${uuid}")
	public int deleteByUUID(@Param("from") String from, @Param("uuid") String uuid);

	/**
	 * 标记删除，不是物理删除
	 * 
	 * @param from
	 *            表名
	 * @param id
	 * @return 若成功返回大于零
	 */
	@Update("UPDATE SET ${from} isDelete = 1 WHERE id = ${id}")
	public int markDeleteById(@Param("from") String from, @Param("id") int id);

	/**
	 * 标记删除，不是物理删除
	 * 
	 * @param from
	 *            表名
	 * @param uuid
	 *            唯一序号
	 * @return 若成功返回大于零
	 */
	@Update("UPDATE SET ${from} isDelete = 1 WHERE uuid = ${uuid}")
	public int markDeleteByUUID(@Param("from") String from, @Param("uuid") String uuid);
	
	/**
	 * 表中是否存在某条记录。原本可不用 COUNT，但 Mybatis 希望返回单行记录。
	 * @param from
	 *            可以是表名，也可以是 表名+ 复杂的查询语句
	 * @return true 表示为有这记录
	 */
	@Select("SELECT COUNT(*) AS count FROM ${from}")
	public boolean isExist(@Param("from") String from);
	
	/**
	 * 选择满足条件的最后一条记录
	 * 
	 * @param form
	 *            表名
	 * @return
	 */
	@Select("SELECT MAX(%s) FROM ${from}")
	public String getMaxId(@Param("from") String form);

	// UNION 时，SQLite 居然不能直接使用括号，所以必须得 SELECT * FROM
	// 可以用 Union 合并 两次查询为一次
	@Select(
		"SELECT * FROM (SELECT id, name FROM ${from} WHERE id > ${id} LIMIT 1) " +
		"UNION ALL " +
		"SELECT * FROM (SELECT id, name FROM ${from} WHERE id < ${id} ORDER BY id DESC LIMIT 1)"
	)
	public List<Map<String, String>> getNeighbor(@Param("from") String form, @Param("id") long id);
}
