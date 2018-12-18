package com.ajaxjs.cms.app.attachment;

import java.util.List;

import com.ajaxjs.cms.utils.DataDict;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface Attachment_pictureDao extends IDao<Attachment_picture, Long> {
	final static String tableName = "attachment_picture";
	
	
	public final static String selectByUid = "SELECT path FROM attachment_picture WHERE catelog = 2 AND owner = e.uid LIMIT  0, 1";

	@Select("SElECT * FROM " + tableName + " WHERE owner = ?")
	public List<Attachment_picture> findByOwner(Long owner);

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Attachment_picture findById(Long id);
	
	@Select(value = "SELECT * FROM " + tableName + " ORDER BY id DESC")
	@Override
	public PageResult<Attachment_picture> findPagedList(int start, int limit);

	@Insert(tableName = tableName)
	@Override
	public Long create(Attachment_picture entry);

	@Update(tableName = tableName)
	@Override
	public int update(Attachment_picture entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Attachment_picture entry);
	
	@Delete("DELETE FROM "+ tableName +" WHERE `owner` = ?")
	boolean deleteByOwnerId(Long ownerUid);
	@Update("UPDATE " + tableName + " SET `index` = ? WHERE id = ?")
	public int saveImgIndex(int index, Long imgId);
	
	/**
	 * 实体别名必须为 entry
	 */
	public final static String selectCover =   "(SELECT path FROM attachment_picture p1 WHERE entry.uid = p1.owner AND p1.catelog = "+ DataDict.PIC_COVER + " ORDER BY p1.id DESC LIMIT 0, 1)";
}