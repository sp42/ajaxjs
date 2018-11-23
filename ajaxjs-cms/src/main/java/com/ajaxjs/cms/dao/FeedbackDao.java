package com.ajaxjs.cms.dao;

import java.util.List;

import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface FeedbackDao extends IDao<Feedback, Long> {
	public final static String tableName = "entity_feedback";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Feedback findById(Long id);

	@Select(value = "SELECT e.*, u.name AS userName FROM " + tableName + " e LEFT JOIN user u ON u.id = e.createdByUser")
	@Override
	public PageResult<Feedback> findPagedList(int start, int limit);

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public List<Feedback> findList();

	@Insert(tableName = tableName)
	@Override
	public Long create(Feedback bean);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Feedback bean);

	@Update(tableName = tableName)
	@Override
	public int update(Feedback bean);

	@Select(value = "SELECT * FROM " + tableName + " WHERE createdByUser = ?")
	public List<Feedback> getListByUserId(long userId);
}
