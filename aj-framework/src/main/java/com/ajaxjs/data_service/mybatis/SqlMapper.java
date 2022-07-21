package com.ajaxjs.data_service.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;

/**
 * MyBatis 执行 sql 工具，在写 SQL 的时候建议使用参数形式的可以是${}或#{}
 *
 * 不建议将参数直接拼到字符串中，当大量这么使用的时候由于缓存 MappedStatement 而占用更多的内存
 *
 * @author liuzh https://liuzh.blog.csdn.net/article/details/44193939
 * @since 2015-03-10
 */
public class SqlMapper {
	private final MSUtils msUtils;

	private final SqlSession sqlSession;

	/**
	 * 构造方法，默认缓存MappedStatement
	 *
	 * @param sqlSession
	 */
	public SqlMapper(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
		this.msUtils = new MSUtils(sqlSession.getConfiguration());
	}

	/**
	 * 获取List中最多只有一个的数据
	 *
	 * @param list List结果
	 * @param <T>  泛型类型
	 * @return
	 */
	private <T> T getOne(List<T> list) {
		if (list.size() == 1)
			return list.get(0);
		else if (list.size() > 1)
			throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
		else
			return null;
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql 执行的sql
	 * @return
	 */
	public Map<String, Object> selectOne(String sql) {
		List<Map<String, Object>> list = selectList(sql);

		return getOne(list);
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql   执行的sql
	 * @param value 参数
	 * @return
	 */
	public Map<String, Object> selectOne(String sql, Object value) {
		List<Map<String, Object>> list = selectList(sql, value);

		return getOne(list);
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql        执行的sql
	 * @param resultType 返回的结果类型
	 * @param <T>        泛型类型
	 * @return
	 */
	public <T> T selectOne(String sql, Class<T> resultType) {
		List<T> list = selectList(sql, resultType);

		return getOne(list);
	}

	/**
	 * 查询返回一个结果，多个结果时抛出异常
	 *
	 * @param sql        执行的sql
	 * @param value      参数
	 * @param resultType 返回的结果类型
	 * @param <T>        泛型类型
	 * @return
	 */
	public <T> T selectOne(String sql, Object value, Class<T> resultType) {
		List<T> list = selectList(sql, value, resultType);

		return getOne(list);
	}

	/**
	 * 查询返回List<Map<String, Object>>
	 *
	 * @param sql 执行的sql
	 * @return
	 */
	public List<Map<String, Object>> selectList(String sql) {
		String msId = msUtils.select(sql);

		return sqlSession.selectList(msId);
	}

	/**
	 * 查询返回List<Map<String, Object>>
	 *
	 * @param sql   执行的sql
	 * @param value 参数
	 * @return
	 */
	public List<Map<String, Object>> selectList(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.selectDynamic(sql, parameterType);

		return sqlSession.selectList(msId, value);
	}

	/**
	 * 查询返回指定的结果类型
	 *
	 * @param sql        执行的sql
	 * @param resultType 返回的结果类型
	 * @param <T>        泛型类型
	 * @return
	 */
	public <T> List<T> selectList(String sql, Class<T> resultType) {
		String msId;

		if (resultType == null)
			msId = msUtils.select(sql);
		else
			msId = msUtils.select(sql, resultType);

		return sqlSession.selectList(msId);
	}

	/**
	 * 查询返回指定的结果类型
	 *
	 * @param sql        执行的 sql
	 * @param value      参数
	 * @param resultType 返回的结果类型
	 * @param <T>        泛型类型
	 * @return
	 */
	public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
		String msId;
		Class<?> parameterType = value != null ? value.getClass() : null;

		if (resultType == null)
			msId = msUtils.selectDynamic(sql, parameterType);
		else
			msId = msUtils.selectDynamic(sql, parameterType, resultType);

		return sqlSession.selectList(msId, value);
	}

	/**
	 * 插入数据
	 *
	 * @param sql 执行的sql
	 * @return
	 */
	public int insert(String sql) {
		String msId = msUtils.insert(sql);

		return sqlSession.insert(msId);
	}

	/**
	 * 插入数据
	 *
	 * @param sql   执行的sql
	 * @param value 参数
	 * @return
	 */
	public int insert(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.insertDynamic(sql, parameterType);

		return sqlSession.insert(msId, value);
	}

	/**
	 * 更新数据
	 *
	 * @param sql 执行的sql
	 * @return
	 */
	public int update(String sql) {
		String msId = msUtils.update(sql);

		return sqlSession.update(msId);
	}

	/**
	 * 更新数据
	 *
	 * @param sql   执行的sql
	 * @param value 参数
	 * @return
	 */
	public int update(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.updateDynamic(sql, parameterType);

		return sqlSession.update(msId, value);
	}

	/**
	 * 删除数据
	 *
	 * @param sql 执行的sql
	 * @return
	 */
	public int delete(String sql) {
		String msId = msUtils.delete(sql);

		return sqlSession.delete(msId);
	}

	/**
	 * 删除数据
	 *
	 * @param sql   执行的sql
	 * @param value 参数
	 * @return
	 */
	public int delete(String sql, Object value) {
		Class<?> parameterType = value != null ? value.getClass() : null;
		String msId = msUtils.deleteDynamic(sql, parameterType);

		return sqlSession.delete(msId, value);
	}
}
