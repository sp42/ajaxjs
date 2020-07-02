package com.ajaxjs.app;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;

@Component("FeedbackService")
public class FeedbackService extends BaseService<Feedback> {
	@TableName(value = "entity_feedback", beanClass = Feedback.class)
	public static interface FeedbackDao extends IBaseDao<Feedback> {
		public static final String SQL = "SELECT *, (SELECT name FROM user WHERE id = e.userId) AS userName FROM ${tableName} e WHERE " + WHERE_REMARK;

		@Select(value = SQL + " ORDER BY e.id DESC")
		@Override
		public PageResult<Feedback> findPagedList(int start, int limit, Function<String, String> fn);

		@Select(value = SQL + " AND e.id = ?")
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
