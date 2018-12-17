package com.ajaxjs.cms.domain;

import java.util.List;

import com.ajaxjs.cms.app.attachment.Attachment_pictureDao;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;


public interface DomainEntityDao extends IDao<DomainEntity, Long> {
	@Select("SELECT entry.*, general_catelog.name AS catelogName FROM ${tableName} entry INNER JOIN general_catelog ON general_catelog.id = entry.catelog WHERE entry.id = ?")
	@Override
	public DomainEntity findById(Long id);

	@Override
	public List<DomainEntity> findList();

	@Override
	public PageResult<DomainEntity> findPagedList(int start, int limit);

	@Insert
	@Override
	public Long create(DomainEntity bean);

	@Update
	@Override
	public int update(DomainEntity bean);

	@Delete
	@Override
	public boolean delete(DomainEntity bean);
	
	@Select("SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 ) " + 
			"UNION ALL " + 
			"SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 ) " + 
			"UNION ALL " + 
			"SELECT id, name, catelog FROM(SELECT * FROM ${tableName} WHERE catelog = ? ORDER BY id DESC LIMIT 0, 3 )")
	public List<DomainEntity> findHome(int c1, int c2, int c3);
	
	@Select("SELECT id, name, intro, createDate, updateDate, catelog, catelogName, " + Attachment_pictureDao.selectCover + " AS cover "
			+ "FROM ${tableName} a" + 
			" INNER JOIN (SELECT id AS catelogId, name AS catelogName FROM general_catelog WHERE `path` LIKE (  ( SELECT `path` FROM general_catelog WHERE id = ? ) || '%')) AS c " + 
			" ON a.`catelog` = c.catelogId")
	public PageResult<DomainEntity> findListByCatelogId(int catelogId, int start, int limit);
}
