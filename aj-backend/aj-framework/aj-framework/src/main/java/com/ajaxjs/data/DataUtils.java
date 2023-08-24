package com.ajaxjs.data;

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据工具类
 */
public class DataUtils {
    /**
     * 将以下划线分隔的数据库字段转换为驼峰风格的字符串
     *
     * @param columnName 下划线分隔的字符串
     * @return 驼峰风格的字符串
     */
    public static String changeColumnToFieldName(String columnName) {
        StringBuilder result = new StringBuilder();
        String[] words = columnName.split("_");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (i != 0) {
                // 将每个单词的首字母大写
                char firstChar = word.charAt(0);
                word = Character.toUpperCase(firstChar) + word.substring(1);
            }

            result.append(word);
        }

        return result.toString();
    }

    /**
     * 将驼峰风格的字符串转换为以下划线分隔的数据库字段
     *
     * @param fieldName 驼峰风格的字符串
     * @return 下划线分隔的数据库字段
     */
    public static String changeFieldToColumnName(String fieldName) {
        if (fieldName == null)
            return null;

        StringBuilder columnName = new StringBuilder();
        int length = fieldName.length();

        for (int i = 0; i < length; i++) {
            char c = fieldName.charAt(i);

            if ('A' <= c && 'Z' >= c)
                columnName.append("_").append((char) (c + 32));
            else
                columnName.append(fieldName.charAt(i));
        }

        String str = columnName.toString();

        if (str.startsWith("_"))  // 单字母 如 SXxx 会出现 _s_xxx
            str = str.substring(1);

        return str;
    }

    /**
     * ? 和参数的实际个数是否匹配
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return true 表示为 ? 和参数的实际个数匹配
     */
    private static boolean match(String sql, Object[] params) {
        if (params == null || params.length == 0)
            return true; // 没有参数，完整输出

        Matcher m = Pattern.compile("(\\?)").matcher(sql);
        int count = 0;
        while (m.find())
            count++;

        return count == params.length;
    }

    /**
     * 多行空行
     */
    private final static String SPACE_LINE = "(?m)^[ \t]*\r?\n";

    /**
     * 打印真实 SQL 执行语句
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 实际 sql 语句
     */
    public static String printRealSql(String sql, Object[] params) {
        if (!StringUtils.hasText(sql))
            throw new IllegalArgumentException("SQL 语句不能为空！");

        //        if (isClosePrintRealSql)
        //            return null;

        sql = sql.replaceAll(SPACE_LINE, "");

        if (params == null || params.length == 0) // 完整的 SQL 无须填充
            return sql;

        if (!match(sql, params))
            LogHelper.p("SQL 语句中的占位符与值参数（个数上）不匹配。SQL：{0}，\nparams:{1}", sql, Arrays.toString(params));

        if (sql.endsWith("?"))
            sql += " ";

        String[] arr = sql.split("\\?");

        for (int i = 0; i < arr.length - 1; i++) {
            Object value = params[i];
            String inSql;

            if (value instanceof Date) // 只考虑了字符串、布尔、数字和日期类型的转换
                inSql = "'" + value + "'";
            else if (value instanceof String)
                inSql = "'" + value + "'";
            else if (value instanceof Boolean)
                inSql = (Boolean) value ? "1" : "0";
            else if (value != null)
                inSql = value.toString();// number
            else
                inSql = "";

            arr[i] = arr[i] + inSql;
        }

        String str = String.join(" ", arr).trim();

        return insertNewline(str, 20);
    }

    /**
     * 指定行数时插入换行符
     *
     * @param input 字符串
     * @param n     第几个单词就换行
     * @return 字符串
     */
    public static String insertNewline(String input, int n) {
        StringBuilder sb = new StringBuilder();
        String[] words = input.split(" ");
        int lineCount = 0;

        for (String word : words) {
            sb.append(word).append(" ");
            lineCount++;

            if (lineCount == n) {
                sb.append("\n");
                lineCount = 0;
            }
        }

        return sb.toString().trim();
    }

    /**
     * 检测表是否存在
     */
    public  static boolean checkTableExists(Connection conn, String tableName) {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            if (rs.next())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
