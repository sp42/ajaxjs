package com.ajaxjs.sql.inject_attack;

import com.ajaxjs.sql.ConstAnalyzer;
import com.ajaxjs.sql.ParserSupport;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 基于SQL语法对象的SQL注入攻击分析实现
 * <p>
 * InjectionSyntaxObjectAnalyzer 为基于SQL语法对象的SQL注入攻击分析实现
 * TablesNamesFinder是jsqlparser提供的一个语法元素遍历对象，继承这个对象可以实现对需要的语法元素的访问，
 * 当遇到有注入攻击危险的表达式，语句时抛出InjectionAttackException异常，就是这个类做的工作
 *
 * @author guyadong
 */
public class InjectionSyntaxObjectAnalyzer extends TablesNamesFinder {
    private static final String DANGROUS_FUNCTIONS = "(sleep|benchmark|extractvalue|updatexml|ST_LatFromGeoHash|ST_LongFromGeoHash|GTID_SUBSET|GTID_SUBTRACT|floor|ST_Pointfromgeohash"
            + "|geometrycollection|multipoint|polygon|multipolygon|linestring|multilinestring)";

    private static ThreadLocal<Boolean> disableSubselect = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    };

    private ConstAnalyzer constAnalyzer = new ConstAnalyzer();

    public InjectionSyntaxObjectAnalyzer() {
        super();
        init(true);
    }

    @Override
    public void visitBinaryExpression(BinaryExpression binaryExpression) {
        if (binaryExpression instanceof ComparisonOperator) {
            if (isConst(binaryExpression.getLeftExpression()) && isConst(binaryExpression.getRightExpression()))
                throw new InjectionAttackException("DISABLE IDENTICAL EQUATION " + binaryExpression);
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
        if (function.getName().matches(DANGROUS_FUNCTIONS))
            throw new InjectionAttackException("DANGROUS FUNCTION: " + function.getName());

        super.visit(function);
    }

    @Override
    public void visit(WithItem withItem) {
        try {
            /** 允许 WITH 语句中的子查询 */
            disableSubselect.set(false);
            super.visit(withItem);
        } finally {
            disableSubselect.set(true);
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        if (disableSubselect.get())
            throw new InjectionAttackException("DISABLE subselect " + subSelect);
    }

    @Override
    public void visit(Column tableColumn) {
        if (ParserSupport.isBoolean(tableColumn))
            throw new InjectionAttackException("DISABLE CONST BOOL " + tableColumn);

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
            throw new InjectionAttackException("DISABLE CONST EXPRESSION " + expression);
    }
}
