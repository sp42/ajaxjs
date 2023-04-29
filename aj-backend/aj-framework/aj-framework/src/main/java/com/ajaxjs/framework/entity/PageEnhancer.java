package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.JdbcUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;
import lombok.Data;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * SQL 增强器
 *
 * @author Frank Cheung sp42@qq.com
 */
@Data
public class PageEnhancer {
    private static final LogHelper LOGGER = LogHelper.getLog(PageEnhancer.class);

    /**
     * 统计总数的 SQL
     */
    public String countTotal;

    /**
     * 分页 SQL
     */
    public String pageSql;

//    private String

    private int start;
    private int limit;

    /**
     * 获取分页参数
     */
    public void getParams() {
        /* 判断分页参数，兼容 MySQL or 页面两者。最后统一使用 start/limit */
        HttpServletRequest req = DiContextUtil.getRequest();

        if (req == null) {
            // 可能在测试
            start = 0;
            limit = PageResult.DEFAULT_PAGE_SIZE;

            return;
        }

        Integer pageSize = get(req, new String[]{"pageSize", "rows", "limit"});
        limit = pageSize == null ? PageResult.DEFAULT_PAGE_SIZE : pageSize;

        Integer pageNo = get(req, new String[]{"pageNo", "page"});

        if (pageNo != null) {
            start = JdbcUtil.pageNo2start(pageNo, limit);
        } else if (req.getParameter("start") != null) {
            start = Integer.parseInt(req.getParameter("start"));
        } else
            start = 0;
    }

    private static Integer get(HttpServletRequest req, String[] maybe) {
        for (String m : maybe) {
            if (req.getParameter(m) != null) {
                return Integer.parseInt(req.getParameter(m));
            }
        }

        return null;
    }


    public PageEnhancer initSql(String sql) {
        getParams();
        return initSql(sql, start, limit);
    }

    /**
     * 分页
     *
     * @param sql   普通 SELECT 语句
     * @param start 起始数
     * @param limit 读取数量
     * @return 该实例
     */
    public PageEnhancer initSql(String sql, int start, int limit) {
        Select selectStatement = null;

        try {
            selectStatement = (Select) CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            LOGGER.info(sql);
            LOGGER.warning(e);
        }

        assert selectStatement != null;
        SelectBody selectBody = selectStatement.getSelectBody();

        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;

            // 设置分页语句
//            Limit limitObj = new Limit();
//            limitObj.setRowCount(new LongValue(limit));
//            limitObj.setOffset(new LongValue(start));
//            plainSelect.setLimit(limitObj);

//            pageSql = selectStatement.toString();
            pageSql = sql + " LIMIT " + start + ", " + limit;
//			System.out.println(result.pageSql);

            // 移除 排序 语句
            if (sql.toUpperCase().contains("ORDER BY")) {
                List<OrderByElement> orderBy = plainSelect.getOrderByElements();

                if (orderBy != null) plainSelect.setOrderByElements(null);
            }

            // 创建一个 count 函数的表达式
            Function countFunc = new Function();
            countFunc.setName("COUNT");
            countFunc.setParameters(new ExpressionList(new AllColumns()));

            // 替换所有的 Select Item
            List<SelectItem> selectItems = plainSelect.getSelectItems();
            selectItems.clear();
            selectItems.add(new SelectExpressionItem(countFunc));

            countTotal = selectStatement.toString();
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

            countTotal = selectStatement.toString();
//		System.out.println(result.countTotal);
        }

        return this;
    }

    public <T> PageResult<T> page(Class<T> beanCls) {
        PageResult<T> result = new PageResult<>();
        Connection connection = JdbcConnection.getConnection();
        Long total = JdbcHelper.queryOne(connection, countTotal, Long.class);

        if (total != null && total > 0) {
            List<T> list;

            if (beanCls == null) list = (List<T>) JdbcHelper.queryAsMapList(connection, pageSql);
            else list = JdbcHelper.queryAsBeanList(beanCls, connection, pageSql);

            if (list != null) {
                result.setTotalCount(total.intValue());
                result.addAll(list);

                return result;
            }
        }

        result.setTotalCount(0);
        result.setZero(true);

        return result;
    }

    public static <T> PageResult<T> page(String sql, Class<T> beanClz) {
        PageEnhancer p = new PageEnhancer();
        p.initSql(sql);

        return p.page(beanClz);
    }
}
