package com.ajaxjs.cms.app.attachment;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface AttachmentDao extends IDao<Attachment, Long> {
	final static String tableName = "attachment";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Attachment findById(Long id);
	
	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<Attachment> findPagedList(int start, int limit);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(Attachment entry);

	@Update(tableName = tableName)
	@Override
	public int update(Attachment entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Attachment entry);
}