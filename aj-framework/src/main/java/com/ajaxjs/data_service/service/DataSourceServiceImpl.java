package com.ajaxjs.data_service.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.framework.PageResult;

public class DataSourceServiceImpl implements DataSourceService {
	@Override
	public List<DataSourceInfo> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(DataSourceInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean update(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TableInfo> getSelectTables(Long dataSourceId, String dbName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<Map<String, Object>> getAllTables(Integer start, Integer limit, String tablename, String dbName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<Map<String, Object>> getTableAndComment(Long dataSourceId, Integer start, Integer limit, String tablename, String dbName)
			throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, String>> getFields(Long datasourceId, String tableName, String dbName) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
