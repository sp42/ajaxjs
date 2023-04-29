package com.ajaxjs.database_meta;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 可以根据 Java 对象 生成 Create table 语句
 * MYSQL 表转 JavaBean 工具类 <a href="https://www.cnblogs.com/niejunlei/p/10169654.html">...</a>
 *
 * @author Stephen Cai 2018-08-22 15:20
 */
public class DaoGenUtils {
    public static void genParam(List<String> params) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        sb1.append(" where ");

        for (String p : params) {
            sb.append("@Param(").append(p).append(") ").append(p).append(",");
            sb1.append(p).append(" = #{").append(p).append("} and ");
        }

        System.out.println(sb);
        System.out.println(sb1);
    }

    public static void genDataSchema(Class<?> clazz) {
        if (clazz == null)
            return;

        String tableName = "PN_" + clazz.getSimpleName();
        // 获取关联的所有类，本类以及所有父类
        boolean ret = true;
        List<Class<?>> clazzs = new ArrayList<>();

        // 需要关联父类时候打开这个注释
        while (ret) {
            clazzs.add(clazz);
            clazz = clazz.getSuperclass();

            if (clazz == null || clazz == Object.class)
                break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE `").append(tableName).append("` (\n");

        for (Class<?> aClass : clazzs) {
            Field[] declaredFields = aClass.getDeclaredFields();
//			List<Field> declaredFieldsList = Lists.newArrayList(declaredFields); // com.google.common.collect.Lists
            List<Field> declaredFieldsList = new ArrayList<>(declaredFields.length);
            declaredFieldsList = declaredFieldsList.stream().collect(Collectors.toList());

            Field[] fields = declaredFieldsList.toArray(new Field[0]);
            int length = fields.length;

            for (int j = 0; j < length; j++) {
                Field field = fields[j];
                int mod = field.getModifiers();
                // 过滤 static 和 final 类型
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod))
                    continue;

                field.setAccessible(true);
                String fieldName = field.getName();
                String schemaStr = "`" + fieldName + "` " + getSchemaType(field) + " \n";

                if (j == length - 1)
                    schemaStr = schemaStr.replace(",", "");

                sb.append("  ").append(schemaStr);
            }
        }

        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;\n");
        System.out.println(sb);
    }

    private static String getSchemaType(Field field) {
        Class<?> fieldType = field.getType();

        if (fieldType == String.class)
            return "VARCHAR(50) NOT NULL,";
        else if (fieldType == Double.class || fieldType == double.class || fieldType == BigDecimal.class)
            return "BIGDECIMAL(10,2) NOT NULL,";
        else if (fieldType == Integer.class || fieldType == int.class) {
            if (field.getName().toLowerCase().contains("type") || field.getName().toLowerCase().contains("status"))
                return "TINYINT(4) NOT NULL,";
            else
                return "INT(11) NOT NULL,";
        } else if (fieldType == Date.class) {
            if (field.getName().contains("add"))
                return "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,";
            else if (field.getName().contains("update"))
                return "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,";

            return "timestamp";
        }
        return "VARCHAR(50) NOT NULL,";
    }
}