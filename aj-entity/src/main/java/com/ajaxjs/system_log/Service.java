package com.ajaxjs.system_log;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.teant.TenantDao;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

/**
 *
 */
public class Service extends BaseService<ServiceLog> implements IServiceLog {
	@TableName(value = "sys_log", beanClass = ServiceLog.class)
	public interface ServiceLogDao extends IBaseDao<ServiceLog>, TenantDao<ServiceLog> {
	}

	public static ServiceLogDao DAO = new Repository().bind(ServiceLogDao.class);

	{
		setDao(DAO);
	}

	@Override
	public List<ServiceLog> findByTenantId(long teantId) {
		return DAO.findByTenantId(teantId);
	}

	@Override
	public PageResult<ServiceLog> findPageByTenantId(long teantId) {
		return DAO.findPageByTenantId(teantId);
	}

}
