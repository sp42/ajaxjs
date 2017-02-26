package com.ajaxjs.jdbc.sqlbuilder;

import java.util.ArrayList;
import java.util.List;

public class SQLStatement {

	public enum StatementType {
		DELETE, INSERT, SELECT, UPDATE
	}

	StatementType statementType;
	List<String> sets = new ArrayList<String>();
	List<String> select = new ArrayList<String>();
	List<String> tables = new ArrayList<String>();
	List<String> join = new ArrayList<String>();
	List<String> innerJoin = new ArrayList<String>();
	List<String> outerJoin = new ArrayList<String>();
	List<String> leftOuterJoin = new ArrayList<String>();
	List<String> rightOuterJoin = new ArrayList<String>();
	List<String> where = new ArrayList<String>();
	List<String> having = new ArrayList<String>();
	List<String> groupBy = new ArrayList<String>();
	List<String> orderBy = new ArrayList<String>();
	List<String> lastList = new ArrayList<String>();
	List<String> columns = new ArrayList<String>();
	List<String> values = new ArrayList<String>();
	boolean distinct;

	public SQLStatement() {
		// Prevent Synthetic Access
	}

	/**
	 * SQL 子句 
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

	/**
	 * 
	 * @param builder
	 * @return
	 */
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
}