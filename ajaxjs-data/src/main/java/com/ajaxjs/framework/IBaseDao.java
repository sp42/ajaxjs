package com.ajaxjs.framework;

import java.util.List;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.PageResult;

public interface IBaseDao<T extends IBaseBean> {
	
	/**
	 * 实体别名必须为 entry
	 */
	public final static String selectCover = "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catelog = "+ DataDict.PIC_COVER + " ORDER BY p1.id DESC LIMIT 0, 1)";
	
	/**
	 * 简单关联 catelog 表，注意表名称 alies 为 gc
	 */
	public final static String catelog_simple_join = " LEFT JOIN general_catelog gc ON gc.id = entry.catelog ";
	
	/**
	 * 用于 catelogId 查询的，通常放在 LEFT JOIN 后面还需要，WHERE e.catelog = c.id。 
	 * 还需要预留一个 catelogId 的参数
	 * 另外也可以用 IN 查询
	 */  
	public final static String catelog_finById = " (SELECT id, name FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AS c ";
	
	/**
	 * 
	 */
	public final static String catelog_find = " IN (SELECT id FROM general_catelog WHERE `path` LIKE (CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%')))";
	
//	@Select(value="SELECT GROUP_CONCAT(p.id, '|', p.`path`, '|', IFNULL(p.`catelog`, 0), '|', p.`index` SEPARATOR '\", \"') AS pics, e.*, "
//			+ "(SELECT `path` FROM attachment_picture p WHERE p.`catelog` = 2 AND owner = e.uid ORDER BY ID DESC LIMIT 1) AS cover"
//			+ " FROM  ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ?",
//			
//			sqliteValue = "SELECT (p.id || '|' || p.`path` || '|' || IFNULL(p.`catelog`, 0) || '|' || p.`index` ) AS pics, e.*, "
//					+ " p.path AS cover"
//					+ " FROM ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ? ORDER BY p.id DESC LIMIT 1")
//	public T findById_catelog_avatar(Long id);
	
	@Select(value = "SELECT entry.*, " + selectCover + " AS cover, gc.name AS catelogName FROM ${tableName} entry" + catelog_simple_join + " WHERE entry.id = ?")
	public T findById_catelog_avatar(Long id);
	
	@Select("SELECT * FROM ${tableName}")
	public List<T> findList();
	
	@Select("SELECT id, name FROM ${tableName}")
	public List<T> findSimpleList();
	
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 序号
	 * @return POJO
	 */
	@Select("SELECT entry.*, gc.name AS catelogName FROM ${tableName} entry " + catelog_simple_join +" WHERE entry.id = ?")
	public T findById_catelog(Long id);
	
	@Select("SELECT * FROM ${tableName} WHERE id = ?")
	public T findById(Long id);

	/**
	 * 简单分页。注意不用在 SQL 后面加上 LIMIT，系统会自动加的
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select("SELECT * FROM ${tableName} ORDER BY id DESC")
	public PageResult<T> findPagedList(int start, int limit);
	
	@Select(value = 	"SELECT a.*, c.name AS catelogName FROM ${tableName} a INNER JOIN " + catelog_finById + "ON a.`catelog` = c.id  WHERE a.`catelog` =  c.id AND 1 = 1 ORDER BY id DESC",
			countSql = 	"SELECT COUNT(a.id) AS count FROM ${tableName} a WHERE catelog " + catelog_find + " AND 1 = 1",

			sqliteValue = "SELECT id, name, intro, createDate, updateDate, catelog, catelogName, expr FROM ${tableName} a"
					+ " INNER JOIN (SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c "
					+ " ON a.`catelog` = c.catelogId  WHERE a.`catelog` =  c.catelogId AND 1 = 1",
			sqliteCountSql = "SELECT COUNT(a.id) AS count FROM ${tableName} a "
					+ "WHERE catelog in (SELECT id FROM general_catelog WHERE `path` LIKE ( ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AND 1 = 1")
	public PageResult<T> findPagedListByCatelogId(int catelogIds,  int start, int limit);

	/**
	 * 带封面图，可分类，可分页的列表
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select("SELECT id, name, intro, createDate, updateDate, catelog, catelogName, " + selectCover + " AS cover " + "FROM ${tableName} entry"
			+ " INNER JOIN (SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c " + " ON entry.`catelog` = c.catelogId")
	public PageResult<T> findPagedListByCatelogId_Cover(int catelogId, int start, int limit);

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
