package com.ajaxjs.jdbc.sqlbuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis SqlBuilder
 * 
 * @author Clinton Begin
 * @author Jeff Butler
 * @author Adam Gent
 */
public class SQLStatement {

	public enum StatementType {
		DELETE, INSERT, SELECT, UPDATE
	}

	StatementType statementType;
	List<String> sets = new ArrayList<>();
	List<String> select = new ArrayList<>();
	List<String> tables = new ArrayList<>();
	List<String> join = new ArrayList<>();
	List<String> innerJoin = new ArrayList<>();
	List<String> outerJoin = new ArrayList<>();
	List<String> leftOuterJoin = new ArrayList<>();
	List<String> rightOuterJoin = new ArrayList<>();
	List<String> where = new ArrayList<>();
	List<String> having = new ArrayList<>();
	List<String> groupBy = new ArrayList<>();
	List<String> orderBy = new ArrayList<>();
	List<String> lastList = new ArrayList<>();
	List<String> columns = new ArrayList<>();
	List<String> values = new ArrayList<>();
	boolean distinct;

	public SQLStatement() {
		// Prevent Synthetic Access
	}

	/**
	 * SQL 子句
	 * 
	 * @param builder
	 * @param keyword
	 * @param parts
	 * @param open
	 * @param close
	 * @param conjunction
	 */
	private void sqlClause(SafeAppendable builder, String keyword, List<String> parts, String open, String close, String conjunction) {
		if (!parts.isEmpty()) {
			if (!builder.isEmpty())
				builder.append("\n");// 非空换行

			builder.append(keyword);
			builder.append(" ");
			builder.append(open);
			String last = "________";

			for (int i = 0, n = parts.size(); i < n; i++) {
				String part = parts.get(i);

				if (i > 0 && !part.equals(SqlBuilder.AND) && !part.equals(SqlBuilder.OR) && !last.equals(SqlBuilder.AND) && !last.equals(SqlBuilder.OR)) {
					builder.append(conjunction);
				}

				builder.append(part);
				last = part;
			}

			builder.append(close);
		}
	}

	private String selectSQL(SafeAppendable builder) {
		if (distinct) {
			sqlClause(builder, "SELECT DISTINCT", select, "", "", ", ");
		} else {
			sqlClause(builder, "SELECT", select, "", "", ", ");
		}

		sqlClause(builder, "FROM", tables, "", "", ", ");
		sqlClause(builder, "JOIN", join, "", "", "\nJOIN ");
		sqlClause(builder, "INNER JOIN", innerJoin, "", "", "\nINNER JOIN ");
		sqlClause(builder, "OUTER JOIN", outerJoin, "", "", "\nOUTER JOIN ");
		sqlClause(builder, "LEFT OUTER JOIN", leftOuterJoin, "", "", "\nLEFT OUTER JOIN ");
		sqlClause(builder, "RIGHT OUTER JOIN", rightOuterJoin, "", "", "\nRIGHT OUTER JOIN ");
		sqlClause(builder, "WHERE", where, "(", ")", " AND ");
		sqlClause(builder, "GROUP BY", groupBy, "", "", ", ");
		sqlClause(builder, "HAVING", having, "(", ")", " AND ");
		sqlClause(builder, "ORDER BY", orderBy, "", "", ", ");

		return builder.toString();
	}

	private String insertSQL(SafeAppendable builder) {
		sqlClause(builder, "INSERT INTO", tables, "", "", "");
		sqlClause(builder, "", columns, "(", ")", ", ");
		sqlClause(builder, "VALUES", values, "(", ")", ", ");

		return builder.toString();
	}

	private String deleteSQL(SafeAppendable builder) {
		sqlClause(builder, "DELETE FROM", tables, "", "", "");
		sqlClause(builder, "WHERE", where, "(", ")", " AND ");

		return builder.toString();
	}

	private String updateSQL(SafeAppendable builder) {
		sqlClause(builder, "UPDATE", tables, "", "", "");
		sqlClause(builder, "SET", sets, "", "", ", ");
		sqlClause(builder, "WHERE", where, "(", ")", " AND ");

		return builder.toString();
	}

	public String sql(Appendable a) {
		SafeAppendable builder = new SafeAppendable(a);
		if (statementType == null)
			return null;

		String answer;

		switch (statementType) {
		case DELETE:
			answer = deleteSQL(builder);
			break;

		case INSERT:
			answer = insertSQL(builder);
			break;

		case SELECT:
			answer = selectSQL(builder);
			break;

		case UPDATE:
			answer = updateSQL(builder);
			break;

		default:
			answer = null;
		}

		return answer;
	}

	public static class SafeAppendable {
		private final Appendable a;
		private boolean empty = true;

		public SafeAppendable(Appendable a) {
			super();
			this.a = a;
		}

		public SafeAppendable append(CharSequence s) {
			try {
				if (empty && s.length() > 0)
					empty = false;

				a.append(s);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			return this;
		}

		public boolean isEmpty() {
			return empty;
		}

	}
}