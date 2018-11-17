package com.ajaxjs.cms.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

public interface HrDao extends IDao<Map<String, Object>, Long> {
	public final static String tableName = "entity_hr";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Map<String, Object> findById(Long id);

	@Select(value = "SELECT id, name, createDate, updateDate, catelog FROM " + tableName + " ORDER BY ID DESC")
	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams params, int start, int limit);

	@Select(value = "SELECT id, name, createDate, expr, catelog FROM " + tableName)
	public PageResult<Map<String, Object>> findPagedList_public(QueryParams params, int start, int limit);

	@Insert(tableName = tableName)
	@Override
	public Long create(Map<String, Object> bean);

	@Update(tableName = tableName)
	@Override
	public int update(Map<String, Object> bean);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Map<String, Object> bean);

	@Select("SELECT * FROM " + tableName + " LIMIT 0, 5")
	public List<Map<String, Object>> getTop5();

	@Select("SELECT * FROM " + CatalogDao.tableName + " WHERE pid = ?")
	public List<Catalog> getHrCatalog(int catelogParentId);

	@Select(value = "SELECT a.*, c.name AS catelogName FROM entity_hr a "
			+ "INNER JOIN (SELECT id, name FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AS c "
			+ "ON a.`catelog` = c.id  WHERE a.`catelog` =  c.id AND 1 = 1 ORDER BY id DESC",

			sqliteValue = "SELECT id, name, intro, createDate, updateDate, catelog, catelogName, expr FROM entity_hr a"
					+ " INNER JOIN (SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c "
					+ " ON a.`catelog` = c.catelogId  WHERE a.`catelog` =  c.catelogId AND 1 = 1",

			countSql = "SELECT COUNT(a.id) AS count FROM entity_hr a "
					+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AND 1 = 1",

			sqliteCountSql = "SELECT COUNT(a.id) AS count FROM entity_hr a "
					+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AND 1 = 1")

	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catelogIds, QueryParams param, int start, int limit);
	
	@Select(value = "SELECT * FROM " + tableName + " ORDER BY ID DESC")
	@Override
	public List<Map<String, Object>> findList();
}