package com.ajaxjs.orm.thirdparty;

/**
 * MyBatis SqlBuilder
 * 拼凑类十分好用，详细用法参见官方文档：http://www.mybatis.org/mybatis-3/statement-builders.html
 * 我复制了在这里用
 * 
 * @author Clinton Begin
 * @author Jeff Butler
 * @author Adam Gent
 */
public class SqlBuilder {
	public static final String AND = ") \nAND (";
	public static final String OR = ") \nOR (";

	private SQLStatement sql = new SQLStatement();

	public SqlBuilder UPDATE(String table) {
		sql().statementType = SQLStatement.StatementType.UPDATE;
		sql().tables.add(table);
		return this;
	}

	public SqlBuilder SET(String sets) {
		sql().sets.add(sets);
		return this;
	}

	public SqlBuilder INSERT_INTO(String tableName) {
		sql().statementType = SQLStatement.StatementType.INSERT;
		sql().tables.add(tableName);
		return this;
	}

	public SqlBuilder VALUES(String columns, String values) {
		sql().columns.add(columns);
		sql().values.add(values);
		return this;
	}

	public SqlBuilder SELECT(String columns) {
		sql().statementType = SQLStatement.StatementType.SELECT;
		sql().select.add(columns);
		return this;
	}

	public SqlBuilder SELECT_DISTINCT(String columns) {
		sql().distinct = true;
		SELECT(columns);
		return this;
	}

	public SqlBuilder DELETE_FROM(String table) {
		sql().statementType = SQLStatement.StatementType.DELETE;
		sql().tables.add(table);
		return this;
	}

	public SqlBuilder FROM(String table) {
		sql().tables.add(table);
		return this;
	}

	public SqlBuilder JOIN(String join) {
		sql().join.add(join);
		return this;
	}

	public SqlBuilder INNER_JOIN(String join) {
		sql().innerJoin.add(join);
		return this;
	}

	public SqlBuilder LEFT_OUTER_JOIN(String join) {
		sql().leftOuterJoin.add(join);
		return this;
	}

	public SqlBuilder RIGHT_OUTER_JOIN(String join) {
		sql().rightOuterJoin.add(join);
		return this;
	}

	public SqlBuilder OUTER_JOIN(String join) {
		sql().outerJoin.add(join);
		return this;
	}

	public SqlBuilder WHERE(String conditions) {
		sql().where.add(conditions);
		sql().lastList = sql().where;
		return this;
	}

	public SqlBuilder OR() {
		sql().lastList.add(OR);
		return this;
	}

	public SqlBuilder AND() {
		sql().lastList.add(AND);
		return this;
	}

	public SqlBuilder GROUP_BY(String columns) {
		sql().groupBy.add(columns);
		return this;
	}

	public SqlBuilder HAVING(String conditions) {
		sql().having.add(conditions);
		sql().lastList = sql().having;
		return this;
	}

	public SqlBuilder ORDER_BY(String columns) {
		sql().orderBy.add(columns);
		return this;
	}

	private SQLStatement sql() {
		return sql;
	}

	public <A extends Appendable> A usingAppender(A a) {
		sql().sql(a);
		return a;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sql().sql(sb);
		return sb.toString();
	}
}