package com.ajaxjs.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用java写出一个类似 mybatis 替换动态 sql 的方法，要求支持 <forEach> 遍历标签
 * <p>
 * 这里提供一个简单的示例方法，用于将 SQL 中的 ${xxx} 占位符替换为对应的值，并支持 <forEach> 遍历标签。
 * 具体实现方式是，首先使用正则表达式匹配 SQL 中的 ${xxx}，然后通过 Map 类型的参数获取对应的值，并将占位符替换为实际的值。
 * 如果 SQL 中包含 <forEach> 标签，将递归执行该方法，将遍历标签中的子 SQL 依次解析并替换后拼接成最终的 SQL 字符串。
 * <p>
 * 在上述代码中，parse() 方法用于解析 SQL 字符串和参数，并返回最终的 SQL 字符串。方法内部使用正则表达式匹配 ${xxx} 占位符，然后通过参数 params 获取对应的值，并将占位符替换为实际的值。
 * 如果 SQL 中包含 <forEach> 标签，则调用 parseForEach() 方法递归处理子 SQL 并进行拼接。
 * <p>
 * parseForEach() 方法用于解析 <forEach> 标签，遍历列表并递归处理子 SQL。
 * 方法首先查找 <forEach> 标签的位置，并将前面的部分添加到结果中。然后解析子 SQL，并获取列表参数。如果列表参数是一个 List 类型的对象，则遍历列表，依次将每个元素与 index 索引存入 Map 中，并递归调用 parse() 方法解析子 SQL 并拼接到结果中。
 * 最后处理完所有的子 SQL 后，将结果返回。
 */
public class MybatisSqlParser {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\{([^{}]+)\\}");

    public String parse(String sql, Map<String, Object> params) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = PARAMETER_PATTERN.matcher(sql);
        int index = 0;
        while (matcher.find()) {
            String paramName = matcher.group(1);
            Object paramValue = params.get(paramName);
            if (paramValue != null) {
                result.append(sql.substring(index, matcher.start()));
                result.append(paramValue);
                index = matcher.end();
            }
        }
        result.append(sql.substring(index));
        return parseForEach(result.toString(), params);
    }

    private String parseForEach(String sql, Map<String, Object> params) {
        StringBuilder result = new StringBuilder(sql.length());
        int start = 0;
        int end = sql.indexOf("<forEach>", start);

        while (end != -1) {
            result.append(sql.substring(start, end));
            int subStart = end + "<forEach>".length();
            int subEnd = sql.indexOf("</forEach>", subStart);
            String subSql = sql.substring(subStart, subEnd);
            String varName = sql.substring(end + "<forEach>".length(), subStart - "</forEach>".length() - 1).trim();
            Object list = params.get(varName);

            if (list instanceof List<?>) {
                List<?> itemList = (List<?>) list;
                for (int i = 0; i < itemList.size(); i++) {
                    Map<String, Object> subParams = new HashMap<>(params);
                    subParams.put("item", itemList.get(i));
                    subParams.put("index", i);
                    String subResult = parse(subSql, subParams);
                    result.append(subResult);
                }
            }

            start = subEnd + "</forEach>".length();
            end = sql.indexOf("<forEach>", start);
        }

        result.append(sql.substring(start));

        return result.toString();
    }
}
