package com.ajaxjs.framework.entity;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.DataUtils;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class BaseCRUDService implements BaseCRUDController {
    public final Map<String, BaseCRUD<?, Long>> namespaces = new HashMap<>();

    @Override
    public Map<String, Object> info(String namespace, Long id) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).infoMap(id);
    }

    @Override
    public Map<String, Object> info(String namespace, String namespace2, Long id) {
        System.out.println(namespace2);
        return null;
    }

    @Override
    public List<Map<String, Object>> list(String namespace) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).listMap();
    }

    @Override
    public List<Map<String, Object>> list(String namespace, String namespace2) {
        return null;
    }

    @Override
    public PageResult<Map<String, Object>> page(String namespace) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        String where = getWhereClause(Objects.requireNonNull(DiContextUtil.getRequest()));

        return (PageResult<Map<String, Object>>) namespaces.get(namespace).page(where);
    }

    @Override
    public PageResult<Map<String, Object>> page(String namespace, String namespace2) {
        return null;
    }

    @Override
    public Long create(String namespace, Map<String, Object> params) {
        params = init(namespace, params);

        return namespaces.get(namespace).create(params);
    }

    @Override
    public Long create(String namespace, String namespace2, Map<String, Object> params) {
        return null;
    }

    @Override
    public Boolean update(String namespace, Map<String, Object> params) {
        params = init(namespace, params);

        return namespaces.get(namespace).update(params);
    }

    @Override
    public Boolean update(String namespace, String namespace2, Map<String, Object> params) {
        return update(namespace, params);
    }

    private Map<String, Object> init(String namespace, Map<String, Object> params) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        Map<String, Object> _params = new HashMap<>();
        params.forEach((key, value) -> _params.put(DataUtils.changeFieldToColumnName(key), value));

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
     * 从数据库中加载配置
     */
    public void loadConfigFromDatabase() {
        DataBaseConnection.initDb();
        namespaces.clear();

        try {
            List<ConfigPO> list = CRUD.list(ConfigPO.class, "SELECT * FROM ds_common_api WHERE stat != 1");

            if (!CollectionUtils.isEmpty(list)) {
                for (ConfigPO config : list) {
                    // 定义表的 CRUD
                    BaseCRUD<Map<String, Object>, Long> app = new BaseCRUD<>();
                    BeanUtils.copyProperties(config, app);

                    namespaces.put(config.getNamespace(), app);
                }
            }
        } finally {
            DataBaseConnection.closeDb();
        }
    }
}
