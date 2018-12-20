package com.ajaxjs.cms;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.orm.dao.PageResult;

@Bean("FeedbackService")
public class FeedbackService extends BaseService<EntityMap> {
	@TableName(value = "entity_feedback", beanClass = EntityMap.class)
	public static interface FeedbackDao extends IBaseDao<EntityMap> {
		@Select(value = "SELECT e.*, u.name AS userName FROM ${tableName} e LEFT JOIN user u ON u.id = e.createdByUser")
		@Override
		public PageResult<EntityMap> findPagedList(int start, int limit);

		@Select(value = "SELECT * FROM ${tableName}  WHERE createdByUser = ?")
		public List<EntityMap> getListByUserId(long userId);
	}

	FeedbackDao dao = new Repository().bind(FeedbackDao.class);

	{
		setUiName("留言反馈");
		setShortName("feedback");
		setDao(dao);
	}
}
