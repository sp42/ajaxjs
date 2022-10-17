package com.ajaxjs.data_service.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceDTO;
import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.framework.PageResult;

/**
 * 数据源管理
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface IDataSourceService extends DataServiceDAO, DataServiceDTO {
	/**
	 * 数据源列表
	 * 
	 * @param req
	 * @param appId
	 * @return
	 */
	List<MyDataSource> list(String appId);

	/**
	 * 创建数据源
	 * 
	 * @param entity
	 * @return
	 */
	Long create(MyDataSource entity);

	/**
	 * 修改数据源
	 * 
	 * @param id
	 * @return
	 */
	Boolean update(long id);

	/**
	 * 获取某个数据源下面的所有表
	 * 
	 * @param dataSourceId 数据源 id
	 * @param dbName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	List<TableInfo> getSelectTables(Long dataSourceId, String dbName) throws ClassNotFoundException, SQLException;

	/**
	 * 单数据源返回数据源下的表名和表注释
	 *
	 * @param start
	 * @param limit
	 * @param tablename 搜索的关键字
	 * @return
	 * @throws SQLException
	 */
	PageResult<Map<String, Object>> getAllTables(Integer start, Integer limit, String tablename, String dbName) throws SQLException;

	/**
	 * 指定数据源返回数据源下的表名和表注释
	 *
	 * @param dataSourceId
	 * @param start
	 * @param limit
	 * @param tablename    搜索的关键字
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	PageResult<Map<String, Object>> getTableAndComment(Long dataSourceId, Integer start, Integer limit, String tablename, String dbName)
			throws ClassNotFoundException, SQLException;

	/**
	 * 获取所有字段
	 * 
	 * @param datasourceId
	 * @param tableName
	 * @param dbName
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	List<Map<String, String>> getFields(Long datasourceId, String tableName, String dbName) throws ClassNotFoundException, SQLException;

}
