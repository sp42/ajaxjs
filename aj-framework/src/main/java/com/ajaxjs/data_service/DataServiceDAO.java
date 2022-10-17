package com.ajaxjs.data_service;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

/**
 * DAO
 * 
 * @author Frank Cheung
 *
 */
public interface DataServiceDAO {
	@TableName(value = "bdp_datasource", beanClass = MyDataSource.class)
	static interface DataSourceDao extends IBaseDao<MyDataSource> {
		@Select("SELECT e.*, p.name AS projectName FROM ${tableName} e INNER JOIN project p ON e.projectId = p.id " + WHERE_REMARK_ORDER)
		PageResult<MyDataSource> findPagedList(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT * FROM ${tableName} e WHERE projectId = ?" + WHERE_REMARK_AND)
		List<MyDataSource> getDsByProjectId(long projectId);

		@Select("SELECT d.urlDir AS dataSourceUrlDir, d.* FROM datasource d " + "INNER JOIN project p ON d.projectId = p.id WHERE d.id = ?")
		MyDataSource getUrlDir(long dataSourceId);

		@Select("SELECT tableName FROM datasource_table WHERE dataSourceId = ?")
		String[] findSelectedTables(long dataSourceId);

		@Select("SELECT id FROM ${tableName} WHERE urlDir = ? AND id != ? LIMIT 1")
		boolean isRepeatUrlDir(String urlDir, long id);
	}

	public static DataSourceDao DataSourceDAO = new Repository().bind(DataSourceDao.class);

	@TableName(value = "bdp_data_service", beanClass = DataServiceTable.class)
	static interface DataServiceAdminDao extends IBaseDao<DataServiceTable> {
		@Select("SELECT id, urlDir FROM ${tableName} WHERE datasourceId =? AND tableName = ?")
		DataServiceTable findCCA(long datasourceId, String tableName);

		@Select("SELECT id FROM ${tableName} WHERE urlDir = ? AND datasourceId = ? LIMIT 1")
		DataServiceTable findRepeatUrlDirAndDsId(String urlDir, long id);

		@Select("SELECT id FROM ${tableName} WHERE urlDir = ? LIMIT 1")
		DataServiceTable findRepeatUrlDir(String urlDir);

		@Select("SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') AND datasourceId = ? ORDER BY urlDir DESC LIMIT 1")
		String findRepeatUrlDirAndDsIdMaxId(String urlDir, long id);

		@Select("SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') ORDER BY urlDir DESC LIMIT 1")
		String findRepeatUrlDirMaxId(String urlDir);
	}

	public static DataServiceAdminDao DataServiceAdminDAO = new Repository().bind(DataServiceAdminDao.class);
}
