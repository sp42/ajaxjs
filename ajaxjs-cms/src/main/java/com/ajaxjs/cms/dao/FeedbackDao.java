package com.ajaxjs.cms.dao;

import java.util.List;

import com.ajaxjs.cms.model.Feedback;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.dao.PageResult;

@TableName(value = "entity_feedback", beanClass = Feedback.class)
public interface FeedbackDao extends IBaseDao<Feedback> {
	@Select(value = "SELECT e.*, u.name AS userName FROM ${tableName} e LEFT JOIN user u ON u.id = e.createdByUser")
	@Override
	public PageResult<Feedback> findPagedList(int start, int limit);

	@Select(value = "SELECT * FROM ${tableName}  WHERE createdByUser = ?")
	public List<Feedback> getListByUserId(long userId);
}
