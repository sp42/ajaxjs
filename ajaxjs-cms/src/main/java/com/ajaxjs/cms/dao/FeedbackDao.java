package com.ajaxjs.cms.dao;

import java.util.List;

import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.dao.PageResult;

@TableName(value = "entity_feedback", beanClass = EntityMap.class)
public interface FeedbackDao extends IBaseDao<EntityMap> {
	@Select(value = "SELECT e.*, u.name AS userName FROM ${tableName} e LEFT JOIN user u ON u.id = e.createdByUser")
	@Override
	public PageResult<EntityMap> findPagedList(int start, int limit);

	@Select(value = "SELECT * FROM ${tableName}  WHERE createdByUser = ?")
	public List<EntityMap> getListByUserId(long userId);
}
