package com.ajaxjs.data.util;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.regex.Pattern;

/**
 * 基于 SQL 语法对象的 SQL 注入攻击分析实现
 *
 * @author guyadong
 */
public class SqlInjectionAnalyzer extends TablesNamesFinder {
    /**
     * 危险函数名
     */
    private static final String DANGEROUS_FUNCTIONS = "(sleep|benchmark|extractvalue|updatexml|ST_LatFromGeoHash|ST_LongFromGeoHash|GTID_SUBSET|GTID_SUBTRACT|floor|ST_Pointfromgeohash"
            + "|geometrycollection|multipoint|polygon|multipolygon|linestring|multilinestring)";

//    private static final ThreadLocal<Boolean> disableSubSelect = new ThreadLocal<Boolean>() {
//        @Override
//        protected Boolean initialValue() {
//            return true;
//        }
//    };

    private final ConstAnalyzer constAnalyzer = new ConstAnalyzer();

    public SqlInjectionAnalyzer() {
        super();
        init(true);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        if (binaryExpression instanceof ComparisonOperator) {
            if (isConst(binaryExpression.getLeftExpression()) && isConst(binaryExpression.getRightExpression()))
                /* 禁用恒等式 */
                throw new SecurityException("DISABLE IDENTICAL EQUATION " + binaryExpression);
        }

        super.visitBinaryExpression(binaryExpression);
    }

    @Override
    public void visit(AndExpression andExpression) {
        super.visit(andExpression);
        checkConstExpress(andExpression.getLeftExpression());
        checkConstExpress(andExpression.getRightExpression());
    }

    @Override
    public void visit(OrExpression orExpression) {
        super.visit(orExpression);
        checkConstExpress(orExpression.getLeftExpression());
        checkConstExpress(orExpression.getRightExpression());
    }

    @Override
    public void visit(Function function) {
        if (function.getName().matches(DANGEROUS_FUNCTIONS))
            /* 禁用危险函数 */
            throw new SecurityException("DANGEROUS FUNCTION: " + function.getName());

        super.visit(function);
    }

    @Override
    public void visit(WithItem withItem) {
//        try {
//            /* 允许 WITH 语句中的子查询 */
//            disableSubSelect.set(false);
//            super.visit(withItem);
//        } finally {
//            disableSubSelect.set(true);
//        }
    }

    @Override
    public void visit(SubSelect subSelect) {
//        if (disableSubSelect.get()) // 禁用子查询
//            throw new SecurityException("DISABLE subSelect " + subSelect);
    }

    @Override
    public void visit(Column tableColumn) {
        if (isBoolean(tableColumn))
            throw new SecurityException("DISABLE CONST BOOL " + tableColumn);

        super.visit(tableColumn);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        if (plainSelect.getSelectItems() != null) {
            for (SelectItem item : plainSelect.getSelectItems())
                item.accept(this);
        }

        if (plainSelect.getFromItem() != null)
            plainSelect.getFromItem().accept(this);

        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                join.getRightItem().accept(this);

                for (Expression e : join.getOnExpressions())
                    e.accept(this);
            }
        }

        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(this);
            checkConstExpress(plainSelect.getWhere());
        }

        if (plainSelect.getHaving() != null)
            plainSelect.getHaving().accept(this);

        if (plainSelect.getOracleHierarchical() != null)
            plainSelect.getOracleHierarchical().accept(this);

        if (plainSelect.getOrderByElements() != null) {
            for (OrderByElement orderByElement : plainSelect.getOrderByElements())
                orderByElement.getExpression().accept(this);
        }

        if (plainSelect.getGroupBy() != null) {
            for (Expression expression : plainSelect.getGroupBy().getGroupByExpressionList().getExpressions())
                expression.accept(this);
        }
    }

    private boolean isConst(Expression expression) {
        return constAnalyzer.isConstExpression(expression);
    }

    private void checkConstExpress(Expression expression) {
        if (constAnalyzer.isConstExpression(expression))
            /* 禁用常量表达式 */
            throw new SecurityException("DISABLE CONST EXPRESSION " + expression);
    }

    private static final Pattern BOL = Pattern.compile("(true|false)", Pattern.CASE_INSENSITIVE);

    /**
     * 如果{@link Column}没有定义table,且字段名为true/false(不区分大小写)则视为布尔常量
     */
    public static boolean isBoolean(Column column) {
        return null != column && null == column.getTable() && BOL.matcher(column.getColumnName()).matches();
    }

    private static final SqlInjectionAnalyzer injectionChecker = new SqlInjectionAnalyzer();

    /**
     * SQL 注入攻击分析器
     * 对解析后的SQL对象执行注入攻击分析，有注入攻击的危险则抛出异常，
     * 并通过{@code visitor}参数提供基于AST(抽象语法树)的遍历所有节点的能力。
     *
     * @param sql SQL语句
     * @throws SecurityException 输入的SQL语句有语法错误
     */
    public static boolean check(String sql) {
        boolean allowComplexParsing = CCJSqlParserUtil.getNestingDepth(sql) <= CCJSqlParserUtil.ALLOWED_NESTING_DEPTH;

        try {
//            CCJSqlParserUtil.parse(sql).accept(injectionChecker);
            CCJSqlParserUtil.newParser(sql).withAllowComplexParsing(allowComplexParsing).Statement().accept(injectionChecker);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

