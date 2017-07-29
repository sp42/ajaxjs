package com.ajaxjs.cms.dao;

import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface FeedbackDao extends IDao<Feedback, Long> {
	final static String tableName = "feedback";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Feedback findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value="SELECT * FROM " + tableName)
	@Override
	public PageResult<Feedback> findPagedList(QueryParams parame);
	
	@Insert(tableName=tableName)
	@Override
	public Long create(Feedback bean);

	@Update(tableName=tableName)
	@Override
	public int update(Feedback bean);

	@Delete(tableName=tableName)
	@Override
	public boolean delete(Feedback bean);
}
