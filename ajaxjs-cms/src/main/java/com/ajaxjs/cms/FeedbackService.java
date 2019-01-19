package com.ajaxjs.cms;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean("FeedbackService")
public class FeedbackService extends BaseService<Map<String, Object>> {
	@TableName(value = "entity_feedback")
	public static interface FeedbackDao extends IBaseDao<Map<String, Object>> {
		@Select(value = "SELECT e.*, u.name AS userName FROM ${tableName} e LEFT JOIN user u ON u.id = e.createdByUser")
		@Override
		public PageResult<Map<String, Object>> findPagedList(int start, int limit);

		@Select(value = "SELECT * FROM ${tableName}  WHERE createdByUser = ?")
		public List<Map<String, Object>> getListByUserId(long userId);
	}

	FeedbackDao dao = new Repository().bind(FeedbackDao.class);

	{
		setUiName("留言反馈");
		setShortName("feedback");
		setDao(dao);
	}
}
