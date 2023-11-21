package com.ajaxjs.developertools.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.developertools.api.DatasourceController;
import com.ajaxjs.developertools.model.DataSourceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class DatasourceService implements DatasourceController {
    @Override
    public List<DataSourceInfo> list() {
        return CRUD.list(DataSourceInfo.class, "SELECT * FROM adp_datasource WHERE stat != 1");
    }

    @Override
    public Boolean test(Long id) {
        DataSourceInfo info = CRUD.info(DataSourceInfo.class, "SELECT * FROM adp_datasource WHERE stat != 1 AND id = ?", id);

        try (Connection connection = DataServiceUtils.getConnectionByDataSourceInfo(info)) {
            log.info(connection.getMetaData().getURL());

            return true;
        } catch (SQLException e) {
            log.warn("err:", e);
            return false;
        }
    }

    @Override
    public Long create(DataSourceInfo entity) {
        return CRUD.create(entity);
    }

    /**
     * 是否重复数据源编码
     *
     * @param dsId 数据源 id，非 null 时候表示更新排除自己
     */
    private void checkIfIsRepeat(DataSourceInfo entity, Long dsId) {
        String sql;

        if (dsId != null)
            sql = "SELECT id FROM adp_datasource WHERE url_dir = ? AND id != " + dsId + " LIMIT 1";
        else
            sql = "SELECT id FROM adp_datasource WHERE url_dir = ? LIMIT 1";

        Long id = CRUD.queryOne(Long.class, sql, entity.getUrlDir());

        if (id != null)
            throw new IllegalArgumentException("已存在相同编码的数据源 " + entity.getUrlDir());
    }

    @Override
    public Boolean update(DataSourceInfo entity) {
        if (entity.getId() == null)
            throw new IllegalArgumentException("缺少 id 参数");

        checkIfIsRepeat(entity, entity.getId());

        return CRUD.updateWithIdField(entity);
    }

    @Override
    public Boolean delete(Long id) {
        return CRUD.delete("adp_datasource", id);
    }
}
