package com.ajaxjs.cms;

import java.util.List;

import com.ajaxjs.cms.app.catelog.Catelog;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Param;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.dao.PageResult;

public interface ArticleDao extends IBaseDao<EntityMap> {
	public static final String tableName = "entity_article";

	@Select(value="SELECT GROUP_CONCAT(p.id, '|', p.`path`, '|', IFNULL(p.`catelog`, 0), '|', p.`index` SEPARATOR '\", \"') AS pics, e.*, "
			+ "(SELECT `path` FROM attachment_picture p WHERE p.`catelog` = 2 AND owner = e.uid ORDER BY ID DESC LIMIT 1) AS avatarPath"
			+ " FROM entity_article e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ?",
			
			sqliteValue = "SELECT (p.id || '|' || p.`path` || '|' || IFNULL(p.`catelog`, 0) || '|' || p.`index` ) AS pics, e.*, "
					+ " p.path AS avaterPath"
					+ " FROM entity_article e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ? ORDER BY p.id DESC LIMIT 1")
	@Override
	public EntityMap findById(Long id);

	@Select("SELECT id, name, createDate FROM " + tableName + " ORDER BY updateDate DESC LIMIT 0, ?")
	public List<EntityMap> getTop(int limit);

	@Select("SELECT entity_article.*, general_catelog.name AS catelogName FROM entity_article INNER JOIN general_catelog ON entity_article.catelog = general_catelog.id")
	public PageResult<EntityMap> findPagedListCatalog(int start, int limit);

	@Select("SELECT * FROM entity_article WHERE catelog in ( #{catelogIds} )")
	public PageResult<EntityMap> findPagedListByCatalogId2(@Param String catelogIds, int start, int limit);

	@Select(value = "SELECT a.id, a.name, a.intro, a.createDate, a.updateDate, a.uid, c.name AS catelogName, (SELECT `path` FROM attachment_picture p WHERE p.`catelog` = 2 AND owner = a.uid ORDER BY ID DESC LIMIT 1) AS avatarPath FROM entity_article a "
			+ "INNER JOIN (SELECT id, name FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AS c "
			+ "ON a.`catelog` = c.id  WHERE a.`catelog` =  c.id AND 1 = 1 ORDER BY id DESC",

	sqliteValue = "SELECT id, name, intro, createDate, updateDate, catelog, catelogName FROM entity_article a" + 
			" INNER JOIN (SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c " + 
			" ON a.`catelog` = c.catelogId  WHERE a.`catelog` =  c.catelogId AND 1 = 1",
			
	countSql = "SELECT COUNT(a.id) AS count FROM entity_article a "
			+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AND 1 = 1",
	
	sqliteCountSql = "SELECT COUNT(a.id) AS count FROM entity_article a "
			+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AND 1 = 1")
	
	public PageResult<EntityMap> findPagedListByCatalogId(int catelogIds, int start, int limit);

	@Select("SELECT * FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))")
	public List<Catelog> getCatalog(int catelogParentId);
}
