package com.ajaxjs.data_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.data_service.service.IAdminService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.easy_controller.ControllerMethod;

public interface AdminController extends IAdminService {
	@GetMapping
	@ControllerMethod("数据服务列出命令")
	@Override
	PageResult<DataServiceTable> list(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit, Long datasourceId);

	@PostMapping
	@ControllerMethod("数据服务创建命令")
	@Override
	DataServiceTable create(DataServiceTable entity);

	@PutMapping("/{id}")
	@ControllerMethod("数据服务更新命令")
	@Override
	Boolean update(@PathVariable long id, DataServiceTable entity);

	@DeleteMapping("/{id}")
	@ControllerMethod("数据服务删除命令")
	@Override
	Boolean delete(@PathVariable long id);

	@GetMapping("/reload")
	@ControllerMethod("重新加载配置")
	@Override
	Boolean reload();

	@GetMapping("/getDatabases")
	@ControllerMethod("查询数据库库名")
	@Override
	List<String> getDatabases(long datasourceId);

	@GetMapping("/{id}")
	@ControllerMethod("查询表详情")
	@Override
	DataServiceTable getInfo(@PathVariable long id, String dbName);
	
	@GetMapping("/getAllFieldsByDataSourceAndTablename")
	@ControllerMethod("getAllFieldsByDataSourceAndTablename")
	@Override
	List<Map<String, String>> getAllFieldsByDataSourceAndTablename(long datasourceId, String tableName, String dbName);
}
