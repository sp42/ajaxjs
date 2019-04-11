package com.ajaxjs.cms.app.catalog;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "general_catelog", beanClass = Catalog.class)
public interface CatalogDao extends IBaseDao<Catalog> {
	/**
	 * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序. 前端排序的话 chrom 有稳定排序的问题，故放在后端排序
	 */
	@Select(value = "SELECT * FROM ${tableName} ORDER BY pid ")
	@Override
	public PageResult<Catalog> findPagedList(int start, int limit);
	
	
	/**
	 * 删除所有，包括子分类
	 * 
	 * @param id
	 * @return
	 */
	@Delete(value = "DELETE FROM ${tableName} WHERE id in ( SELECT n.id FROM (" + // 如果子查询的 from 子句和更新、删除对象使用同一张表，会出现错误。
			"(SELECT id FROM ${tableName} WHERE `path` LIKE ( CONCAT ( (SELECT `path` FROM general_catelog WHERE id = ?) , '%')))) AS n)", 
			sqliteValue = "DELETE FROM ${tableName} " + 
			"WHERE id in (SELECT id FROM ${tableName} WHERE \"path\" LIKE (( SELECT \"path\" FROM general_catelog WHERE id = ?) || '%'));")
	public boolean deleteAll(int id);

	/**
	 * 根据父 id 获取下一层的子分类列表，获取直接一层的分类。
	 * 
	 * @param parentId
	 * @return
	 */
	@Select(value = "SELECT * FROM ${tableName} WHERE pid = ?")
	public List<Catalog> getListByParentId(int parentId);
	
	/**
	 * 获取下一级和下下一级，一共只获取这两级
	 * 
	 * @param parentId
	 * @return
	 */
	@Select("SELECT c.id, c.name, c.path, "
			+ " (SELECT GROUP_CONCAT(id, '|', name ,'|' ,`path`) FROM general_catelog WHERE `path` REGEXP CONCAT(c.path, '/[0-9]+$')) AS sub\n " + 
			"FROM general_catelog c WHERE pid = ?;")
	public List<Map<String, Object>> getListAndSubByParentId(int parentId);
	
	/**
	 * 所有后代
	 * 
	 * @param parentId
	 * @return
	 */
	@Select(value = "SELECT * FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))", 
			sqliteValue = "SELECT * FROM general_catelog WHERE " + 
			"	\"path\" LIKE (  ( " + 
			"	SELECT" + 
			"		\"path\" " + 
			"	FROM" + 
			"		general_catelog" + 
			"	WHERE" + 
			"		id = ? ) || '%')")
	public List<Catalog> getAllListByParentId(int parentId);
}
