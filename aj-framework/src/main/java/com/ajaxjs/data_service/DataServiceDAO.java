package com.ajaxjs.data_service;

import com.ajaxjs.data_service.model.DataServiceEntity;
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
	@TableName(value = "bdp_data_service", beanClass = DataServiceEntity.class)
	static interface DataServiceAdminDao extends IBaseDao<DataServiceEntity> {
		@Select("SELECT id, urlDir FROM ${tableName} WHERE datasourceId =? AND tableName = ?")
		DataServiceEntity findCCA(long datasourceId, String tableName);

		@Select("SELECT id FROM ${tableName} WHERE urlDir = ? AND datasourceId = ? LIMIT 1")
		DataServiceEntity findRepeatUrlDirAndDsId(String urlDir, long id);

		@Select("SELECT id FROM ${tableName} WHERE urlDir = ? LIMIT 1")
		DataServiceEntity findRepeatUrlDir(String urlDir);

		@Select("SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') AND datasourceId = ? ORDER BY urlDir DESC LIMIT 1")
		String findRepeatUrlDirAndDsIdMaxId(String urlDir, long id);

		@Select("SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') ORDER BY urlDir DESC LIMIT 1")
		String findRepeatUrlDirMaxId(String urlDir);
	}

	public static DataServiceAdminDao DataServiceAdminDAO = new Repository().bind(DataServiceAdminDao.class);
}
