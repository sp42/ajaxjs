package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        return (PageResult<Map<String, Object>>) namespaces.get(namespace).page(null);
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
}
