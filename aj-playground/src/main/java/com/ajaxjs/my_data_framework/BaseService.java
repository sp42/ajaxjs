/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.my_data_framework;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.util.StringUtils;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.util.SnowflakeId;

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
//		initCreate(bean);
		return dao.create(bean);
	}

	public static void initCreate(Object bean) {
		Objects.requireNonNull(bean, "Bean 实体不能为空");

		if (bean instanceof BaseModel) {
			BaseModel model = (BaseModel) bean;
			if (model.getUid() == null)
				model.setUid(SnowflakeId.get());

			Date now = new Date();

			if (model.getCreateDate() == null)
				model.setCreateDate(now);
			if (model.getUpdateDate() == null)
				model.setUpdateDate(now);
		} else if (bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) bean;
			if (map.get("uid") == null)
				map.put("uid", SnowflakeId.get());

			Date now = new Date();
			Object createDate = map.get("createDate");

			if (createDate == null || (createDate != null && !StringUtils.hasText(createDate.toString())))
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

//		if (catalogId != 0)
//			fn = fn.andThen(isSimpleCatalog ? by("catalogId", catalogId) : TreeLikeService.setCatalog(catalogId));

		return dao.findPagedList(start, limit, fn);
	}

	private IBaseDao<T> dao;

	private String tableName;

	@Override
	public IBaseDao<T> getDao() {
		return dao;
	}

	public void setDao(IBaseDao<T> dao) {
		this.dao = dao;
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 谨慎使用！这查询权力很大，可指定任意的字段
	 * 
	 * @return SQL 处理器
	 */
	public static Function<String, String> byAny() {
		return byAny(BaseController.getRequest());
	}

	/**
	 * 对 name、content 字段搜索
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String searchQuery(String sql) {
		return searchQuery(new String[] { "name", "content" }, BaseController.getRequest()).apply(sql);
	}

	/**
	 * 对 name 字段搜索
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String searchQuery_NameOnly(String sql) {
		return searchQuery(new String[] { "name" }, BaseController.getRequest()).apply(sql);
	}

	/**
	 * 时间范围的查询
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String betweenCreateDate(String sql) {
		return betweenCreateDate("createDate", BaseController.getRequest()).apply(sql);
	}

	/**
	 * 时间范围的查询
	 * 
	 * @param sql 输入的SQL
	 * @return 修改后的 SQL
	 */
	public static String betweenCreateDateWithE(String sql) {
		return betweenCreateDate("e.createDate", BaseController.getRequest()).apply(sql);
	}
}
