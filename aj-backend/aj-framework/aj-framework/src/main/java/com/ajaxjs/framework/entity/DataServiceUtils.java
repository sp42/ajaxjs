package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.convert.ConvertBasicValue;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DataServiceUtils {
    public static Map<String, Object> getQueryStringParams() {
        HttpServletRequest request = DiContextUtil.getRequest();
        assert request != null;
        Map<String, String[]> parameterMap = request.getParameterMap();

        if (ObjectUtils.isEmpty(parameterMap)) return null;

        Map<String, Object> params = new HashMap<>();
        parameterMap.forEach((key, value) -> params.put(key, value == null ? null : ConvertBasicValue.toJavaValue(escapeSqlInjection(value[0]))));

        return params;
    }

    private static final Pattern PATTERN = Pattern.compile("(?i)select|update|delete|insert|drop|truncate|union|\\*|--|;");

    /**
     * 过滤掉所有可能导致 SQL 注入的关键字
     */
    public static String escapeSqlInjection(String input) {
        return PATTERN.matcher(input).replaceAll("");
    }

}
