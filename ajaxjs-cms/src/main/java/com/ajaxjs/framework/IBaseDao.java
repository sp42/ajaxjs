package com.ajaxjs.framework;

import com.ajaxjs.cms.app.attachment.Attachment_pictureDao;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.PageResult;

public interface IBaseDao<T extends IBaseBean> {
	
//	@Select(value="SELECT GROUP_CONCAT(p.id, '|', p.`path`, '|', IFNULL(p.`catelog`, 0), '|', p.`index` SEPARATOR '\", \"') AS pics, e.*, "
//			+ "(SELECT `path` FROM attachment_picture p WHERE p.`catelog` = 2 AND owner = e.uid ORDER BY ID DESC LIMIT 1) AS cover"
//			+ " FROM  ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ?",
//			
//			sqliteValue = "SELECT (p.id || '|' || p.`path` || '|' || IFNULL(p.`catelog`, 0) || '|' || p.`index` ) AS pics, e.*, "
//					+ " p.path AS cover"
//					+ " FROM ${tableName} e LEFT JOIN attachment_picture p ON e.uid = p.owner WHERE e.id = ? ORDER BY p.id DESC LIMIT 1")
//	public T findById_catelog_avatar(Long id);
	
	@Select(sqliteValue = "SELECT entry.*, " + Attachment_pictureDao.selectCover + " AS cover, "
			+ "general_catelog.name AS catelogName FROM ${tableName} entry INNER JOIN general_catelog ON general_catelog.id = entry.catelog WHERE entry.id = ?")
	public T findById_catelog_avatar(Long id);
	
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 序号
	 * @return POJO
	 */
	@Select("SELECT entry.*, general_catelog.name AS catelogName FROM ${tableName} entry INNER JOIN general_catelog ON general_catelog.id = entry.catelog WHERE entry.id = ?")
	public T findById_catelog(Long id);
	
	@Select("SELECT * FROM ${tableName}")
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

	/**
	 * 带封面图，可分类，可分页的列表
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select("SELECT id, name, intro, createDate, updateDate, catelog, catelogName, " + Attachment_pictureDao.selectCover + " AS cover " + "FROM ${tableName} entry"
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
