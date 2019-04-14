package com.ajaxjs.cms.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.app.catalog.Catalog;
import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.cms.app.catalog.CatalogServiceImpl;
import com.ajaxjs.cms.app.catalog.Catalogable;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Resource;

public class DomainEntityService extends BaseService<Map<String, Object>> implements Catalogable<Map<String, Object>> {
	public DomainEntityDao dao;

	public DomainEntityService(String tableName) {
		dao = new Repository().bind(DomainEntityDao.class, tableName);
		setDao(dao);
	}

	public DomainEntityService(String tableName, int domainCatelogId) {
		this(tableName);
		setDomainCatelogId(domainCatelogId);
	}

	public DomainEntityService(String tableName, int domainCatelogId, String uiName) {
		this(tableName, domainCatelogId);
		setUiName(uiName);
	}

	public DomainEntityService(String tableName, int domainCatelogId, String uiName, String shortName) {
		this(tableName, domainCatelogId, uiName);
		setShortName(shortName);
	}

	public DomainEntityService(String tableName, String configNodeName, String uiName, String shortName) {
		this(tableName, ConfigService.getValueAsInt(configNodeName), uiName, shortName);
	}

	/**
	 * 不用分类的
	 * 
	 * @param tableName
	 * @param uiName
	 * @param shortName
	 */
	public DomainEntityService(String tableName, String uiName, String shortName) {
		this(tableName);
		setUiName(uiName);
		setShortName(shortName);
	}

	public Map<Integer, List<Map<String, Object>>> getHome() {
		Map<Integer, List<Map<String, Object>>> data = new HashMap<>();
		List<Map<String, Object>> list = dao.findHome(1, 2, 3);
		
		if (list != null)
			for (Map<String, Object> item : list) {
				Integer catelog = (Integer) item.get("catelogId");

				List<Map<String, Object>> itemList;
				if (data.containsKey(catelog)) {
					itemList = data.get(catelog);
				} else {
					itemList = new ArrayList<>();
					data.put(catelog, itemList);
				}

				itemList.add(item);
			}

		return data;
	}

	@Resource("闪亮杯国际少儿音乐大赛")
	CatalogService catalogService = new CatalogServiceImpl();

	public Map<Integer, String> getDomainCatelogMap() {
		List<Catalog> catelogList = catalogService.findByParentId(6);
		Map<Integer, String> catelogMap = new HashMap<>();

		for (Catalog c : catelogList) {
			catelogMap.put(c.getId().intValue(), c.getName());
		}

		return catelogMap;
	}

	@Override
	public Map<String, Object> findById(Long id) {
		return dao.findById(id);
	}
	
	public Map<String, Object> findById_Cover(Long id) {
		return dao.findById_catelog_avatar(id);
	}

	@Override
	public List<Map<String, Object>> findListByCatelogId(int catelogId) {
		return null;
	}

	@Override
	public PageResult<Map<String, Object>> findPagedListByCatelogId(int catelogId, int start, int limit) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();

		
		return dao.findPagedListByCatelogId(catelogId, start, limit == 0 ? 5 : limit);
	}
	
	public PageResult<Map<String, Object>> findPagedListByCatelogId_Cover(int catelogId, int start, int limit) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();
		return dao.findPagedListByCatelogId_Cover(catelogId, start, limit == 0 ? 5 : limit);
	}

	private int domainCatelogId;

	@Override
	public int getDomainCatelogId() {
		return domainCatelogId;
	}

	public void setDomainCatelogId(int domainCatelogId) {
		this.domainCatelogId = domainCatelogId;
	}
}