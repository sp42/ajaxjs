package com.ajaxjs.framework;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.ajaxjs.cms.service.TreeLikeService;
import com.ajaxjs.sql.SimpleSnowflakeId;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 基础业务类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 * @param <T> 实体类型，可以为 Map 或 Java Bean
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
		initCreate(bean);
		return dao.create(bean);
	}

	public static void initCreate(Object bean) {
		Objects.requireNonNull(bean, "Bean 实体不能为空");

		if (bean instanceof BaseModel) {
			BaseModel model = (BaseModel) bean;
			if (model.getUid() == null)
				model.setUid(SimpleSnowflakeId.get());

			Date now = new Date();

			if (model.getCreateDate() == null)
				model.setCreateDate(now);
			if (model.getUpdateDate() == null)
				model.setUpdateDate(now);

		} else if (bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) bean;
			if (map.get("uid") == null)
				map.put("uid", SimpleSnowflakeId.get());

			Date now = new Date();
			Object createDate = map.get("createDate");

			if (createDate == null || (createDate != null && CommonUtil.isEmptyString(createDate.toString())))
				map.put("createDate", now);
			
			if (map.get("updateDate") == null)
				map.put("updateDate", now);
		}
	}

	@Override
	public int update(T bean) {
		initUpdate(bean);
		return dao.update(bean);
	}

	/**
	 * 通用的更新操作
	 * 
	 * @param bean
	 */
	public static void initUpdate(Object bean) {
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
		Function<String, String> fn = setStatus(status).andThen(BaseService::searchQuery).andThen(byAny());

		if (catalogId != 0)
			fn = fn.andThen(isSimpleCatalog ? by("catalogId", catalogId) : TreeLikeService.setCatalog(catalogId));

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
	 * 对 name 字段搜索
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String searchQuery_NameOnly(String sql) {
		return searchQuery(new String[] { "name" }, MvcRequest.getHttpServletRequest()).apply(sql);
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
	/**
	 * 时间范围的查询
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String betweenCreateDateWithE(String sql) {
		return betweenCreateDate("e.createDate", MvcRequest.getHttpServletRequest()).apply(sql);
	}
}
