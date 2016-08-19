/**
 * Copyright 2015 Frank Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.framework.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.framework.model.Query;
import com.ajaxjs.framework.model.Query.*;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;

/**
 * 动态的 SQL，定义于此。静态的 SQL，请参见 SQLProvider.java。
 * MyBatis SQL 拼凑类十分好用，详细用法参见官方文档：http://www.mybatis.org/mybatis-3/dynamic-sql.html
 * @author frank
 *
 */
public class DynamicSqlProvider {
	private static final LogHelper LOGGER = LogHelper.getLog(DynamicSqlProvider.class);

	/**
	 * 返回查询总数的 SQL
	 * 
	 * @param parames
	 *            MyBatis 参数
	 * @return SQL 语句
	 */
	public String pageCount(final Map<String, Object> parames) {
		String sql = new SQL() {
			{
				SELECT("COUNT(*)");
				FROM(parames.get("tablename").toString());
				if (parames.containsKey("query")) {
					addWhere(this, (Query) parames.get("query"));
				}
			}
		}.toString();
		
		LOGGER.info("pageCount-------------->" + sql);
		
		return sql;
	}

	/**
	 * 返回查询列表的 SQL
	 * 
	 * @param parames
	 *            MyBatis 参数
	 * @return SQL 语句
	 */
	public String page(final Map<String, Object> parames) {
		final int start = (int) parames.get("start"), limit = (int) parames.get("limit");
		final String tablename = parames.get("tablename").toString();
 
		String sql = new SQL() {
			{
				SELECT(getFileds(parames));
				FROM(tablename);
				
				if (parames.containsKey("query")) {
					Query query = (Query) parames.get("query");
					addWhere(this, query);
					
					if (query.getOrder() != null) {
						Order order = query.getOrder();
						for (String key : order.keySet()) {
							if(!StringUtil.isEmptyString(order.get(key)))
								ORDER_BY(key + " " + order.get(key));
						}
					}
				}
				
				if (!parames.containsKey("query") || (parames.containsKey("query") && ((Query) parames.get("query")).getOrder() == null)) {
					ORDER_BY("id DESC"); // 默认排序
				}
			}


		}.toString() + " LIMIT " + start + ", " + limit;
		
		LOGGER.info("page sql-------------->" + sql);
		
		return sql;
	}
	
	private String getFileds(Map<String, Object> parames) {
		String addStr = "*";
		
		if (parames.containsKey("query") ) {
			Query query = (Query) parames.get("query");
			if(query.getDb_field_mapping() != null && query.getDb_field_mapping().size() > 0) {
				String [] pairs = new String[query.getDb_field_mapping().size()];
				
				int i = 0;
				for (String key : query.getDb_field_mapping().keySet())
					pairs[i++] = query.getDb_field_mapping().get(key) + " AS " + key;
				
				addStr += " ," + StringUtil.stringJoin(pairs, ",");
			}
		}
		
		return addStr;
	}

	/**
	 * 返回新建的 SQL
	 * 
	 * @param model
	 *            实体
	 * @return SQL 语句
	 */
	public String create(final BaseModel model) {
		SQL sql = new SQL();
		sql.INSERT_INTO(getTableName(model));
		addFieldValues(sql, model, model.getClass().getMethods(), false);

		LOGGER.info("create sql-------------->" + sql);
		
		return sql.toString();
	}
	
	/**
	 * 修改
	 * 
	 * @param model
	 *            实体
	 * @return SQL 语句
	 */
	public String update(final BaseModel model) {
		final Method[] methods = model.getClass().getMethods();
		
		LOGGER.info("更新记录{0}！", model.getName());
		
		// 反射获取字段
		String sql = new SQL() {
			{
				UPDATE(getTableName(model));
				addFieldValues(this, model, methods, true);
				WHERE("id = #{id}");
			}
		}.toString();
		
		LOGGER.info("update sql-------------->" + sql);

		return sql;
	}
	
	public static String createTable(String tableName) {
		String sql = "CREATE TABLE `%s` (`id` INTEGER PRIMARY KEY  NOT NULL  DEFAULT (null),`"
				+ "`name` VARCHAR(255), `uid` VARCHAR(36)"
				+ "`createDate` DATETIME,`updateDate` DATETIME,"
				+ "`content` TEXT, `intro` TEXT,"
				+ "`catalog` INTEGER, `cover` VARCHAR(255),"
				+ "`isOnline` BOOL DEFAULT (null)";
		
		return String.format(sql, tableName);
	}
	
	/**
	 * 
	 * @param sql
	 *            动态 SQL 实例
	 * @param model
	 *            实体
	 * @param methods
	 *            反射获取字段
	 * @param isSet
	 *            true=Update/false=Create
	 */
	private void addFieldValues(SQL sql, BaseModel model, Method[] methods, boolean isSet) {
		for (Method method : methods) {	// 反射获取字段
			String methodName = method.getName();
			
			Map<String, String> fieldMap = model.getService().getHidden_db_field_mapping();
			
			if (isOk_field(methodName)) {
				try {
					if (method.invoke(model) != null) {
						methodName = getFieldName(methodName);
						String pojoName = methodName;
						// 字段映射
						if (fieldMap.size() > 0 && fieldMap.containsKey(methodName)) {
							pojoName = methodName;
							methodName = fieldMap.get(methodName);
						}
						
						if(isSet)
							sql.SET(methodName + "= #{" + pojoName + "}");
						else
							sql.VALUES(methodName, "#{" + pojoName + "}");
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.warning(e);
				}
			}
		}
	}

	/**
	 * 如果有 mappingTableName 则返回；如果没有则返回 tableName。
	 * 
	 * @param model
	 *            实体模型
	 * @return
	 */
	private String getTableName(BaseModel model) {
		return model.getService().getMappingTableName() == null ? 
			   model.getService().getTableName() : model.getService().getMappingTableName();
	} 
	
	/**
	 * 添加 WHERE 子语句
	 * 
	 * @param sql
	 *            动态 SQL 实例
	 * @param query
	 *            Query 查询对象
	 */
	private static void addWhere(SQL sql, Query query) {
		Map<String, String> map;
		if (query.getFilter() != null) {
			Filter filter = query.getFilter();
			
			map = (Map<String, String>)filter;
			
			for (String key : map.keySet()) {
				if(!StringUtil.isEmptyString(map.get(key))){
					
					if(filter.isCustomOpeartor()) {
						sql.WHERE(key + map.get(key));
					} else {
						sql.WHERE(key + " = " + map.get(key));
					}
				}
			}
		}
		if (query.getSearch() != null) {
			map = query.getSearch();
			for (String key : map.keySet()) {
				if(!StringUtil.isEmptyString(map.get(key)))
					sql.WHERE(key + " LIKE '%" + map.get(key) + "%'");
			}
		}
		if (query.getMatch() != null) {
			map = query.getMatch();
			for (String key : map.keySet()) {
				if(!StringUtil.isEmptyString(map.get(key)))
					sql.WHERE(key + " LIKE '" + map.get(key) + "'");
			}
		}
	}
	
	/**
	 * 不是 pojo 所有的字段都要，这里判断
	 * 
	 * @param methodName
	 *            方法名称
	 * @return
	 */
	private static boolean isOk_field(String methodName) {
		return (methodName.startsWith("get") || methodName.startsWith("is")) && !"getId".equals(methodName) && !"getClass".equals(methodName) && !"getService".equals(methodName);
	}

	/**
	 * 把 getter 的 getXX() 转换为 xX 的字段名
	 * 
	 * @param methodName
	 *            方法名称
	 * @return
	 */
	public static String getFieldName(String methodName) {
		methodName = methodName.replace("get", "");
		methodName = Character.toString(methodName.charAt(0)).toLowerCase() + methodName.substring(1);
		return methodName;
	}

	/**
	 * 通过子查询获得图片列表 图片表是根据实体 uid 获得其所有的图片，形成列表返回 这里返回的 SQL Concat 结果后的字符串，用 , 分隔开
	 * UI 层需要 split 字符串
	 * @deprecated
	 * @param tablename
	 * @return
	 */
	public String getImgList(String tablename) {
		String subQuerySql = " (SELECT group_concat(fileName) FROM img WHERE img.parentId = %s.uid) AS imgs";
		return String.format(subQuerySql, tablename);
	}
}
