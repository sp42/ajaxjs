package com.ajaxjs.framework;

import java.util.Map;

import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.orm.annotation.TableName;

/**
 * Map 的 CRUD 服务。期望一个实体的快速增删改查
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MapCRUDService extends BaseService<Map<String, Object>> {
	/**
	 * 该域下面的分类id
	 */
	private int domainCatalogId;

	@TableName(beanClass = Map.class)
	public static interface MapCRUDDao extends IBaseDao<Map<String, Object>> {
	}

	public MapCRUDDao dao;

	public MapCRUDService(String tableName) {
		dao = new Repository().bind(MapCRUDDao.class, tableName);
		setDao(dao);
	}

	public MapCRUDService(String tableName, int domainCatalogId) {
		this(tableName);
		setDomainCatalogId(domainCatalogId);
	}

	public MapCRUDService(String tableName, int domainCatelogId, String uiName) {
		this(tableName, domainCatelogId);
		setUiName(uiName);
	}

	public MapCRUDService(String tableName, int domainCatelogId, String uiName, String shortName) {
		this(tableName, domainCatelogId, uiName);
		setShortName(shortName);
	}

	public MapCRUDService(String tableName, String configNodeName, String uiName, String shortName) {
		this(tableName, ConfigService.getValueAsInt(configNodeName), uiName, shortName);
	}

	/**
	 * 不用分类的
	 * 
	 * @param tableName
	 * @param uiName
	 * @param shortName
	 */
	public MapCRUDService(String tableName, String uiName, String shortName) {
		this(tableName);
		setUiName(uiName);
		setShortName(shortName);
	}

	public int getDomainCatalogId() {
		return domainCatalogId;
	}

	public void setDomainCatalogId(int domainCatalogId) {
		this.domainCatalogId = domainCatalogId;
	}


	/**
	 * 关联类别表的
	 * 
	 * @param catelogId
	 * @param start
	 * @param limit
	 * @param status
	 * @return
	 */
	public PageResult<Map<String, Object>> findPagedListJoinCatalog(int catelogId, int start, int limit, int status) {
		return dao.findPagedList(0, 10, CatalogService.setCatalog(catelogId, getDomainCatalogId()).andThen(setStatus(status)).andThen(BaseService::searchQuery));
	}
}
