package com.ajaxjs.framework.teant;

import java.util.List;

import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.annotation.Select;

public interface TenantDao<T> {
    /**
     *
     * @param teantId
     * @return
     */
    @Select("SELECT * FROM ${tableName} WHERE teantId = ?")
    List<T> findByTenantId(long teantId);

    @Select("SELECT * FROM ${tableName} WHERE teantId = ?")
    PageResult<T> findPageByTenantId(long teantId);
}
