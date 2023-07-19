package com.ajaxjs.data.jdbc_helper;

import com.ajaxjs.data.DataUtils;
import com.ajaxjs.data.jdbc_helper.common.IgnoreDB;
import com.ajaxjs.framework.entity.TableName;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.Methods;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 基本 JDBC 写入数据操作的封装
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JdbcWriter extends JdbcConn implements JdbcConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(JdbcWriter.class);

    /**
     * id 字段名称，默认 id，可以是其他，如 news_id、xxx_id 等
     * 应该填数据库的，不要 bean 的驼峰风格
     */
    private String idField = "id";

    /**
     * 自定义 WHERE 查询部分
     */
    private String where;

    /**
     * id 字段类型，可以雪花 id（Long）、自增（Integer）、UUID（String），这里默认 Long
     * 它们都有父类 Serializable
     */
    private Class<? extends Serializable> idType = Long.class;

    /**
     * 是否自增 id
     */
    private Boolean isAutoIns = false;

    /**
     * 数据库表名
     */
    private String tableName;

    public static final long INSERT_OK_LONG = -1L;
    public static final int INSERT_OK_INT = -1;
    public static final String INSERT_OK_STR = "INSERT_OK";

    /**
     * 新建记录
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
     */
    public Serializable insert(String sql, Object... params) {
        try (PreparedStatement ps = isAutoIns ? conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(sql)) {
            setParam2Ps(ps, params);
            LOGGER.infoYellow("执行 SQL-->[" + DataUtils.printRealSql(sql, params) + "]");
            int effectRows = ps.executeUpdate();

            if (effectRows > 0) {// 插入成功
                if (isAutoIns) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {// 当保存之后会自动获得数据库返回的主键
                        if (rs.next()) {
                            Object newlyId = rs.getObject(1);

                            if (newlyId instanceof BigInteger)
                                newlyId = ((BigInteger) newlyId).longValue();

                            if (idType.equals(Long.class))
                                return (Long) newlyId;
                            else if (idType.equals(Integer.class))
                                return (Integer) newlyId;
                            else if (idType.equals(String.class))
                                return (String) newlyId;
                        }
                    }
                } else { // 不是自增，但不能返回 null，返回 null 就表示没插入成功
                    if (idType.equals(Long.class))
                        return INSERT_OK_LONG;
                    else if (idType.equals(Integer.class))
                        return INSERT_OK_INT;
                    else if (idType.equals(String.class))
                        return INSERT_OK_STR;
                }
            }
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }

    /**
     * 执行 SQL UPDATE 更新。
     *
     * @param sql    SQL 语句，可以带有 ? 的占位符
     * @param params 插入到 SQL 中的参数，可单个可多个可不填
     * @return 成功修改的行数
     */
    public int write(String sql, Object... params) {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParam2Ps(ps, params);
            LOGGER.infoYellow("执行 SQL-->[" + (DataUtils.printRealSql(sql, params)) + "]");

            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 给 PrepareStatement 用的 SQL 语句和参数值列表
     */
    public static class SqlParams {
        /**
         * 给 PrepareStatement 用的 SQL 语句
         */
        public String sql;

        /**
         * 参数值列表
         */
        public Object[] values;
    }

    private static void everyMapField(Object entity, BiConsumer<String, Object> everyMapField) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) entity;
        if (map.size() == 0) throw new NullPointerException("该实体没有任何字段和数据");

        map.forEach(everyMapField);
    }

    private static void everyBeanField(Object entity, BiConsumer<String, Object> everyBeanField) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());

            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                String filedName = property.getName();
                if ("class".equals(filedName)) continue;

                Method method = property.getReadMethod();
                if (method.getAnnotation(IgnoreDB.class) != null) // 忽略的字段，不参与
                    continue;

                Object value = method.invoke(entity);

                if (value != null) {// 有值的才进行操作
                    String field = DataUtils.changeFieldToColumnName(filedName);
                    everyBeanField.accept(field, value);
                }
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.warning(e);
        }
    }

    /**
     * 将一个实体转换成插入语句的 SqlParams 对象
     *
     * @param tableName 数据库表名
     * @param entity    字段及其对应的值
     * @return 插入语句的 SqlParams 对象
     */
    public static SqlParams entity2InsertSql(String tableName, Object entity) {
        StringBuilder sb = new StringBuilder();
        List<Object> values = new ArrayList<>();
        List<String> valuesHolder = new ArrayList<>();
        sb.append("INSERT INTO ").append(tableName).append(" (");

        if (entity instanceof Map) {
            everyMapField(entity, (field, value) -> {
                sb.append(" `").append(field).append("`,");
                valuesHolder.add(" ?");
                values.add(value);
            });
        } else { // Java Bean
            everyBeanField(entity, (field, value) -> {
                sb.append(" `").append(field).append("`,");
                valuesHolder.add(" ?");
                values.add(beanValue2SqlValue(value));
            });
        }

        sb.deleteCharAt(sb.length() - 1);// 删除最后一个 ,
        sb.append(") VALUES (").append(String.join(",", valuesHolder)).append(")");

        Object[] arr = values.toArray();  // 将 List 转为数组

        SqlParams sp = new SqlParams();
        sp.sql = sb.toString();
        sp.values = arr;

        return sp;
    }

    // Bean 的值转换为符合 SQL 格式的
    private static Object beanValue2SqlValue(Object value) {
        if (value instanceof Enum) // 枚举类型，取其字符串保存
            return value.toString();
        else if (value.equals(NULL_DATE) || value.equals(NULL_INT) || value.equals(NULL_LONG) || value.equals(NULL_STRING)) // 如何设数据库 null 值
            return null;
        else
            return value;
    }

    /**
     * 将一个实体转换成更新语句的 SqlParams 对象
     *
     * @param tableName 数据库表名
     * @param entity    字段及其对应的值
     * @param idField   ID 字段名
     * @param where     指定记录的 ID 值
     * @return 更新语句的 SqlParams 对象
     */
    public static SqlParams entity2UpdateSql(String tableName, Object entity, String idField, Object where) {
        StringBuilder sb = new StringBuilder();
        List<Object> values = new ArrayList<>();
        sb.append("UPDATE ").append(tableName).append(" SET");

        if (entity instanceof Map) {
            everyMapField(entity, (field, value) -> {
                if (field.equals(idField)) // 忽略 id
                    return;

                sb.append(" `").append(field).append("` = ?,");
                values.add(value);
            });
        } else { // Java Bean
            everyBeanField(entity, (field, value) -> {
                if (field.equals(idField)) // 忽略 id
                    return;

                sb.append(" `").append(field).append("` = ?,");
                values.add(beanValue2SqlValue(value));
            });
        }

        sb.deleteCharAt(sb.length() - 1);// 删除最后一个 ,
        Object[] arr = values.toArray();  // 将 List 转为数组

        if (StringUtils.hasText(idField) && where != null) {
            sb.append(" WHERE ").append(idField).append(" = ?");

            arr = Arrays.copyOf(arr, arr.length + 1);
            arr[arr.length - 1] = where; // 将新值加入数组末尾
        }

        SqlParams sp = new SqlParams();
        sp.sql = sb.toString();
        sp.values = arr;

        return sp;
    }

    /**
     * 新建记录
     *
     * @param entity 实体，可以是 Map or Java Bean
     * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
     */
    public Serializable create(Object entity) {
        SqlParams sp = entity2InsertSql(tableName, entity);
        Serializable newlyId = insert(sp.sql, sp.values);

        if (entity instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) entity;
            map.put(idField, newlyId); // id 一开始是没有的，保存之后才有，现在增加到实体
        } else { // bean
            TableName a = entity.getClass().getAnnotation(TableName.class);

            if (a != null && a.isReturnNewlyId()) {
                try {
                    Method getId = entity.getClass().getMethod(DataUtils.changeColumnToFieldName("get_" + idField));

                    if (newlyId == null)
                        return null; // 创建失败

                    if (newlyId.equals(-1)) { // 插入成功 但没有自增
                        return (Serializable) getId.invoke(entity);
                    }

                    Class<?> idClz = getId.getReturnType();// 根据 getter 推断 id 类型
                    String setIdMethod = DataUtils.changeColumnToFieldName("set_" + idField);

                    if (Long.class == idClz && newlyId instanceof Integer) {
                        newlyId = (long) (int) newlyId;
                        Methods.executeMethod(entity, setIdMethod, newlyId);
                    } else if (Long.class == idClz && newlyId instanceof BigInteger) {
                        newlyId = ((BigInteger) newlyId).longValue();
                        Methods.executeMethod(entity, setIdMethod, newlyId);
                    } else Methods.executeMethod(entity, setIdMethod, newlyId); // 直接保存
                } catch (Throwable e) {
                    LOGGER.warning(e);
                }
            }
        }

        return newlyId;
    }

    /**
     * 修改实体
     *
     * @param entity 实体，可以是 Map or Java Bean
     * @return 成功修改的行数，一般为 1
     */
    public int update(Object entity) {
        SqlParams sp;

        if (entity instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) entity;
            sp = entity2UpdateSql(tableName, map, idField, map.get(idField));
        } else {
            String getId = DataUtils.changeColumnToFieldName("get_" + idField);
            Object id = Methods.executeMethod(entity, getId);
            sp = entity2UpdateSql(tableName, entity, idField, id);
        }

        return write(sp.sql, sp.values);
    }

    /**
     * 修改实体
     * 将一个 Map 转换成更新语句的 SqlParams 对象，可允许任意的过滤条件，而不只是 WHERE id = 1
     *
     * @param entity 实体，可以是 Map or Java Bean
     * @return 成功修改的行数，一般为 1
     */
    public int updateWhere(Object entity, String where) {
        SqlParams sp = entity2UpdateSql(tableName, entity, null, null);
        sp.sql += " WHERE " + where;

        return write(sp.sql, sp.values);
    }

    /**
     * 批量插入数据
     *
     * @param fields 数据库表的字段列表，多个字段用逗号分隔，例如："id,name,age"
     * @param values 批量插入的数据列表，每个元素代表一条记录，格式为"('value1', 'value2', 'value3', ... , 'valueN')"
     */
    public void createBatch(String fields, List<String> values) {
        LOGGER.info("批量插入 " + values.size() + " 条数据");
        createBatch(fields, String.join(",", values));
    }

    /**
     * 批量插入数据
     * <a href="https://blog.csdn.net/C3245073527/article/details/122071045">参考链接</a>
     *
     * @param fields 数据库表的字段列表，多个字段用逗号分隔，例如："id,name,age"
     * @param values 批量插入的数据，格式为"('value1', 'value2', 'value3', ... , 'valueN')"，每个元素代表一条记录
     */
    public void createBatch(String fields, String values) {
        long start = System.currentTimeMillis();
        String sql = "INSERT INTO " + tableName + " (" + fields + ") VALUE " + values;
        System.out.println(sql);

        int[] result = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);// 取消自动提交

            ps.addBatch();
            result = ps.executeBatch();
            ps.clearBatch();

            conn.commit();// 所有语句都执行完毕后才手动提交sql语句
        } catch (Throwable e) {
            try {
                conn.rollback();// 回滚事务
            } catch (SQLException ex) {
                LOGGER.warning(ex);
            }

            LOGGER.warning(e);
        }

        System.out.println(Arrays.toString(result));
        LOGGER.info("批量插入完毕 " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 物理删除
     *
     * @param id 实体 ID
     * @return 是否成功
     */
    public boolean delete(Serializable id) {
        String sql = "DELETE FROM " + tableName + " WHERE " + idField + " = ?";

        return write(sql, id) > 0;
    }

    /**
     * 物理批量删除
     *
     * @param ids 实体 ID 列表
     * @return 是否成功
     */
    public boolean deleteBatch(List<? extends Serializable> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tableName).append(" WHERE ").append(idField).append(" IN (");

        List<String> valueHolders = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        ids.forEach(id -> {
            valueHolders.add("?");
            params.add(id);
        });

        sb.append(String.join(",", valueHolders));
        sb.append(")");

        return write(sb.toString(), params.toArray()) > 0;
    }

}
