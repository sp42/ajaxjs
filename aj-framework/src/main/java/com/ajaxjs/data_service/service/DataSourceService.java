package com.ajaxjs.data_service.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceDTO;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.util.filter.DataBaseFilter;

/**
 * 数据源管理
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface DataSourceService extends DataServiceDAO, DataServiceDTO {
	/**
	 * 获取某个数据源下面的所有表
	 * 
	 * @param dataSourceId 数据源 id
	 * @param dbName
	 * @return
	 */
	@GetMapping("/{id}/getSelectTables")
	@ControllerMethod("获取某个数据源下面的所有表")
	List<TableInfo> getSelectTables(@PathVariable("id") Long dataSourceId, String dbName);

	/**
	 * 单数据源返回数据源下的表名和表注释
	 *
	 * @param start
	 * @param limit
	 * @param tablename 搜索的关键字
	 * @return
	 * @throws SQLException
	 */
	@GetMapping("/getAllTables")
	@ControllerMethod("单数据源返回数据源下的表名和表注释")
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
	@GetMapping("/{id}/getAllTables")
	@ControllerMethod("指定数据源返回数据源下的表名和表注释")
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
	@GetMapping("/{id}/getFields/{tableName}")
	@ControllerMethod("获取所有字段")
	List<Map<String, String>> getFields(Long datasourceId, String tableName, String dbName) throws ClassNotFoundException, SQLException;
}
