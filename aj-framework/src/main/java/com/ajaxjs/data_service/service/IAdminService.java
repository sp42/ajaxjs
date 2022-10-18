package com.ajaxjs.data_service.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.data_service.DataServiceDAO;
import com.ajaxjs.data_service.model.DataServiceDTO;
import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.framework.PageResult;

/**
 * 数据服务后台管理
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface IAdminService extends DataServiceDAO, DataServiceDTO {
	/**
	 * 列出命令
	 * 
	 * @param start
	 * @param limit
	 * @param datasourceId
	 * @return
	 */
	PageResult<DataServiceTable> list(int start, int limit, Long datasourceId);

	/**
	 * 创建命令
	 * 
	 * @param entity
	 * @return
	 */
	DataServiceTable create(DataServiceTable entity);

	/**
	 * 更新命令
	 * 
	 * @param id
	 * @return
	 */
	Boolean update(long id, DataServiceTable entity);

	/**
	 * 删除命令
	 * 
	 * @param id
	 * @return
	 */
	Boolean delete(long id);

	/**
	 * 重新加载配置
	 * 
	 * @return
	 */
	Boolean reload();

	/**
	 * 查询数据库库名
	 * 
	 * @param datasourceId
	 * @return
	 */
	List<String> getDatabases(long datasourceId);

	/**
	 * 查询表详情
	 * 
	 * @param id     命令 id
	 * @param dbName
	 * @return
	 */
	DataServiceTable getInfo(long id, String dbName);

	/**
	 * 接口重复
	 * 
	 * @param datasourceId
	 * @param tableName
	 * @param dbName
	 * @return
	 */
	@Deprecated
	List<Map<String, String>> getAllFieldsByDataSourceAndTablename(long datasourceId, String tableName, String dbName);
}
