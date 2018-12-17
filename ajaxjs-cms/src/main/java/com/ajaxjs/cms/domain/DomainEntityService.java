package com.ajaxjs.cms.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.app.catelog.Catelog;
import com.ajaxjs.cms.app.catelog.CatelogService;
import com.ajaxjs.cms.app.catelog.CatelogServiceImpl;
import com.ajaxjs.cms.app.catelog.Catelogable;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;

public class DomainEntityService implements IService<DomainEntity, Long>, Catelogable<DomainEntity> {
	DomainEntityDao dao;

	public DomainEntityService(String tableName) {
		dao = new DaoHandler().bind(DomainEntityDao.class, tableName);
	}

	public DomainEntityService(String tableName, int domainCatelogId) {
		setDomainCatelogId(domainCatelogId);

		dao = new DaoHandler().bind(DomainEntityDao.class, tableName);
	}

	public DomainEntityService(String tableName, int domainCatelogId, String uiName) {
		this(tableName, domainCatelogId);
		setName(uiName);
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
	public List<DomainEntity> findListByCatelogId(int catelogId) {
		return null;
	}

	@Override
	public PageResult<DomainEntity> findPagedListByCatelogId(int catelogId, int start, int limit) {
		if (catelogId == 0)
			catelogId = getDomainCatelogId();

		return dao.findListByCatelogId(catelogId, start, limit == 0 ? 5 : limit);
	}

	private int domainCatelogId;

	@Override
	public int getDomainCatelogId() {
		return domainCatelogId;
	}

	public void setDomainCatelogId(int domainCatelogId) {
		this.domainCatelogId = domainCatelogId;
	}

	@Override
	public DomainEntity findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<DomainEntity> findList() {
		return null;
	}

	@Override
	public PageResult<DomainEntity> findPagedList(int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long create(DomainEntity bean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(DomainEntity bean) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean delete(DomainEntity bean) {
		// TODO Auto-generated method stub
		return false;
	}

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTableName() {
		return null;
	}

}