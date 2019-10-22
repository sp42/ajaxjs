/**
 * Copyright sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.ajaxjs.framework.model.DaoInfo;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.util.CommonUtil;

/**
 * Data Access Object 负责对数据库的增删改查工作最后的工作。 框架中一般无须写出实现，提供接口即可。 通过 Java
 * 代理自动实现接口的实例 必须是接口原类型，而不是接口其父类，也就是不支持多态
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class Repository extends RepositoryReadOnly {
	/**
	 * 执行时的调用。不管执行哪个方法都会调用该方法。
	 * 
	 * @param proxy		代理对象
	 * @param method	DAO 方法对象
	 * @param args		DAO 方法的参数
	 * @return DAO 方法执行的结果
	 * @throws DaoException DAO 异常
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws DaoException {
		if (init(method))
			return "This is a AJAXJS DAO.";

		if (isRead(method))
			return select(method, args);

		// if (args != null && args[0] != null) {
		// Class<?> entryType = args[0].getClass();// 实体类型由参数决定，因为 写入方法通常后面跟着的就是实体
		// } else
		// throw new DaoException("DAO 接口方法:" + method + " 签名缺少实体参数！");

		if (isCreate(method))
			return insert(method, args);

		if (isUpdate(method))
			return update(method, args);

		if (isDelete(method))
			return delete(method, args);

		throw new DaoException("没有任何 DAO CRUD 的注解。你继承 IDAO 接口的子接口中，可能没有覆盖 IDAO 的方法" + method);
	}

	/**
	 * 
	 * @param getSql
	 * @param getTableName
	 * @param args
	 * @param writeSql
	 * @param writeBean
	 * @return
	 * @throws DaoException 
	 */
	private <T> T getFn(Supplier<String> getSql, Supplier<String> getTableName, Object[] args, Method method,
			Function<DaoInfo, T> writeSql, Function<DaoInfo, T> writeBean) throws DaoException {
		Object bean = args[0];

		DaoInfo daoInfo = new DaoInfo();
		daoInfo.sql = getSql.get();
		// 表名可以通过注解获取（类），也可以直接 insert.tableName() 获取
		daoInfo.tableName = CommonUtil.isEmptyString(getTableName.get()) ? getTableName() : getTableName.get();
		daoInfo.isMap = bean instanceof Map;
		daoInfo.bean = bean;

		if (!CommonUtil.isEmptyString(daoInfo.sql)) { /* 以 sql 方式更新 */
			daoInfo.sql = handleSql(daoInfo.sql, method, args).sql;

			return writeSql.apply(daoInfo);
		} else if (CommonUtil.isEmptyString(daoInfo.sql) && bean != null) // 以 bean 方式删除
			return writeBean.apply(daoInfo);

		throw new DaoException("程序错误");
	}

	@SuppressWarnings("unchecked")
	private Function<DaoInfo, Serializable> createEntity = daoInfo -> daoInfo.isMap
			? createMap(conn, (Map<String, Object>) daoInfo.bean, daoInfo.tableName)
			: createBean(conn, daoInfo.bean, daoInfo.tableName);

	@SuppressWarnings("unchecked")
	private Function<DaoInfo, Integer> updateEntity = daoInfo -> daoInfo.isMap
			? updateMap(conn, (Map<String, Object>) daoInfo.bean, daoInfo.tableName)
			: updateBean(conn, daoInfo.bean, daoInfo.tableName);

	/**
	 * 新增动作
	 * 
	 * @param method		DAO 方法对象
	 * @param args			DAO 方法的参数
	 * @return 自增 id
	 * @throws DaoException
	 */
	private <R> Serializable insert(Method method, Object[] args) throws DaoException {
		Class<?> returnType = getReturnType(method);
		Insert insert = method.getAnnotation(Insert.class);
		Function<DaoInfo, Serializable> writeSql = daoInfo -> create(conn, daoInfo.sql, args);
		// INSERT 返回新建的 id
		Serializable id = getFn(insert::value, insert::tableName, args, method, writeSql, createEntity);

		if ((returnType == Integer.class || returnType == int.class) && id.getClass() == Long.class) {
			return Integer.parseInt("" + id);
		} else if ((returnType == Long.class || returnType == long.class) && id.getClass() == Integer.class) {
			return new Long((Integer) id);
		} else {
			return id;
		}
	}

	/**
	 * 更新动作
	 * 
	 * @param method 	DAO 方法对象
	 * @param args 		DAO 方法的参数
	 * @return 影响的行数
	 * @throws DaoException 
	 */
	private int update(Method method, Object[] args) throws DaoException {
		Update update = method.getAnnotation(Update.class);
		Function<DaoInfo, Integer> writeSql = daoInfo -> update(conn, daoInfo.sql, args);

		return getFn(update::value, update::tableName, args, method, writeSql, updateEntity);
	}

	/**
	 * 删除动作
	 * 
	 * @param method		DAO 方法对象
	 * @param args			DAO 方法的参数
	 * @return 是否删除成功
	 * @throws DaoException 
	 */
	private Boolean delete(Method method, Object[] args) throws DaoException {
		Delete delete = method.getAnnotation(Delete.class);
		Supplier<String> getSql = isSqlite(delete.sqliteValue(), conn) ? delete::sqliteValue : delete::value;

		/* DELETESQL 也是用 update方法 */
		return getFn(getSql, delete::tableName, args, method, daoInfo -> update(conn, daoInfo.sql, args) >= 1,
				daoInfo -> delete(conn, daoInfo.bean, daoInfo.tableName));
	}
}
