package com.ajaxjs.framework.sql;

import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;

/**
 * SQL 增强器
 *
 * @author Frank Cheung sp42@qq.com
 */
public class SqleEnhancer {
	/**
	 * 分页结果
	 */
	static class PageSql {
		/**
		 * 统计总数的 SQL
		 */
		public String countTotal;

		/**
		 * 分页 SQL
		 */
		public String pageSql;
	}

	/**
	 * 分页
	 *
	 * @param sql   普通 SELECT 语句
	 * @param start
	 * @param limit
	 * @return
	 */
	public static PageSql page(String sql, int start, int limit) {
		PageSql result = new PageSql();

		Select selectStatement;

		try {
			selectStatement = (Select) CCJSqlParserUtil.parse(sql);
		} catch (JSQLParserException e) {
			throw new RuntimeException("Error parsing SQL statement", e);
		}

		SelectBody selectBody = selectStatement.getSelectBody();

		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) selectBody;

			// 设置分页语句
			Limit limitObj = new Limit();
			limitObj.setRowCount(new LongValue(limit));
			limitObj.setOffset(new LongValue(start));
			plainSelect.setLimit(limitObj);

			result.pageSql = selectStatement.toString();
//			System.out.println(result.pageSql);

			// 移除 排序 语句
			if (sql.toUpperCase().contains("ORDER BY")) {
				List<OrderByElement> orderBy = plainSelect.getOrderByElements();

				if (orderBy != null)
					plainSelect.setOrderByElements(null);
			}

			// 创建一个 count 函数的表达式
			Function countFunc = new Function();
			countFunc.setName("COUNT");
			countFunc.setParameters(new ExpressionList(new Expression[] { new AllColumns() }));

			// 替换所有的 Select Item
			List<SelectItem> selectItems = plainSelect.getSelectItems();
			selectItems.clear();
			selectItems.add(new SelectExpressionItem(countFunc));
		} else if (selectBody instanceof SetOperationList) {
			SetOperationList setOperationList = (SetOperationList) selectBody;
			List<SelectBody> selectBodies = setOperationList.getSelects();

			/*
			 * 我们还考虑了 SQL 查询语句中使用了 SetOperationList 的情况，这时需要对每个 SELECT 子查询都进行分页，同时修改 FROM
			 * 部分的表名，以避免语法错误。
			 */
			selectBodies.forEach(selectItem -> {
				if (selectItem instanceof PlainSelect) {
					PlainSelect plainSelect = (PlainSelect) selectItem;
					Limit limitObj = new Limit();
					limitObj.setRowCount(new LongValue(limit));
					limitObj.setOffset(new LongValue(start));
					plainSelect.setLimit(limitObj);

					if (plainSelect.getFromItem() != null) {
						// modify the original table by adding an alias
//						plainSelect.getFromItem().setAlias(new Table("original_table_alias"));
					}
				}
			});
		}

		result.countTotal = selectStatement.toString();
//		System.out.println(result.countTotal);

		return result;
	}

}
