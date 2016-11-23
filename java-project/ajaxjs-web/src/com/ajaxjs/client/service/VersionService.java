package com.ajaxjs.client.service;

import java.util.List;

import com.ajaxjs.client.dao.VersionDAO;
import com.ajaxjs.client.model.Version;
import com.ajaxjs.framework.exception.ServiceException;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.service.BaseCrudService;
import com.ajaxjs.framework.service.DAO_callback;

public class VersionService extends BaseCrudService<Version, VersionDAO> {
	public VersionService() {
		setMapper(VersionDAO.class);
		setTableName("version");
		setUiName("版本更新");
	}

	public Version getNew() throws ServiceException {
		return getOne(new DAO_callback<Version, VersionDAO>() {
			@Override
			public List<Version> getList(VersionDAO dao, int start, int limit, String sql_TableName, Query query) {
				return null;
			}

			@Override
			public Version getOne(VersionDAO dao) {
				return dao.selectTopVersion();
			}
		});
	}
}
