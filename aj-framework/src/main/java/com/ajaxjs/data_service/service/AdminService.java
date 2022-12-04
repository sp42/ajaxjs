package com.ajaxjs.data_service.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceDTO;
import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.spring.easy_controller.ControllerMethod;

/**
 * 数据服务后台管理
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface AdminService extends DataServiceDAO, DataServiceDTO {
	/**
	 * 列出命令
	 * 
	 * @param start
	 * @param limit
	 * @param datasourceId
	 * @return
	 */
	@GetMapping
	@ControllerMethod("数据服务列出命令")
	PageResult<DataServiceTable> list(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit, Long datasourceId);

	/**
	 * 创建命令
	 * 
	 * @param entity
	 * @return
	 */
	@PostMapping
	@ControllerMethod("数据服务创建命令")
	DataServiceTable create(@RequestBody DataServiceTable entity);

	/**
	 * 更新命令
	 * 
	 * @param id
	 * @return
	 */
	@PutMapping("/{id}")
	@ControllerMethod("数据服务更新命令")
	Boolean update(@PathVariable long id, @RequestBody DataServiceTable entity);

	/**
	 * 删除命令
	 * 
	 * @param id
	 * @return
	 */

	@DeleteMapping("/{id}")
	@ControllerMethod("数据服务删除命令")
	Boolean delete(@PathVariable long id);

	/**
	 * 重新加载配置
	 * 
	 * @return
	 */
	@GetMapping("/reload")
	@ControllerMethod("重新加载配置")
	Boolean reload();

	/**
	 * 查询数据库库名
	 * 
	 * @param datasourceId
	 * @return
	 */
	@GetMapping("/getDatabases")
	@ControllerMethod("查询数据库库名")
	List<String> getDatabases(long datasourceId);

	/**
	 * 查询表详情
	 * 
	 * @param id     命令 id
	 * @param dbName
	 * @return
	 */
	@GetMapping("/{id}")
	@ControllerMethod("查询表详情")
	DataServiceTable getInfo(@PathVariable long id, String dbName);

	/**
	 * 接口重复
	 * 
	 * @param datasourceId
	 * @param tableName
	 * @param dbName
	 * @return
	 */
	@GetMapping("/getAllFieldsByDataSourceAndTablename")
	@ControllerMethod("getAllFieldsByDataSourceAndTablename")
	@Deprecated
	List<Map<String, String>> getAllFieldsByDataSourceAndTablename(long datasourceId, String tableName, String dbName);
}
