package com.ajaxjs.framework.sql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLFormatter {
    public static String formatSql(String sql) {
        sql = sql.replaceAll("\\n", " ");

        Pattern pattern = Pattern.compile("(?i)\\s*(SELECT|FROM|WHERE|GROUP BY|HAVING|ORDER BY)\\s*");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer sb = new StringBuffer();
        int indentLevel = 0;
        while (matcher.find()) {

            String matchStr = matcher.group();
            if (matchStr.equalsIgnoreCase("SELECT") || matchStr.equalsIgnoreCase("FROM")) {
                indentLevel++;
            } else if (matchStr.equalsIgnoreCase("WHERE") || matchStr.equalsIgnoreCase("GROUP BY") || matchStr.equalsIgnoreCase("HAVING") || matchStr.equalsIgnoreCase("ORDER BY")) {
                indentLevel--;
            }
            matcher.appendReplacement(sb, "\n" + getIndent(indentLevel) + matchStr.trim() + " ");
        }

        matcher.appendTail(sb);

        return sb.toString().trim();
    }

    private static String getIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String sql = "SELECT a.id, b.name, c.age FROM t_user a JOIN t_user_profile b ON a.id = b.user_id " +
                "JOIN t_user_info c ON a.id = c.user_id WHERE a.id = 1 AND b.region_code IN (SELECT ID, NAME FROM USERS WHERE ID = 1 AND DELETED = 0 ORDER BY NAME)";
        String input = "";


        String output = formatSql(sql);

        System.out.println(output);

    }
}
