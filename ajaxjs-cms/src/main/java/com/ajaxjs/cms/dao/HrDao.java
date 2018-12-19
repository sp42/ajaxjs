package com.ajaxjs.cms.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.app.catelog.Catelog;
import com.ajaxjs.cms.app.catelog.CatelogDao;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.dao.PageResult;
@TableName(value = "entity_hr", beanClass = EntityMap.class)
public interface HrDao extends IBaseDao<EntityMap> {
	public final static String tableName = "${tableName}";

	@Select(value = "SELECT id, name, createDate, updateDate, catelog FROM " + tableName + " ORDER BY ID DESC")
	@Override
	public PageResult<EntityMap> findPagedList(int start, int limit);

	@Select("SELECT * FROM ${tableName}" )
	public List<EntityMap> findList();
	
	@Select("SELECT * FROM " + CatelogDao.tableName + " WHERE pid = ?")
	public List<Catelog> getHrCatalog(int catelogParentId);

	@Select(value = "SELECT a.*, c.name AS catelogName FROM ${tableName} a "
			+ "INNER JOIN (SELECT id, name FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AS c "
			+ "ON a.`catelog` = c.id  WHERE a.`catelog` =  c.id AND 1 = 1 ORDER BY id DESC",

			sqliteValue = "SELECT id, name, intro, createDate, updateDate, catelog, catelogName, expr FROM ${tableName} a"
					+ " INNER JOIN (SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c "
					+ " ON a.`catelog` = c.catelogId  WHERE a.`catelog` =  c.catelogId AND 1 = 1",

			countSql = "SELECT COUNT(a.id) AS count FROM ${tableName} a "
					+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AND 1 = 1",

			sqliteCountSql = "SELECT COUNT(a.id) AS count FROM ${tableName} a "
					+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AND 1 = 1")

	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catelogIds,  int start, int limit);

}