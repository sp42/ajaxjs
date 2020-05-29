package com.ajaxjs.app.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.service.UserDao;

@Bean("FeedbackService")
public class FeedbackService extends BaseService<Feedback> {
	@TableName(value = "entity_feedback", beanClass = Map.class)
	public static interface FeedbackDao extends IBaseDao<Feedback> {
		public static final String sql = "SELECT e.*, " + UserDao.getUserName + " FROM ${tableName} e LEFT JOIN user u ON u.id = e.userId WHERE 1=1";

		@Select(value = sql + " ORDER BY e.id DESC")
		@Override
		public PageResult<Feedback> findPagedList(int start, int limit, Function<String, String> fn);

		@Select(value = sql + " AND e.id = ?")
		@Override
		public Feedback findById(Long id);

		@Select(value = "SELECT * FROM ${tableName} WHERE userId = ?")
		public List<Feedback> getListByUserId(long userId);
	}

	FeedbackDao dao = new Repository().bind(FeedbackDao.class);

	{
		setUiName("留言反馈");
		setShortName("feedback");
		setDao(dao);
	}
}
