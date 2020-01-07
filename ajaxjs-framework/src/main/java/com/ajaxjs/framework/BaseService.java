package com.ajaxjs.framework;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.ajaxjs.cms.app.catalog.CatalogService;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.SnowflakeIdWorker;
import com.ajaxjs.util.CommonUtil;

/**
 * 基础业务类
 * 
 * @author Frank Cheung
 *
 * @param <T> 实体
 */
public abstract class BaseService<T> extends QueryTools implements IBaseService<T> {
	@Override
	public T findById(Long id) {
		return dao.findById(id);
	}

	/**
	 * 按实体唯一 id查找单个实体
	 * 
	 * @param uid 实体唯一 id
	 * @return 实体
	 */
	public T findByUid(long uid) {
		return dao.find(byUid(uid));
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

	@Override
	public boolean delete(T bean) {
		return dao.delete(bean);
	}

	@Override
	public List<T> findList() {
		return dao.findList(null);
	}

	@Override
	public List<T> findList(Function<String, String> sqlHandler) {
		return dao.findList(sqlHandler);
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
	 * 有简单分类功能
	 * 
	 * @param catalogId
	 * @param start
	 * @param limit
	 * @param status
	 * @param isSimpleCatalog 是否递归分类查询
	 * @return
	 */
	public PageResult<T> findPagedList(int catalogId, int start, int limit, int status, boolean isSimpleCatalog) {
		Function<String, String> fn = setStatus(status).andThen(BaseService::searchQuery);

		if (catalogId != 0)
			fn = fn.andThen(isSimpleCatalog ? by("catalogId", catalogId) : CatalogService.setCatalog(catalogId));

		return dao.findPagedList(start, limit, fn);
	}

	private IBaseDao<T> dao;

	private String uiName;

	private String tableName;

	private String shortName;

	@Override
	public IBaseDao<T> getDao() {
		return dao;
	}

	public void setDao(IBaseDao<T> dao) {
		this.dao = dao;
	}

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
	 * 谨慎使用！这查询权力很大，可指定任意的字段
	 * 
	 * @return SQL 处理器
	 */
	public static Function<String, String> byAny() {
		return byAny(MvcRequest.getHttpServletRequest());
	}

	/**
	 * 对 name、content 字段搜索
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String searchQuery(String sql) {
		return searchQuery(new String[] { "name", "content" }, MvcRequest.getHttpServletRequest()).apply(sql);
	}

	/**
	 * 时间范围的查询
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String betweenCreateDate(String sql) {
		return betweenCreateDate("createDate", MvcRequest.getHttpServletRequest()).apply(sql);
	}
}
