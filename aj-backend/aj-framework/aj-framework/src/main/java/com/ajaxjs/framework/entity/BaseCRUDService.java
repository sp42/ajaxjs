package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.DataUtils;
import com.ajaxjs.data.SmallMyBatis;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.util.convert.ConvertBasicValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public abstract class BaseCRUDService implements BaseCRUDController, BaseEntityConstants {
    public final Map<String, BaseCRUD<?, Long>> namespaces = new HashMap<>();

    @Override
    public Map<String, Object> info(String namespace, Long id) {
        return getCRUD(namespace, crud -> CRUD.infoMap(crud.getSql()), crud -> crud.infoMap(id));
    }

    @Override
    public Map<String, Object> info(String namespace, String namespace2, Long id) {
        BaseCRUD<?, Long> crud = getCrudChild(namespace, namespace2);

        return isSingle(crud) ? CRUD.infoMap(crud.getSql()) : crud.infoMap(id);
    }

    @Override
    public List<Map<String, Object>> list(String namespace) {
        return getCRUD(namespace, crud -> CRUD.listMap(crud.getSql()), BaseCRUD::listMap);
    }

    private BaseCRUD<?, Long> getCrudChild(String namespace, String namespace2) {
        if (!namespaces.containsKey(namespace))
            throw new IllegalStateException("命名空间 " + namespace + " 没有配置 BaseCRUD");

        BaseCRUD<?, Long> parentCrud = namespaces.get(namespace);
        BaseCRUD<?, Long> crud = parentCrud.getChildren().get(namespace2);

        if (crud == null)
            throw new IllegalStateException("命名空间 " + namespace2 + " 没有配置 BaseCRUD");

        return crud;
    }

    @Override
    public List<Map<String, Object>> list(String namespace, String namespace2) {
        BaseCRUD<?, Long> crud = getCrudChild(namespace, namespace2);

        return isSingle(crud) ? CRUD.listMap(crud.getSql()) : crud.listMap();
    }

    private static boolean isSingle(BaseCRUD<?, Long> crud) {
        return CMD_TYPE.SINGLE.equals(crud.getType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResult<Map<String, Object>> page(String namespace) {
        String where = getWhereClause(Objects.requireNonNull(DiContextUtil.getRequest()));

        return (PageResult<Map<String, Object>>) getCRUD(namespace, crud -> {
            // TODO, handle WHERE
            return CRUD.page(null, crud.getSql(), null);
        }, crud -> crud.page(where));
    }

    @Override
    public PageResult<Map<String, Object>> page(String namespace, String namespace2) {
        return null;
    }

    @Override
    public Long create(String namespace, Map<String, Object> params) {
        final Map<String, Object> _params = initParams(namespace, params, true);

        return (Long) getCRUD(namespace, crud -> {
            String sql = SmallMyBatis.handleSql(crud.getSql(), _params);
            return CRUD.jdbcWriterFactory().insert(sql);
        }, crud -> {
            if (StringUtils.hasText(crud.getCreateSql())) {
                String _sql = SmallMyBatis.handleSql(crud.getCreateSql(), _params);
                return CRUD.jdbcWriterFactory().insert(_sql);
            } else
                return crud.create(_params);
        });
    }

    @Override
    public Long create(String namespace, String namespace2, Map<String, Object> params) {
        return null;
    }

    @Override
    public Boolean update(String namespace, Map<String, Object> params) {
        final Map<String, Object> _params = initParams(namespace, params);

        return getCRUD(namespace, crud -> {
            String sql = SmallMyBatis.handleSql(crud.getSql(), _params);
            return CRUD.jdbcWriterFactory().write(sql) > 0;
        }, crud -> crud.update(_params));
    }

    @Override
    public Boolean update(String namespace, String namespace2, Map<String, Object> params) {
        return update(namespace, params);
    }

    /**
     * 执行 CRUD 操作
     *
     * @param namespace 命名空间
     * @param singleCMD 单个命令操作的函数
     * @param crudCMD   CRUD 操作的函数
     * @param <R>       返回值类型
     * @return 执行结果
     */
    private <R> R getCRUD(String namespace, Function<BaseCRUD<?, Long>, R> singleCMD, Function<BaseCRUD<?, Long>, R> crudCMD) {
        if (!namespaces.containsKey(namespace))
            throw new IllegalStateException("命名空间 " + namespace + " 没有配置 BaseCRUD");

        BaseCRUD<?, Long> crud = namespaces.get(namespace);

        return isSingle(crud) ? singleCMD.apply(crud) : crudCMD.apply(crud);
    }

    /**
     * 初始化参数的封装方法。
     * 这是一个重载方法，调用的是另一个具有三个参数的 initParams 方法，其中最后一个参数默认为 false。
     *
     * @param namespace 命名空间，用于区分不同的参数集合。
     * @param params    参数集合，以键值对的形式存储不同的参数。
     * @return 返回一个初始化后的参数集合 Map。
     */
    private Map<String, Object> initParams(String namespace, Map<String, Object> params) {
        return initParams(namespace, params, false);
    }

    /**
     * 初始化参数
     *
     * @param namespace        命名空间
     * @param params           原始参数映射
     * @param isFormSubmitOnly 是否仅处理表单提交的参数
     * @return 处理后的参数映射
     */
    private Map<String, Object> initParams(String namespace, Map<String, Object> params, boolean isFormSubmitOnly) {
        if (isFormSubmitOnly) {
            HttpServletRequest req = DiContextUtil.getRequest();
            assert req != null;
            String queryString = req.getQueryString(); // 获取查询字符串

            // 解码查询字符串并处理参数
            if (StringUtils.hasText(queryString)) {
                queryString = StringUtils.uriDecode(queryString, StandardCharsets.UTF_8);
                String[] parameters = queryString.split("&");

                for (String parameter : parameters) {// 从 params 中移除查询字符串中的参数
                    String[] keyValuePair = parameter.split("=");
                    params.remove(keyValuePair[0]);
                }
            }
        }

        Map<String, Object> _params = new HashMap<>(); // 创建新的参数映射，并将原始参数转换为指定格式
        params.forEach((key, value) -> _params.put(DataUtils.changeFieldToColumnName(key), ConvertBasicValue.toJavaValue(value.toString())));

        return _params;
    }


    @Override
    public Boolean delete(String namespace, Long id) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).delete(id);
    }

    @Override
    public Boolean delete(String namespace, String namespace2, @PathVariable Long id) {
        return delete(namespace, id);
    }

    /**
     * 基于 URL 的 QueryString，设计一个条件查询的参数规范，可以转化为 SQL 的 Where 里面的查询
     *
     * @param request 请求对象
     * @return SQL Where 语句
     */
    public static String getWhereClause(HttpServletRequest request) {
        // 获取所有 QueryString 参数
        Map<String, String[]> parameters = request.getParameterMap();

        // 创建一个用于存储 SQL 查询的 StringBuilder
        StringBuilder whereClause = new StringBuilder();

        // 遍历所有参数
        for (String parameterName : parameters.keySet()) {
            // 跳过不符合条件的参数
            if (!parameterName.startsWith("q_")) {
                continue;
            }

            // 获取参数值
            String[] parameterValues = parameters.get(parameterName);

            // 构建 SQL 查询
            whereClause.append(" AND ");
            whereClause.append(parameterName.substring(2));

            // 处理单值参数
            if (parameterValues.length == 1) {
                whereClause.append(" = ");
                whereClause.append("'").append(parameterValues[0]).append("'");
            } else {
                // 处理数组参数
                whereClause.append(" IN (");
                for (String parameterValue : parameterValues) {
                    whereClause.append("'");
                    whereClause.append(parameterValue);
                    whereClause.append("',");
                }
                whereClause.deleteCharAt(whereClause.length() - 1);
                whereClause.append(")");
            }
        }

        // 返回 SQL 查询
        return whereClause.toString();
    }

    @Override
    public boolean reloadConfig() {
        loadConfigFromDatabase();

        return true;
    }

    /**
     * 创建之前的执行的回调函数，可以设置 createDate, createBy 等字段
     */
    @Autowired(required = false)
    @Qualifier("CRUD_beforeCreate")
    private Consumer<Map<String, Object>> beforeCreate;

    /**
     * 创建之前的执行的回调函数，可以设置 updateDate, updateBy 等字段
     */
    @Autowired(required = false)
    @Qualifier("beforeUpdate")
    private Consumer<Map<String, Object>> beforeUpdate;

    /**
     * 删除之前的执行的回调函数，可以设置 updateDate, updateBy 等字段
     */
    @Autowired(required = false)
    @Qualifier("beforeDelete")
    private BiFunction<Boolean, String, String> beforeDelete;

    /**
     * 从数据库中加载配置。
     * 此方法首先初始化数据库连接，然后清除现有的配置命名空间。接着，它从数据库中查询所有状态不为1的配置项，并进行处理：
     * - 对查询结果进行排序（按照 pid，以 pid 为-1的项首先排列）；
     * - 遍历排序后的结果，为每个配置项创建一个 CRUD 对象，并根据配置项的 pid 来建立父子关系；
     * - 最后，将所有的配置项按照其所属的命名空间保存到 namespaces 中。
     * 在整个过程完成后，会关闭数据库连接。
     */
    public void loadConfigFromDatabase() {
        DataBaseConnection.initDb();
        namespaces.clear();

        try {
            List<ConfigPO> list = CRUD.list(ConfigPO.class, "SELECT * FROM ds_common_api WHERE stat != 1");// 从数据库中查询所有状态不为1的配置项
            list.sort(Comparator.comparingInt(ConfigPO::getPid)); // 根据pid对配置项进行排序
            Map<Integer, BaseCRUD<Map<String, Object>, Long>> configMap = new HashMap<>();

            if (!CollectionUtils.isEmpty(list)) {
                for (ConfigPO config : list) {
                    BaseCRUD<Map<String, Object>, Long> crud = new BaseCRUD<>(); // 为每个配置项创建CRUD对象，并复制配置项的属性到CRUD对象中
                    BeanUtils.copyProperties(config, crud);

                    if (beforeCreate != null)  // 如果存在 beforeCreate 回调，则设置到 CRUD 对象中
                        crud.setBeforeCreate(beforeCreate);

                    if (beforeUpdate != null)  // 如果存在 beforeUpdate 回调，则设置到 CRUD 对象中
                        crud.setBeforeCreate(beforeUpdate);

                    if (beforeDelete != null)  // 如果存在 beforeUpdate 回调，则设置到 CRUD 对象中
                        crud.setBeforeDelete(beforeDelete);

                    // 如果 pid 为 -1，表示为顶级配置，将其添加到 namespaces 中，并初始化其 children 属性
                    if (crud.getPid() == -1) {
                        namespaces.put(config.getNamespace(), crud);

                        configMap.put(crud.getId(), crud);
                        crud.setChildren(new HashMap<>());
                    } else {
                        BaseCRUD<Map<String, Object>, Long> _crud = configMap.get(crud.getPid()); // 查找并添加父级配置项的子配置项

                        if (_crud == null)
                            throw new IllegalStateException("程序错误：没有找到父级");

                        _crud.getChildren().put(crud.getNamespace(), crud);
                    }
                }
            }
        } finally {
            DataBaseConnection.closeDb(); // 执行完毕后关闭数据库连接
        }
    }

}
