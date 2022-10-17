package com.ajaxjs.data_service.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.CollectionUtils;

import com.ajaxjs.data_service.model.DataServiceConstant;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.model.DataServiceFieldsMapping;
import com.ajaxjs.data_service.model.DataServiceTable;
import com.ajaxjs.data_service.model.ServiceContext;
import com.ajaxjs.data_service.mybatis.MSUtils;
import com.ajaxjs.data_service.mybatis.SqlMapper;
import com.ajaxjs.data_service.plugin.IPlugin;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.JdbcUtil;
import com.ajaxjs.sql.util.SnowflakeId;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 核心 CRUD
 * 
 * @author Frank Cheung
 *
 */
public abstract class ApiCommander implements DataServiceConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(ApiCommander.class);

	/**
	 * 
	 * @param ctx
	 * @param plugins
	 * @return
	 */
	public static Object get(ServiceContext ctx, List<IPlugin> plugins) {
		DataServiceDml node = ctx.getNode();

		if ("list".equals(node.getType()) || "getRows".equals(node.getType()) || "getRowsPage".equals(node.getType())) {
			if ((node.containsKey("pageMode") && (1 == (int) node.get("pageMode"))) || "getRowsPage".equals(node.getType()))
				return page(ctx, plugins);
			else
				return list(ctx, plugins); // 不分页
		} else
			return info(ctx, plugins);
	}

	private static boolean before(CRUD type, ServiceContext ctx, List<IPlugin> plugins) {
		boolean hasPlugins = !CollectionUtils.isEmpty(plugins);
		if (hasPlugins)
			for (IPlugin plugin : plugins) {
				if (!plugin.before(type, ctx))
					throw new Error(getErrMsg(ctx));
			}

		return hasPlugins;
	}

	/**
	 * 单笔查询，查询单个记录
	 * 
	 * @param ctx
	 * @param plugins
	 * @return 单个记录的 Map 如果返回 null 找不到数据
	 */
	public static Map<String, Object> info(ServiceContext ctx, List<IPlugin> plugins) {
		String sql = where(ctx);
		boolean hasPlugins = before(CRUD.INFO, ctx, plugins);
		Map<String, Object> result = null;

		try (SqlSession session = MSUtils.getMyBatisSession(ctx.getDatasource())) {
			result = new SqlMapper(session).selectOne(sql, ctx.getRequestParams());
		}

		try {// 避免 null point
			DataServiceFieldsMapping fieldsMapping = ctx.getNode().getTableInfo().getFieldsMapping();

			if (fieldsMapping != null && result != null) {
				if (fieldsMapping.getDbStyle2CamelCase() != null && fieldsMapping.getDbStyle2CamelCase()) {
					// 数据库风格 转换 驼峰
					Map<String, Object> camel = new HashMap<>(result.size());

					for (String key : result.keySet())
						camel.put(JdbcUtil.changeColumnToFieldName(key), result.get(key));

					result.clear(); // 提早清除
					result = camel;
				}
			}
		} catch (Throwable e) {
			LOGGER.warning(e);
		}

		if (hasPlugins)
			for (IPlugin plugin : plugins) {
				plugin.after(CRUD.INFO, ctx, result);
			}

		return result;
	}

	private static void toCaemlCase(ServiceContext ctx, List<Map<String, Object>> list) {
		DataServiceFieldsMapping m = ctx.getNode().getTableInfo().getFieldsMapping();

		if (m != null && m.getDbStyle2CamelCase() != null && m.getDbStyle2CamelCase()) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);

				if (map != null) {
					// 数据库风格 转换 驼峰
					Map<String, Object> camel = new HashMap<>();

					for (String key : map.keySet())
						camel.put(JdbcUtil.changeColumnToFieldName(key), map.get(key));

					map.clear(); // 提早清除
					list.set(i, camel);
				}
			}
		}
	}

	/**
	 * 列表查询（不分页）
	 * 
	 * @param ctx
	 * @param plugins
	 * @return
	 */
	public static List<Map<String, Object>> list(ServiceContext ctx, List<IPlugin> plugins) {
		String sql = where(ctx);
		boolean hasPlugins = before(CRUD.LIST, ctx, plugins);
		List<Map<String, Object>> list = null;

		if (isDynamicSQL(sql)) // 是否包含 mybatis 脚本控制标签，有的话特殊处理
			sql = "<script>" + sql + "</script>";

		try (SqlSession session = MSUtils.getMyBatisSession(ctx.getDatasource())) {
			list = new SqlMapper(session).selectList(sql, ctx.getRequestParams());
		}

		toCaemlCase(ctx, list);

		if (hasPlugins)
			for (IPlugin plugin : plugins) {
				plugin.after(CRUD.LIST, ctx, list);
			}

		return list;
	}

	/**
	 * 分页列表查询
	 * 
	 * @param ctx
	 * @param plugins
	 * @return
	 */
	public static PageResult<Map<String, Object>> page(ServiceContext ctx, List<IPlugin> plugins) {
		String sql = where(ctx);
		boolean hasPlugins = before(CRUD.PAGE_LIST, ctx, plugins);
		PageResult<Map<String, Object>> page = page(ctx.getDatasource(), sql, ctx.getRequestParams());
		toCaemlCase(ctx, page);

		if (hasPlugins)
			for (IPlugin plugin : plugins) {
				plugin.after(CRUD.PAGE_LIST, ctx, page);
			}

		return page;
	}

	/**
	 * 分页列表
	 *
	 * @param ds     数据源
	 * @param sql    SQL 语句
	 * @param params SQL 参数
	 * @return 分页列表的 JSON
	 */
	private static PageResult<Map<String, Object>> page(DataSource ds, String sql, Map<String, Object> params) {
//		LOGGER.info("获取列表");

		if (params == null)
			params = new HashMap<>();

		/* 判断分页参数，兼容 MySQL or 页面两者。最后统一使用 start/limit */
		int limit;

		if (params.containsKey("pageSize"))
			limit = (int) params.get("pageSize");
		else if (params.containsKey("limit"))
			limit = (int) params.get("limit");
		else
			limit = PageResult.DEFAULT_PAGE_SIZE;

		int start;

		if (params.containsKey("pageNo")) {
			int pageNo = (int) params.get("pageNo");
			start = JdbcUtil.pageNo2start(pageNo, limit);
		} else if (params.containsKey("start"))
			start = (int) params.get("start");
		else
			start = 0;

		params.put("start", start);
		params.put("limit", limit);

		try (SqlSession session = MSUtils.getMyBatisSession(ds)) {
			SqlMapper sqlMapper = new SqlMapper(session); // 先获取记录总数，若大于零继续查询，获取分页数据

//            int index = sql.lastIndexOf(";"); // 有分号影响子查询，去掉
//            if (index > 0)
//                sql = sql.substring(0, index);

			String getTotalSql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
			Map<String, Object> _params;
			boolean isDynamicSQL = isDynamicSQL(sql);

			if (isDynamicSQL) {// 是否包含 mybatis 脚本控制标签，有的话特殊处理
				getTotalSql = "<script>" + getTotalSql + "</script>";
				_params = new HashMap<>();
				_params.put("params", params);
			} else
				_params = params;

			Map<String, Object> t = sqlMapper.selectOne(getTotalSql, params);
			Long total = (Long) t.get("count");

			PageResult<Map<String, Object>> pageList = new PageResult<>();
			pageList.setTotalCount(total.intValue());

			if (total == 0) {
				pageList.setZero(true);
//                Map<String, Object> map = new HashMap<>();
//                map.put("result", null);
//                map.put("msg", "没有数据，查询结果为零");
//                map.put("total", 0);
			} else {
				sql += " LIMIT #{start}, #{limit}";

				if (isDynamicSQL)
					sql = "<script>" + sql + "</script>";

				List<Map<String, Object>> _list = sqlMapper.selectList(sql, params);
				pageList.addAll(_list);
			}

			return pageList;
		}
	}

	/**
	 * 查找 where 1=1 的正则
	 */
	private static final Pattern WHERE = Pattern.compile("(?i)WHERE\\s+1\\s?=\\s?1", Pattern.CASE_INSENSITIVE);

	static String where(ServiceContext ctx) {
		DataServiceDml node = ctx.getNode();
		String sql = node.getSql();
		Map<String, Object> params = ctx.getRequestParams();

		// SQL 条件查询
		if (params != null && node.isQuerySearch()) {
			Object queryParams = params.get("where");

			if (queryParams != null) {
				String where = queryParams.toString();
				Matcher matcher = WHERE.matcher(sql);

				if (matcher.find())
					sql = matcher.replaceAll("WHERE (" + where + ") ");
				else
					sql += " WHERE " + where; // 在最后处添加

				ctx.setSql(sql); // 保存 SQL 到 ctx 中
			}
		}

		return sql;
	}

	/**
	 * 
	 * @param ctx
	 * @param plugins
	 * @return
	 */
	public static Serializable create(ServiceContext ctx, List<IPlugin> plugins) {
//		LOGGER.info("数据服务 创建实体");

		DataServiceDml node = ctx.getNode();
		DataServiceTable tableInfo = node.getTableInfo();
		DataServiceFieldsMapping fieldsMapping = tableInfo.getFieldsMapping();

		boolean hasMapping = fieldsMapping != null; // 是否有字段映射
		String idField = hasMapping ? fieldsMapping.getId() : "id"; // 主键字段名称

		Map<String, Object> _params = ctx.getRequestParams();
		boolean hasIdValue = _params.containsKey(idField);

//		if (hasIdValue && node.isCreateOrSave())
//			return exec(ctx.getUri(), PUT, put(params, ctx));// create or save

		// id
		Integer keyGen = tableInfo.getKeyGen();
		if (keyGen != null && !hasIdValue) {// 如果已指定 id 参数，则不覆盖
			if (keyGen == KeyGen.SNOWFLAKE)
				_params.put(idField, SnowflakeId.get());

			if (keyGen == KeyGen.UUID) {
				String uuid = UUID.randomUUID().toString();
				_params.put(idField, uuid);
			}

			// 其他情况，自增
		}

		if (node.isAddUuid())
			_params.put("uid", SnowflakeId.get());

		if (node.isAutoDate()) {// 创建修改时间
			Date now = new Date();
			_params.put(hasMapping ? fieldsMapping.getCreateDate() : "updateDate", now);
			_params.put(hasMapping ? fieldsMapping.getUpdateDate() : "createDate", now);
		}

		boolean hasPlugins = before(CRUD.CREATE, ctx, plugins);

		// 是否转换数据库风格
		if (fieldsMapping.getCamelCase2DbStyle() != null && fieldsMapping.getCamelCase2DbStyle()) {
			// 驼峰 转换 数据库风格
			Map<String, Object> dbStyle = new HashMap<>(_params.size());

			for (String key : _params.keySet())
				dbStyle.put(JdbcUtil.changeFieldToColumnName(key), _params.get(key));

			_params.clear(); // 提早清除
			_params = dbStyle;
		}

		Map<String, Object> params;
		String sql = node.getSql();

		if (isDynamicSQL(sql)) {
			sql = "<script>" + sql + "</script>";
			params = new HashMap<>();
			params.put("params", _params);
		} else
			params = _params;

		ctx.setSql(sql);
		ctx.setSqlParam(params);
		LOGGER.info("创建实体:" + params);

		try (SqlSession session = MSUtils.getMyBatisSession(node.getDataSource())) {
			int effectedRow = new SqlMapper(session).insert(sql, params);
			session.commit();

			if (effectedRow > 0) {
				if (hasPlugins)
					for (IPlugin plugin : plugins) {
						plugin.after(CRUD.CREATE, ctx, params);
					}

				if (params != null && params.containsKey(idField)) {
					Object id = params.get(idField);

					if (id instanceof String) {
						String idStr = id.toString();
						// might be UUID, that is the string
						long idLong = Long.parseLong(idStr); // TODO will it be a format exception if UUID?

						if (idStr.equals(idLong + "")) // if it covernted, the same, that means it should be the Long
							return idLong;
						else
							return idStr;
					} else if (id instanceof BigInteger)
						return ((BigInteger) id).longValue();
					else
						return (Serializable) params.get(idField);
//					long newlyId = MappingValue.object2long(params.get(idField));
				} else if (params.containsKey("params")) {
					if (params.get("params") instanceof Map) {
						Object object = ((Map<?, ?>) params.get("params")).get(idField);

						return object != null ? object.toString() : NOT_AUTOCREMENT_ID;
					} else
						return NOT_AUTOCREMENT_ID;
				} else
					return NOT_AUTOCREMENT_ID;
			}
		}

		return null;
	}

	/**
	 * 不是自增 id，数据库没定义自增 id，可能是 uuid，数据库自动生成
	 */
	public static final String NOT_AUTOCREMENT_ID = "NOT_AUTOCREMENT_ID";

	/**
	 * 修改记录
	 * 
	 * @param ctx
	 * @param plugins
	 * @return
	 */
	public static boolean update(ServiceContext ctx, List<IPlugin> plugins) {
//		LOGGER.info("数据服务 修改实体");
		DataServiceDml node = ctx.getNode();
		Map<String, Object> _params = ctx.getRequestParams();
		DataServiceFieldsMapping fieldsMapping = node.getTableInfo().getFieldsMapping();

		if (node.isUpdateDate())
			_params.put(fieldsMapping != null ? fieldsMapping.getUpdateDate() : "updateDate", new Date());

		boolean hasPlugins = before(CRUD.UPDATE, ctx, plugins);

		// 是否转换数据库风格
		if (fieldsMapping.getCamelCase2DbStyle() != null && fieldsMapping.getCamelCase2DbStyle()) {
			// 驼峰 转换 数据库风格
			Map<String, Object> dbStyle = new HashMap<>();

			for (String key : _params.keySet())
				dbStyle.put(JdbcUtil.changeFieldToColumnName(key), _params.get(key));

			_params.clear(); // 提早清除
			_params = dbStyle;
		}

		Map<String, Object> params;
		String sql = node.getSql();

		if (!_params.containsKey(fieldsMapping != null ? fieldsMapping.getId() : "id"))
			LOGGER.info("没有 id 字段，请检查");

		if (isDynamicSQL(sql)) {// 是否包含 mybatis 脚本控制标签，有的话特殊处理
			sql = "<script>" + sql + "</script>";
			params = new HashMap<>();
			params.put("params", _params);
		} else
			params = _params;

		ctx.setSql(sql);
		ctx.setSqlParam(params);

		LOGGER.info("修改实体:" + params);

		try (SqlSession session = MSUtils.getMyBatisSession(node.getDataSource())) {
			int effectedRow = new SqlMapper(session).update(sql, params);
			// 有时没 update 到数据，却返回 1
			session.commit();
			boolean isOK = effectedRow > 0;

			if (hasPlugins) {
				params.put("isOk", isOK); // 添加一个是否修改成功的状态

				for (IPlugin plugin : plugins)
					plugin.after(CRUD.UPDATE, ctx, params);
			}

			return isOK;
		}
	}

	/**
	 * 删除实体/批量删除
	 * 
	 * @param ctx
	 * @param plugins
	 * @return
	 */
	public static boolean delete(ServiceContext ctx, List<IPlugin> plugins) {
		DataServiceDml node = ctx.getNode();
		Map<String, Object> params = ctx.getRequestParams();
		String sql = node.getSql();
		ctx.setSql(sql);
		ctx.setSqlParam(params);

		boolean hasPlugins = before(CRUD.DELETE, ctx, plugins);
		boolean result = false;

		if (node.isPhysicallyDelete()) {
			if (params.containsKey("ids")) {// 物理删除
				String[] arr = params.get("ids").toString().split(",");

				for (int i = 0; i < arr.length; i++)
					arr[i] = "'" + arr[i] + "'";

				String _sql = sql.replaceAll("=.*$", " IN (" + String.join(",", arr) + ")");
				result = delete(node.getDataSource(), _sql, null);
			} else
				result = delete(node.getDataSource(), sql, params);
		} else { // 逻辑删除
			try (SqlSession session = MSUtils.getMyBatisSession(node.getDataSource())) {
				int effectedRow = new SqlMapper(session).update(sql, params);
				session.commit();
				result = effectedRow > 0;
			}
		}

		if (hasPlugins)
			for (IPlugin plugin : plugins) {
				plugin.after(CRUD.DELETE, ctx, params);
			}

		return result;
	}

	/**
	 * 物理删除一笔记录
	 *
	 * @param ds
	 * @param sql
	 * @param params
	 * @return true 表示删除成功
	 */
	private static boolean delete(DataSource ds, String sql, Map<String, Object> params) {
		try (SqlSession session = MSUtils.getMyBatisSession(ds)) {
			int rows = new SqlMapper(session).delete(sql, params);
			session.commit();

			return rows >= 1;
		}
	}

	/**
	 * 返回具体的异常信息
	 * 
	 * @param ctx
	 * @return
	 */
	private static String getErrMsg(ServiceContext ctx) {
		return ctx.getErrMsg() != null ? ctx.getErrMsg() : "插件中止执行";
	}

	/**
	 * 是否包含 mybatis 脚本控制标签，有的话特殊处理
	 * <p>
	 * https://mybatis.org/mybatis-3/dynamic-sql.html
	 *
	 * @param sql
	 * @return
	 */
	static boolean isDynamicSQL(String sql) {
		return sql.contains("</foreach>") || sql.contains("<if test") || sql.contains("<choose>") || sql.contains("<set>");
	}
}
