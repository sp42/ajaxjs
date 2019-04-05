package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.thirdparty.SnowflakeIdWorker;

/**
 * 基础业务类
 * 
 * @author Frank Cheung
 *
 * @param <T> 实体
 */
public abstract class BaseService<T> implements IBaseService<T> {
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
		if (bean instanceof BaseModel) {
			BaseModel model = (BaseModel) bean;
			if (model.getUid() == null)
				model.setUid(SnowflakeIdWorker.idWorker.nextId());

			Date now = new Date();

			if (model.getCreateDate() == null)
				model.setCreateDate(now);
			if (model.getUpdateDate() == null)
				model.setUpdateDate(now);
		} else if (bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) bean;
			if (map.get("uid") == null)
				map.put("uid", SnowflakeIdWorker.idWorker.nextId());

			Date now = new Date();
			if (map.get("createDate") == null)
				map.put("createDate", now);
			if (map.get("updateDate") == null)
				map.put("updateDate", now);
		}

		return dao.create(bean);
	}

	@Override
	public int update(T bean) {
		Date now = new Date();
		if (bean instanceof BaseModel) {
			BaseModel model = (BaseModel) bean;
			if (model.getUpdateDate() == null)
				model.setUpdateDate(now);
		} else if (bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) bean;
			if (map.get("updateDate") == null)
				map.put("updateDate", now);
		}

		return dao.update(bean);
	}

	/**
	 * 
	 * @param mv
	 * @param tableName
	 * @param id
	 */
	public static void getNeighbor(Map<String, Object> map, String tableName, Serializable id) {
		Map<String, Object> perv, next;
		perv = JdbcHelper.queryAsMap(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE status = 1 AND id < ? ORDER BY id DESC LIMIT 1", id);
		next = JdbcHelper.queryAsMap(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE status = 1 AND id > ? LIMIT 1", id);

		map.put("neighbor_pervInfo", perv);
		map.put("neighbor_nextInfo", next);
	}

	@Override
	public boolean delete(T bean) {
		return dao.delete(bean);
	}

	@Override
	public List<T> findList() {
		return dao.findList();
	}

	/**
	 * 简易的列表
	 * 
	 * @return
	 */
	public List<T> findSimpleList() {
		return dao.findSimpleList();
	}

	@Override
	public PageResult<T> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}
	
	@Override
	public PageResult<T> findPagedList(int start, int limit, Function<String, String> sqlHandler) {
		return dao.findPagedList(start, limit, sqlHandler);
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
