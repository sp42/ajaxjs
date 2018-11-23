package com.ajaxjs.cms.dao.section;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.model.SectionInfo;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface SectionInfoDao extends IDao<SectionInfo, Long> {
	public final static String tableName = "section_info";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public SectionInfo findById(Long id);

	/**
	 * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序. 前端排序的话 chrom 有稳定排序的问题，故放在后端排序
	 */
	@Select(value = "SELECT * FROM " + tableName + " ORDER BY pid ")
	@Override
	public PageResult<SectionInfo> findPagedList(int start, int limit);

	@Insert(tableName = tableName)
	@Override
	public Long create(SectionInfo bean);

	@Update(tableName = tableName)
	@Override
	public int update(SectionInfo bean);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(SectionInfo bean);
	
	/**
	 * 删除所有，包括子分类
	 * @param id
	 * @return
	 */
	@Delete(value="DELETE FROM section_info WHERE id in ( SELECT n.id FROM (" + // 如果子查询的 from 子句和更新、删除对象使用同一张表，会出现错误。
			"(SELECT id FROM section_info WHERE `path` LIKE ( CONCAT ( (SELECT `path` FROM section_info WHERE id = ?) , '%')))) AS n)", 
			sqliteValue="DELETE FROM section_info " + 
			"WHERE id in (SELECT id FROM section_info WHERE \"path\" LIKE (( SELECT \"path\" FROM section_info WHERE id = ?) || '%'));")
	public boolean deleteAll(int id);

	/**
	 * 根据父 id 获取下一层的子分类列表，获取直接一层的分类。
	 * 
	 * @param parentId
	 * @return
	 */
	@Select(value = "SELECT * FROM " + tableName + " WHERE pid = ?")
	public List<SectionInfo> getListByParentId(int parentId);
	
	/**
	 * 获取下一级和下下一级，一共只获取这两级
	 * 
	 * @param parentId
	 * @return
	 */
	@Select("SELECT c.id, c.name, c.path, "
			+ " (SELECT GROUP_CONCAT(id, '|', name ,'|' ,`path`) FROM section_info WHERE `path` REGEXP CONCAT(c.path, '/[0-9]+$')) AS sub\n " + 
			"FROM section_info c WHERE pid = ?;")
	public List<Map<String, Object>> getListAndSubByParentId(int parentId);
	
	/**
	 * 所有后代
	 * @param parentId
	 * @return
	 */
	@Select(value = "SELECT * FROM section_info WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM section_info WHERE id = ? ) , '%'))", 
			sqliteValue = "SELECT * FROM section_info WHERE " + 
			"	\"path\" LIKE (  ( " + 
			"	SELECT" + 
			"		\"path\" " + 
			"	FROM" + 
			"		section_info" + 
			"	WHERE" + 
			"		id = ? ) || '%')")
	public List<SectionInfo> getAllListByParentId(int parentId);

}