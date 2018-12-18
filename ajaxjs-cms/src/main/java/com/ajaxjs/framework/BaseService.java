package com.ajaxjs.framework;

import java.util.Date;
import java.util.List;

import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.thirdparty.SnowflakeIdWorker;

public abstract class BaseService<T extends IBaseBean> implements IBaseService<T> {

	@Override
	public IBaseDao<T> getDao() {
		return dao;
	}

	public void setDao(IBaseDao<T> dao) {
		this.dao = dao;
	}

	private IBaseDao<T> dao;

	@Override
	public T findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public Long create(T bean) {
		SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
		bean.setUid(idWorker.nextId());
		Date now = new Date();

		if (bean.getCreateDate() == null)
			bean.setCreateDate(now);
		if (bean.getUpdateDate() == null)
			bean.setUpdateDate(now);
		
		return dao.create(bean);
	}

	@Override
	public int update(T bean) {
		Date now = new Date();
		if (bean.getUpdateDate() == null)
			bean.setUpdateDate(now);
		
		return dao.update(bean);
	}

	@Override
	public boolean delete(T bean) {
		return dao.delete(bean);
	}

	public List<T> findList() {
		return null;
	}

	@Override
	public PageResult<T> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	private String uiName;
	private String tableName;
	private String shortName;

	@Override
	public String getUiName() {
		return uiName;
	}

	public void setUiName(String uiName) {
		this.uiName = uiName;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}