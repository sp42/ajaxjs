package com.ajaxjs.user.service;

import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.annotation.TableName;

public class GlobalLogService extends BaseService<Map<String, Object>> {
	@TableName(value = "general_log", beanClass = Map.class)
	public static interface GlobalLogDao extends IBaseDao<Map<String, Object>> {
	}

	public GlobalLogDao dao = new Repository().bind(GlobalLogDao.class);

	{
		setUiName("操作日志");
		setShortName("globalLog");
		setDao(dao);
	}
}
