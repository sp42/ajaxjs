package com.ajaxjs.app.catalog;

import java.util.List;
import java.util.Map;

import com.ajaxjs.sql.annotation.Delete;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "general_catalog", beanClass = Catalog.class)
public interface CatalogDao extends IBaseDao<Catalog> {
	public final static String PATH_LIKE_MYSQL = "SELECT * FROM general_catalog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catalog WHERE id = ?  ) , '%'))";
	/**
	 * 供其它实体关联时候用，可以获取下级所有子分类
	 */
	public final static String PATH_LIKE_MYSQL_ID = "SELECT id FROM general_catalog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catalog WHERE id = %d ) , '%%'))";
	public final static String PATH_LIKE_SQLITE = "SELECT * FROM general_catalog WHERE `path` LIKE ( (SELECT `path` FROM general_catalog WHERE id = ? ) || '%')";
//	public final static String pathLike_sqlite = " FROM general_catalog WHERE `path` LIKE ( (SELECT `path` FROM general_catalog WHERE id = ? ) || '/%')";

	@Select(value = PATH_LIKE_MYSQL, sqliteValue = PATH_LIKE_SQLITE)
	public List<Catalog> getAllListByParentId(int pId);

	/**
	 * 获取下一级和下下一级，一共只获取这两级
	 * 
	 * @param pId
	 * @return
	 */
	@Select("SELECT c.id, c.name, c.path, " + " (SELECT GROUP_CONCAT(id, '|', name ,'|' ,`path`) FROM ${tableName} WHERE `path` REGEXP CONCAT(c.path, '/[0-9]+$')) AS sub\n "
			+ "FROM ${tableName} c WHERE pid = ?;")
	public List<Map<String, Object>> getListAndSubByParentId(int pId);

	/**
	 * 删除所有，包括子分类 如果子查询的 from 子句和更新、删除对象使用同一张表，会出现错误。
	 * 
	 * @param id
	 * @return
	 */
	@Delete(value = "DELETE FROM ${tableName} WHERE id in ( SELECT n.id FROM ("
			+ "(SELECT id FROM ${tableName} WHERE `path` LIKE ( CONCAT ( (SELECT `path` FROM general_catalog WHERE id = ?) , '%')))) AS n)", sqliteValue = "DELETE FROM ${tableName} "
					+ "WHERE id in (SELECT id FROM ${tableName} WHERE \"path\" LIKE (( SELECT \"path\" FROM {tableName} WHERE id = ?) || '%'));")
	public boolean deleteAll(int id);

	/**
	 * 左连接分类表，实体简写必须为 e
	 */
	public final static String LEFT_JOIN_CATALOG = " LEFT JOIN general_catalog gc ON gc.id = e.catalogId ";

	/**
	 * 关联分类表以获取分类名称，即增加了 catalogName 字段。另外如果前台不需要显示的话，只是后台的话，可以用 map 显示
	 */
	public final static String SELECT_CATALOGNAME = "SELECT e.*, gc.name catalogName FROM ${tableName} e" + LEFT_JOIN_CATALOG + WHERE_REMARK_ORDER;
}
