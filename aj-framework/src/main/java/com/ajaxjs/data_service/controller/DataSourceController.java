package com.ajaxjs.data_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.model.MyDataSource;
import com.ajaxjs.data_service.service.IDataSourceService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.easy_controller.ControllerMethod;
import com.ajaxjs.util.filter.DataBaseFilter;

@RestController
@RequestMapping("/admin/datasource")
public interface DataSourceController extends IDataSourceService {
	@GetMapping
	@DataBaseFilter
	@ControllerMethod("数据源列表")
	@Override
	List<MyDataSource> list(String appId);

	@PostMapping
	@ControllerMethod("创建数据源")
	@Override
	Long create(MyDataSource entity);

	@PutMapping("/{id}")
	@ControllerMethod("修改数据源")
	@Override
	Boolean update(@PathVariable long id);

	@GetMapping("/{id}/getSelectTables")
	@ControllerMethod("获取某个数据源下面的所有表")
	@Override
	List<TableInfo> getSelectTables(@PathVariable("id") Long dataSourceId, String dbName);

	@GetMapping("/getAllTables")
	@ControllerMethod("单数据源返回数据源下的表名和表注释")
	@Override
	PageResult<Map<String, Object>> getAllTables(Integer start, Integer limit, String tablename, String dbName);

	@GetMapping("/{id}/getAllTables")
	@ControllerMethod("指定数据源返回数据源下的表名和表注释")
	@Override
	PageResult<Map<String, Object>> getTableAndComment(@PathVariable("id") Long dataSourceId, Integer start, Integer limit, String tablename,
			String dbName);

	@GetMapping("/{id}/getFields/{tableName}")
	@ControllerMethod("获取所有字段")
	@Override
	List<Map<String, String>> getFields(@PathVariable("id") Long datasourceId, @PathVariable String tableName, String dbName);
}
