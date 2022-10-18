package com.ajaxjs.data_service.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.framework.PageResult;

public class AdminService implements IAdminService {
	@Override
	public PageResult<DataServiceTable> list(int start, int limit, Long datasourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataServiceTable create(DataServiceTable entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(long id, DataServiceTable entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean reload() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDatabases(long datasourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataServiceTable getInfo(long id, String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getAllFieldsByDataSourceAndTablename(long datasourceId, String tableName, String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

}
