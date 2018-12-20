package com.ajaxjs.cms.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.app.catelog.Catelog;
import com.ajaxjs.cms.app.catelog.CatelogService;
import com.ajaxjs.cms.app.catelog.CatelogServiceImpl;
import com.ajaxjs.cms.app.catelog.Catelogable;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.orm.dao.PageResult;

public class DomainEntityService extends BaseService<DomainEntity> implements Catelogable<DomainEntity> {
	DomainEntityDao dao;
	
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
		setShortName(shortName);
	}

	public Map<Integer, List<DomainEntity>> getHome() {
		Map<Integer, List<DomainEntity>> data = new HashMap<>();
		List<DomainEntity> list = dao.findHome(1, 2, 3);

		for (DomainEntity item : list) {
			Integer catelog = (Integer) item.getCatelog();

			List<DomainEntity> itemList;
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

	@Resource("CatelogService")
	CatelogService catelogService = new CatelogServiceImpl();

	public Map<Integer, String> getDomainCatelogMap() throws ServiceException {
		List<Catelog> catelogList = catelogService.findByParentId(6);
		Map<Integer, String> catelogMap = new HashMap<>();

		for (Catelog c : catelogList) {
			catelogMap.put(c.getId().intValue(), c.getName());
		}

		return catelogMap;
	}
	
	@Override
	public DomainEntity findById(Long id) {
		return dao.findById_catelog_avatar(id);
	}

	@Override
	public List<DomainEntity> findListByCatelogId(int catelogId) {
		return null;
	}

	@Override
	public PageResult<DomainEntity> findPagedListByCatelogId(int catelogId, int start, int limit) {
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