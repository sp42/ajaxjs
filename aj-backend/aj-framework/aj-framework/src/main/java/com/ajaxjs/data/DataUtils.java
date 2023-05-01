package com.ajaxjs.data;

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
}
