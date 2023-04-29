package com.ajaxjs.data_service.mybatis;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.mapping.ResultMap.Builder;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * MyBatis 工具类
 */
public class MSUtils {
	/**
	 * MyBatis 配置
	 */
	private Configuration configuration;

	private LanguageDriver languageDriver;

	/**
	 * @param cfg MyBatis 配置
	 */
	public MSUtils(Configuration cfg) {
		this.configuration = cfg;
		languageDriver = cfg.getDefaultScriptingLanguageInstance();
	}

	/**
	 * 根据数据源创建 Mybatis 会话
	 *
	 * @param ds 数据源
	 * @return SQL 会话
	 */
	public static SqlSession getMyBatisSession(DataSource ds) {
		Objects.requireNonNull(ds, "数据源未准备好");

		Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
		Configuration configuration = new Configuration(environment);
		configuration.setUseGeneratedKeys(true);
		configuration.setCallSettersOnNulls(true);// 设置为 true 为 null 字段也会查询出来

//		if (Version.isDebug)
		configuration.addInterceptor(new MybatisInterceptor()); // 打印 SQL

		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);

		return factory.openSession();
	}

	/**
	 * 创建 MSID
	 *
	 * @param sql 执行的 sql
	 * @param sql 执行的 sqlCommandType
	 * @return
	 */
	private String newMsId(String sql, SqlCommandType sqlCommandType) {
		StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
		msIdBuilder.append(".").append(sql.hashCode());

		return msIdBuilder.toString();
	}

	/**
	 * 是否已经存在该 ID
	 *
	 * @param msId
	 * @return
	 */
	private boolean hasMappedStatement(String msId) {
		return configuration.hasStatement(msId, false);
	}

	/**
	 * 创建一个查询的 MS
	 *
	 * @param msId
	 * @param source     执行的 sqlSource
	 * @param resultType 返回的结果类型
	 */
	private void newSelectMappedStatement(String msId, SqlSource source, final Class<?> resultType) {
		MappedStatement ms = new MappedStatement.Builder(configuration, msId, source, SqlCommandType.SELECT).resultMaps(new ArrayList<ResultMap>() {
			private static final long serialVersionUID = 4647394718738200770L;

			{
				add(new Builder(configuration, "defaultResultMap", resultType, new ArrayList<ResultMapping>(0)).build());
			}
		}).build();

		configuration.addMappedStatement(ms);// 缓存
	}

	/**
	 * 创建一个简单的 MS
	 *
	 * @param msId
	 * @param source 执行的 sqlSource
	 * @param type   执行的 sqlCommandType
	 */
	private void newUpdateMappedStatement(String msId, SqlSource source, SqlCommandType type) {
		MappedStatement ms = new MappedStatement.Builder(configuration, msId, source, type).resultMaps(new ArrayList<ResultMap>() {
			private static final long serialVersionUID = 9060317759673729016L;

			{
				ArrayList<ResultMapping> arr = new ArrayList<>(0);
				Builder builder = new Builder(configuration, "defaultResultMap", int.class, arr);
				add(builder.build());
			}
		}).keyColumn("id").keyProperty("id").build();

		configuration.addMappedStatement(ms);// 缓存
	}

	String select(String sql) {
		String msId = newMsId(sql, SqlCommandType.SELECT);
		if (hasMappedStatement(msId))
			return msId;

		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
		newSelectMappedStatement(msId, sqlSource, Map.class);

		return msId;
	}

	String selectDynamic(String sql, Class<?> parameterType) {
		String msId = newMsId(sql + parameterType, SqlCommandType.SELECT);
		if (hasMappedStatement(msId))
			return msId;

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
		newSelectMappedStatement(msId, sqlSource, Map.class);

		return msId;
	}

	String select(String sql, Class<?> resultType) {
		String msId = newMsId(resultType + sql, SqlCommandType.SELECT);
		if (hasMappedStatement(msId))
			return msId;

		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
		newSelectMappedStatement(msId, sqlSource, resultType);

		return msId;
	}

	String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
		String msId = newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
		if (hasMappedStatement(msId))
			return msId;

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
		newSelectMappedStatement(msId, sqlSource, resultType);

		return msId;
	}

	String insert(String sql) {
		String msId = newMsId(sql, SqlCommandType.INSERT);
		if (hasMappedStatement(msId))
			return msId;

		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
		newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);

		return msId;
	}

	String insertDynamic(String sql, Class<?> parameterType) {
		String msId = newMsId(sql + parameterType, SqlCommandType.INSERT);
		if (hasMappedStatement(msId))
			return msId;

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
		newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);

		return msId;
	}

	String update(String sql) {
		String msId = newMsId(sql, SqlCommandType.UPDATE);
		if (hasMappedStatement(msId))
			return msId;

		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
		newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);

		return msId;
	}

	String updateDynamic(String sql, Class<?> parameterType) {
		String msId = newMsId(sql + parameterType, SqlCommandType.UPDATE);
		if (hasMappedStatement(msId))
			return msId;

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
		newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);

		return msId;
	}

	String delete(String sql) {
		String msId = newMsId(sql, SqlCommandType.DELETE);
		if (hasMappedStatement(msId))
			return msId;

		StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
		newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);

		return msId;
	}

	String deleteDynamic(String sql, Class<?> parameterType) {
		String msId = newMsId(sql + parameterType, SqlCommandType.DELETE);
		if (hasMappedStatement(msId))
			return msId;

		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
		newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);

		return msId;
	}
}