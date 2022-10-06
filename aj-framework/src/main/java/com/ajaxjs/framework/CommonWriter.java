package com.ajaxjs.framework;

import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.annotation.Delete;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.Serializable;
import java.util.Map;

/**
 * 通用的写操作
 */
public abstract class CommonWriter {
    @PostMapping
    public Boolean create(String tableName, Map<String, Object> entity) {
        return JdbcHelper.createMap(null, entity, tableName) != null;
    }

    @PutMapping
    public Boolean update(String tableName, Map<String, Object> entity) {
        return JdbcHelper.updateMap(null, entity, tableName) > 0;
    }

    @Delete
    public Boolean delete(String tableName, Serializable id) {
        return JdbcHelper.deleteById(null, tableName, id);
    }
}
