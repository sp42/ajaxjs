package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.spring.DiContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class BaseCRUDService implements BaseCRUDController {
    public final Map<String, BaseCRUD<?, Long>> namespaces = new HashMap<>();

    @Override
    public Map<String, Object> info(String namespace, Long id) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).infoMap(id);
    }

    @Override
    public List<Map<String, Object>> list(String namespace) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).listMap();
    }

    @Override
    public PageResult<Map<String, Object>> page(String namespace) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        String where = getWhereClause(Objects.requireNonNull(DiContextUtil.getRequest()));

        return (PageResult<Map<String, Object>>) namespaces.get(namespace).page(where);
    }

    @Override
    public Long create(String namespace, Map<String, Object> params) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).create(params);
    }

    @Override
    public Boolean update(String namespace, Map<String, Object> params) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).update(params);
    }

    @Override
    public Boolean delete(String namespace, Long id) {
        if (!namespaces.containsKey(namespace))
            throw new BusinessException("没有配置 BaseCRUD");

        return namespaces.get(namespace).delete(id);
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

}
