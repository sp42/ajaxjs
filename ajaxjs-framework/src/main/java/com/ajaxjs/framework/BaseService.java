package com.ajaxjs.framework;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcHelper;
import com.ajaxjs.orm.SnowflakeIdWorker;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MappingValue;

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
		Objects.requireNonNull(bean, "Bean 实体不能为空");

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
			Object createDate = map.get("createDate");

			if (createDate == null || (createDate != null && CommonUtil.isEmptyString(createDate.toString())))
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
	 * 相邻记录
	 * 
	 * @TODO 没权限的不要列出
	 * 
	 * @param mv
	 * @param tableName
	 * @param id
	 */
	public static void getNeighbor(Map<String, Object> map, String tableName, Serializable id) {
		Map<String, Object> perv, next;
		perv = JdbcHelper.queryAsMap(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE id < ? ORDER BY id DESC LIMIT 1", id);
		next = JdbcHelper.queryAsMap(JdbcConnection.getConnection(), "SELECT id, name FROM " + tableName + " WHERE id > ? LIMIT 1", id);

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

	@Override
	public List<T> findList(Function<String, String> sqlHandler) {
		return dao.findList(sqlHandler);
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
		return dao.findPagedList(start, limit, null);
	}

	@Override
	public PageResult<T> findPagedList(int start, int limit, Function<String, String> sqlHandler) {
		return dao.findPagedList(start, limit, sqlHandler);
	}

	/**
	 * 根据关键字搜索的高阶函数
	 * 
	 * @param keyword
	 * @param isExact
	 * @return
	 */
	public static Function<String, String> likeSqlHandler(String field, String keyword, boolean isExact) {
		String _keyword = isExact ? keyword : "%" + keyword + "%";
		return sql -> sql.replace(IBaseDao.WHERE_REMARK, field + "LIKE " + _keyword + IBaseDao.WHERE_REMARK_AND);
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

	public static final int defaultPageSize = 6;

	/**
	 * 生成查询表达式的高阶函数
	 * 
	 * @param eq
	 * @return
	 */
	public static Function<String, String> addWhere(String eq) {
		return sql -> sql + " WHERE " + eq;
	}

	public static Function<String, String> setSqlHandler(String where) {
		return where == null ? sql -> sql : sql -> sql.replace(IBaseDao.WHERE_REMARK, where + IBaseDao.WHERE_REMARK_AND);
	}

	public static String searchQuery(String sql) {
		return searchQuery(sql, new String[] { "name", "content" });
	}

	public static String searchQuery(String sql, String[] fields) {
		HttpServletRequest r = MvcRequest.getHttpServletRequest();
		if (r == null || CommonUtil.isEmptyString(r.getParameter("keyword")))
			return sql;

		String keyword = r.getParameter("keyword"), isExact = r.getParameter("isExact");

		keyword = keyword.trim();

		String like = MappingValue.toBoolean(isExact) ? keyword : ("'%" + keyword + "%'");

		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i] + " LIKE " + like;
		}

		return setSqlHandler("(" + String.join(" OR ", fields) + ")").apply(sql);
	}

	public static Function<String, String> setStatus(int status) {
		String setStatus;

		switch (status) {
		case CommonConstant.DELTETED:
			setStatus = null;
			break;
		case CommonConstant.ON_LINE:
			setStatus = "(status = 1 OR status IS NULL)";
			break;
		case CommonConstant.ON_OFFLINE: // 常用于后台查看数据
		default:
			setStatus = "(status = 0 OR status = 1 OR status is NULL)";

		}

		return setSqlHandler(setStatus);
	}
}
