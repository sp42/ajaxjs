package com.ajaxjs.framework;

import java.util.List;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;

public interface IBaseDao<T> {
	
	/**
	 * 实体别名必须为 entry
	 */
	public final static String selectCover = "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catelog = "+ DataDict.PIC_COVER + " ORDER BY p1.id DESC LIMIT 0, 1)";
	
	/**
	 * 简单关联 catelog 表，注意表名称 alies 为 gc
	 */
	public final static String catelog_simple_join = " LEFT JOIN general_catelog gc ON gc.id = entry.catelogId ";
	
	/**
	 * 用于 catelogId 查询的，通常放在 LEFT JOIN 后面还需要，WHERE e.catelog = c.id。 
	 * 还需要预留一个 catelogId 的参数
	 * 另外也可以用 IN 查询
	 */  
	public final static String catelog_finById = " (SELECT id, name FROM general_catelog WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%'))) AS c ";
	public final static String catelog_finById_sqlite = "(SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c";
	
	/**
	 * IN 查询用
	 */
	public final static String catelog_find 		= "(SELECT id FROM general_catelog WHERE `path` LIKE (CONCAT (( SELECT `path` FROM general_catelog WHERE id = ? ) , '%')))";
	public final static String catelog_find_sqlite  = "(SELECT id FROM general_catelog WHERE `path` LIKE ( ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%'))";
	
	//---------------- find one-------------------
	
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 记录 id
	 * @return Bean
	 */
	@Select("SELECT * FROM ${tableName} WHERE id = ?")
	public T findById(Long id);

	/**
	 * 查询单个记录，带有类别的。如果找不到则返回 null
	 * 
	 * @param id 记录 id
	 * @return Bean
	 */
	@Select("SELECT entry.*, gc.name AS catelogName FROM ${tableName} entry " + catelog_simple_join + " WHERE entry.id = ?")
	public T findById_catelog(Long id);

	/**
	 * 查询单个记录，带有封面的。如果找不到则返回 null
	 * 
	 * @param id 记录 id
	 * @return Bean
	 */
	@Select(value = "SELECT entry.*, " + selectCover + " AS cover FROM ${tableName} entry WHERE entry.id = ?")
	public T findById_cover(Long id);
	
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
	 * @param id
	 * @return
	 */
	@Select(value="SELECT GROUP_CONCAT(p.id, '|', p.`path`, '|', IFNULL(p.`catelog`, 0), '|', p.`index` SEPARATOR '\", \"') AS pics, e.*, "
			+ "(SELECT `path` FROM attachment_picture p WHERE p.`catelog` = 2 AND owner = e.uid ORDER BY ID DESC LIMIT 1) AS cover"
			+ " FROM  ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ?",
			
			sqliteValue = "SELECT (p.id || '|' || p.`path` || '|' || IFNULL(p.`catelog`, 0) || '|' || p.`index` ) AS pics, e.*, "
					+ " p.path AS cover"
					+ " FROM ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ? ORDER BY p.id DESC LIMIT 1")
	public T findById_Attachment(Long id);
	
	//---------------- find list-------------------

	/**
	 * 查询所有数据
	 * 
	 * @return 实体列表
	 */
	@Select("SELECT * FROM ${tableName}")
	public List<T> findList();

	/**
	 * 查询所有数据，只包含 id、name 两个字段
	 * 
	 * @return 实体列表
	 */
	@Select("SELECT id, name FROM ${tableName}")
	public List<T> findSimpleList();
	

	/**
	 * 简单分页。注意不用在 SQL 后面加上 LIMIT，系统会自动加的
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT * FROM ${tableName} ORDER BY id DESC")
	public PageResult<T> findPagedList(int start, int limit);

	/**
	 * 按照类别查询
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT entry.* FROM ${tableName} entry WHERE catelogId = ?" )
	public List<T> findListByCatelog(int catelogId);
	/**
	 * 按照类别查询，带封面图的
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT entry.*, "+ selectCover +" AS cover FROM ${tableName} entry WHERE catelogId = ?" )
	public List<T> findListByCatelog_Cover(int catelogId);

	/**
	 * 显示类别名称，可分页的
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT entry.*, gc.name AS catelogName FROM ${tableName} entry" + catelog_simple_join)
	public PageResult<T> findPagedList_Catelog(int start, int limit);
	
	
	/**
	 * 带封面图，可分页的
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT id, name, subTitle, createDate, updateDate, " + selectCover + " AS cover FROM ${tableName} entry")
	public PageResult<T> findPagedList_Cover(int start, int limit);
	
	/**
	 * 显示类别名称， 带封面图，可分页的
	 * 
	 * @param start
	 * @param limit
	 * @return 实体分页列表
	 */
	@Select("SELECT entry.*, gc.name AS catelogName, " + selectCover + " AS cover FFROM ${tableName} entry" + catelog_simple_join)
	public PageResult<T> findPagedListCatelog_Cover(int start, int limit);
	
	/**
	 * 可分类的，可分页的列表
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select(value = 	"SELECT entry.*, c.name AS catelogName FROM ${tableName} entry INNER JOIN " + catelog_finById + "ON entry.`catelogId` = c.id  WHERE 1 = 1 ORDER BY id DESC",
			countSql = 	"SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find + " AND 1 = 1",

			sqliteValue = "SELECT id, name, createDate, updateDate, entry.catelogId, catelogName FROM ${tableName} entry INNER JOIN "
					   		+ catelog_finById_sqlite + " ON entry.`catelogId` = c.catelogId  WHERE 1 = 1 ORDER BY id DESC",
			sqliteCountSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find_sqlite + " AND 1 = 1")
	public PageResult<T> findPagedListByCatelogId(int catelogId,  int start, int limit);
	
	/**
	 * 带封面图，可分类的，可分页的列表
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select(value = 	"SELECT entry.*, c.name AS catelogName, " + selectCover + " AS cover FROM ${tableName} entry INNER JOIN " + catelog_finById + "ON entry.`catelogId` = c.id  WHERE 1 = 1 ORDER BY id DESC",
			countSql = 	"SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find + " AND 1 = 1",
			
			sqliteValue = "SELECT id, name, createDate, updateDate, entry.catelogId, catelogName, " + selectCover + " AS cover FROM ${tableName} entry INNER JOIN "
							+ catelog_finById_sqlite + " ON entry.`catelogId` = c.catelogId  WHERE 1 = 1",
			sqliteCountSql = "SELECT COUNT(entry.id) AS count FROM ${tableName} entry WHERE catelogId IN " + catelog_find_sqlite + " AND 1 = 1")
	public PageResult<T> findPagedListByCatelogId_Cover(int catelogId, int start, int limit);
	
	//---------------- create、update、delete------------------

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
