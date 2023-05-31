package com.ajaxjs.framework.entity;

import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.XmlHelper;
import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类似 MyBatis 把 SQL 存储在 XML 中，这个是低配版
 */
public class SmallMyBatis {
    private static final LogHelper LOGGER = LogHelper.getLog(SmallMyBatis.class);

    /**
     * key=id/value=sql
     */
    private final Map<String, String> sqls = new HashMap<>();

    /**
     * 加载 XML SQL
     */
    public void loadXML(String... xmlFiles) {
        Pattern pattern = Pattern.compile("<!--.*?-->", Pattern.DOTALL);  // 使用 DOTALL 匹配多行注释

        for (String xmlFile : xmlFiles) {
            String xmlBody = Resources.getResourceText(xmlFile);

            if (xmlBody != null) {
                // 删除注释
                xmlBody = pattern.matcher(xmlBody).replaceAll("");
                XmlHelper.parseXML(xmlBody, (node, nodeList) -> {
                    if ("sql".equals(node.getNodeName())) {
                        String id = XmlHelper.getNodeAttribute(node, "id");

                        if (sqls.containsKey(id))
                            LOGGER.warning("已有相同 id 的 sql，[id]：" + id);

                        String sql = XmlHelper.getNodeText(node);
                        sqls.put(id, sql);
                    }
                });
            }
        }
    }

    /**
     * 根据 id 获取 SQL
     *
     * @param id xml 里面的 id
     * @return SQL
     */
    public String getSqlById(String id) {
        String sql = sqls.get(id);

        if (!StringUtils.hasText(sql))
            throw new IllegalArgumentException("查询 id 为 " + id + "　的 SQL 为空");

        return sql;
    }

    /**
     * 类似 Mybatis 替换动态 sql 的方法，要求支持 <if> 标签
     *
     * @param sqlTemplate SQL 语句
     * @param params      SQL 插值参数集合
     * @return 实际要执行的 SQL
     */
    public static String generateIfBlock(String sqlTemplate, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder(sqlTemplate);
        int startIdx = sb.indexOf("<if");

        while (startIdx != -1) {
            int endIdx = sb.indexOf("</if>", startIdx);
            String ifBlock = sb.substring(startIdx, endIdx + 5);

            if (evalIfBlock(ifBlock, params)) {
                sb.delete(startIdx, endIdx + 5);
                String content = ifBlock.substring(ifBlock.indexOf(">") + 1, ifBlock.lastIndexOf("<"));
                sb.insert(startIdx, content);
            } else
                sb.delete(startIdx, endIdx + 5);

            startIdx = sb.indexOf("<if", startIdx);
        }

        return sb.toString();
    }

    static private SpelExpressionParser parser = new SpelExpressionParser();

    static class BooleanExpressionParser extends SpelExpressionParser {
        private final StandardEvaluationContext context = new StandardEvaluationContext();

        public BooleanExpressionParser() {
            super();
            context.setPropertyAccessors(Collections.singletonList(new MapAccessor()));
//            context.setVariables(paramMap);
        }

        public boolean get(String expression, Map<String, Object> paramMap) {
            SpelExpression expr = (SpelExpression) parseExpression(expression);
            expr.setEvaluationContext(context);

            return Boolean.TRUE.equals(expr.getValue(paramMap, boolean.class));
        }
    }

    static private BooleanExpressionParser p2 =new BooleanExpressionParser();
    public static boolean evaluateBoolean(String expression, Map<String, Object> paramMap) {
        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariables(paramMap);
        context.setPropertyAccessors(Collections.singletonList(new MapAccessor()));

        SpelExpression expr = (SpelExpression) parser.parseExpression(expression);
        expr.setEvaluationContext(context);

        return Boolean.TRUE.equals(expr.getValue(paramMap, boolean.class));
    }

    public static void main(String[] args) {
        Map<String, Object> params = ListUtils.hashMap("a", "z1");
        boolean b = evaluateBoolean("a != null and a != 'z1'", params);
        System.out.println(b);
        b = p2.get("a != null and a != 'z'", params);
        System.out.println(b);
    }

    private static boolean evalIfBlock(String ifBlock, Map<String, Object> params) {
        String test = ifBlock.substring(ifBlock.indexOf("test=") + 6, ifBlock.lastIndexOf("\""));
        String[] tokens = test.split("\\s+");
        String condition = tokens[1], property = tokens[0];
        Object value = params.get(property);

        switch (condition) {
            case "eq":
            case "==":
                if (value == null && "null".equals(tokens[2]))
                    return true;
                return value.toString().equals(tokens[2]);
            case "!=":
            case "ne":
                if (("".equals(value) || (value == null)) && "null".equals(tokens[2]))
                    return false;
                return !value.equals(tokens[2]);
            case "gt":
                return ((Comparable) value).compareTo(tokens[2]) > 0;
            case "ge":
                return ((Comparable) value).compareTo(tokens[2]) >= 0;
            case "lt":
                return ((Comparable) value).compareTo(tokens[2]) < 0;
            case "le":
                return ((Comparable) value).compareTo(tokens[2]) <= 0;
            case "notNull":
                return value != null;
            case "isNull":
                return value == null;
            default:
                throw new UnsupportedOperationException("Unsupported condition: " + condition);
        }
    }

    private String parseForEach(String sql, Map<String, Object> params) {
        StringBuilder result = new StringBuilder(sql.length());
        int start = 0, end = sql.indexOf("<forEach>", start);

        while (end != -1) {
            result.append(sql.substring(start, end));
            int subStart = end + "<forEach>".length(), subEnd = sql.indexOf("</forEach>", subStart);
            String subSql = sql.substring(subStart, subEnd), varName = sql.substring(end + "<forEach>".length(), subStart - "</forEach>".length() - 1).trim();
            Object list = params.get(varName);

            if (list instanceof List<?>) {
                List<?> itemList = (List<?>) list;

                for (int i = 0; i < itemList.size(); i++) {
                    Map<String, Object> subParams = new HashMap<>(params);
                    subParams.put("item", itemList.get(i));
                    subParams.put("index", i);
                    String subResult = parseForEach(subSql, subParams);
                    result.append(subResult);
                }
            }

            start = subEnd + "</forEach>".length();
            end = sql.indexOf("<forEach>", start);
        }

        result.append(sql.substring(start));

        return result.toString();
    }

    private static final Pattern PATTERN = Pattern.compile("(#\\{|\\$\\{)(.*?)(})"); // 匹配占位符的正则表达式

    public static String getValuedSQL(String template, Map<String, Object> paramMap) {
        Matcher matcher = PATTERN.matcher(template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(2); // 获取占位符中的键名
            Object value = paramMap.get(placeholder); // 获取键名对应的值
            String strValue;

            if (value == null) {
                strValue = ""; // 如果值为空，替换为空字符串
            } else {
                strValue = value.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$"); // 处理转义字符和 $ 符号

                if (matcher.group(1).equals("#{")) {
                    // 使用 PreparedStatement 设置参数，自动转换类型

                    if (value instanceof Number)
                        strValue = String.valueOf(value);
                    else if (value.equals(true))
                        strValue = "1";
                    else if (value.equals(false))
                        strValue = "0";
                    else
                        strValue = "'" + value + "'"; // 如果是非数字类型，加上单引号
                }
            }

            matcher.appendReplacement(sb, strValue); // 替换占位符
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
