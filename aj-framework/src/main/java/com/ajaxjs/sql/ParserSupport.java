package com.ajaxjs.sql;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.CCJSqlParserVisitor;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

public class ParserSupport {
    private static final CountSqlParser countSqlParser = new CountSqlParser();

    /**
     * 解析SELECT SQL语句,解析失败或非SELECT语句则抛出异常
     *
     * @param sql
     * @return
     */
    public static Select parseSelect(String sql) {
        Statement stmt = null;

        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

//		checkArgument(stmt instanceof Select, "%s is not  SELECT statment", sql);
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        // 暂时只支持简单的SELECT xxxx FROM ....语句不支持复杂语句如WITH
//		checkArgument(selectBody instanceof PlainSelect, "ONLY SUPPORT plain select statement %s", sql);

        return (Select) stmt;
    }

    /**
     * 解析SELECT SQL语句,解析失败或非SELECT语句则
     *
     * @param sql
     * @return
     */
    public static Select parseSelectUnchecked(String sql) {
        try {
            return parseSelect(sql);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成 count 查询 SQL,如果{@code select}为空则返回{@code null}
     *
     * @param select
     * @param countColumn 列名，默认 0
     * @return
     */
    public static String countSql(Select select, String countColumn) {
        if (null != select) {
            SelectBody selectBody = select.getSelectBody();

            /**
             * count查询时,忽略SQL语句中的分页语句
             */
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                // 删除OFFSET设置
                plainSelect.setOffset(null);
                // 删除LIMIT设置
                plainSelect.setLimit(null);
            }

            return countSqlParser.getSmartCountSql(select, StringUtils.hasText(countColumn) ? countColumn : "0");
        }

        return null;
    }

    /**
     * 生成 count 查询 SQL,如果{@code sql}为空或不是SELECT语句不能生成count语句则返回{@code null}
     *
     * @param sql
     * @param countColumn 列名，默认 0
     * @return
     */
    public static String countSql(String sql, String countColumn) {
        Select select;

        if (StringUtils.hasText(sql) && null != (select = parseSelectUnchecked(sql)))
            return countSql(select, countColumn);

        return null;
    }

    /**
     * 实现SQL语句解析,解析成功则返回解析后的{@link Statement}，
     * 并通过{@code visitor}参数提供基于AST(抽象语法树)的遍历所有节点的能力。
     *
     * @param sql                 SQL语句
     * @param visitor             遍历所有节点的{@link SimpleNodeVisitor}接口实例，为{@code null}忽略
     * @param sqlSyntaxNormalizer SQL语句分析转换器，为{@code null}忽略
     * @throws JSQLParserException 输入的SQL语句有语法错误
     * @see #parse0(String, CCJSqlParserVisitor, SqlSyntaxNormalizer)
     */
    public static Statement parse(String sql, CCJSqlParserVisitor visitor, SqlSyntaxNormalizer sqlSyntaxNormalizer) throws JSQLParserException {
        return parse0(sql, visitor, sqlSyntaxNormalizer).statement;
    }

    /**
     * 参照{@link CCJSqlParserUtil#parseAST(String)}和{@link CCJSqlParserUtil#parse(String)}实现SQL语句解析,
     * 解析成功则返回解析后的{@link SqlParserInfo}对象，
     * 并通过{@code visitor}参数提供基于AST(抽象语法树)的遍历所有节点的能力。
     *
     * @param sql               SQL语句
     * @param visitor           遍历所有节点的{@link SimpleNodeVisitor}接口实例，为{@code null}忽略
     * @param sqlSyntaxAnalyzer SQL语句分析转换器，为{@code null}忽略
     * @throws JSQLParserException 输入的SQL语句有语法错误
     * @see net.sf.jsqlparser.parser.Node#jjtAccept(SimpleNodeVisitor, Object)
     */
    public static SqlParserInfo parse0(String sql, CCJSqlParserVisitor visitor, SqlSyntaxNormalizer sqlSyntaxAnalyzer) throws JSQLParserException {
//		checkArgument(null != sql, "sql is null");
        boolean allowComplexParsing = CCJSqlParserUtil.getNestingDepth(sql) <= CCJSqlParserUtil.ALLOWED_NESTING_DEPTH;

        CCJSqlParser parser = CCJSqlParserUtil.newParser(sql).withAllowComplexParsing(allowComplexParsing);
        Statement stmt;

        try {
            stmt = parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }

        if (null != visitor)
            parser.getASTRoot().jjtAccept(visitor, null);

        if (null != sqlSyntaxAnalyzer)
            stmt.accept(sqlSyntaxAnalyzer.resetChanged());

        return new SqlParserInfo(stmt.toString(), stmt, (SimpleNode) parser.getASTRoot());
    }

    /**
     * 如果{@link Column}没有定义table,且字段名为true/false(不区分大小写)则视为布尔常量
     *
     * @param column
     */
    public static boolean isBoolean(Column column) {
        return null != column && null == column.getTable()
                && Pattern.compile("(true|false)", Pattern.CASE_INSENSITIVE).matcher(column.getColumnName()).matches();
    }

    public static class SqlParserInfo {
        final String nativeSql;
        public final Statement statement;
        public final SimpleNode simpleNode;

        SqlParserInfo(String nativeSql, Statement statement, SimpleNode simpleNode) {
            this.nativeSql = nativeSql;
            this.statement = statement;
            this.simpleNode = simpleNode;
        }
    }

}
