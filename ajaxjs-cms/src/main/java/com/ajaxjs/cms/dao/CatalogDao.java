package com.ajaxjs.cms.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.app.catelog.Catelog;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface CatalogDao extends IDao<Catelog, Long> {
	public final static String tableName = "general_catelog";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Catelog findById(Long id);

	/**
	 * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序. 前端排序的话 chrom 有稳定排序的问题，故放在后端排序
	 */
	@Select(value = "SELECT * FROM " + tableName + " ORDER BY pid ")
	@Override
	public PageResult<Catelog> findPagedList(int start, int limit);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(Catelog bean);

	@Update(tableName = tableName)
	@Override
	public int update(Catelog bean);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Catelog bean);
	
	/**
	 * 删除所有，包括子分类
	 * @param id
	 * @return
	 */
	@Delete(value="DELETE FROM general_catelog WHERE id in ( SELECT n.id FROM (" + // 如果子查询的 from 子句和更新、删除对象使用同一张表，会出现错误。
			"(SELECT id FROM general_catelog WHERE `path` LIKE ( CONCAT ( (SELECT `path` FROM general_catelog WHERE id = ?) , '%')))) AS n)", 
			sqliteValue="DELETE FROM general_catelog " + 
			"WHERE id in (SELECT id FROM general_catelog WHERE \"path\" LIKE (( SELECT \"path\" FROM general_catelog WHERE id = ?) || '%'));")
	public boolean deleteAll(int id);

	/**
	 * 根据父 id 获取下一层的子分类列表，获取直接一层的分类。
	 * 
	 * @param parentId
	 * @return
	 */
	@Select(value = "SELECT * FROM " + tableName + " WHERE pid = ?")
	public List<Catelog> getListByParentId(int parentId);
	
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
	public List<Catelog> getAllListByParentId(int parentId);

}
