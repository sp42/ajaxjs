package com.ajaxjs.data;

import com.ajaxjs.util.ObjectHelper;
import org.junit.Test;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collections;
import java.util.Map;

public class TestSmallMyBatis {
    static private final SpelExpressionParser parser = new SpelExpressionParser();

    public static boolean evaluateBoolean(String expression, Map<String, Object> paramMap) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(paramMap);
        context.setPropertyAccessors(Collections.singletonList(new MapAccessor()));

        SpelExpression expr = (SpelExpression) parser.parseExpression(expression);
        expr.setEvaluationContext(context);

        return Boolean.TRUE.equals(expr.getValue(paramMap, boolean.class));
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
    @Test
    public void test(){
        Map<String, Object> params = ObjectHelper.hashMap("a", "z1");
        boolean b = evaluateBoolean("a != null and a != 'z1'", params);
        System.out.println(b);
    }
}
