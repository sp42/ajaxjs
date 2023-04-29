package com.ajaxjs.data_service.controller;

import com.ajaxjs.data_service.DataServiceUtils;
import com.ajaxjs.data_service.model.DataServiceEntity;
import com.ajaxjs.data_service.model.DataSourceInfo;
import com.ajaxjs.data_service.service.DataService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.entity.IBaseCRUD;
import com.ajaxjs.framework.spring.easy_controller.anno.ControllerMethod;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.sql.util.DataBaseMetaHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.util.regexp.RegExpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据服务 后台控制器
 */
public abstract class BaseDataServiceAdminController extends ProjectService implements IBaseCRUD<DataServiceEntity, Long> {
    private static final LogHelper LOGGER = LogHelper.getLog(BaseDataServiceAdminController.class);

    @Autowired
    DataService dataService;

    /**
     * 返回数据源配置所在的表名
     *
     * @return
     */
    protected abstract String getDataSourceTableName();

    @GetMapping("/reload")
    public Boolean reload() {
        dataService.init();// 重新加载配置

        return true;
    }

    @GetMapping("/{id}")
    public DataServiceEntity info(@PathVariable long id, String dbName) throws ClassNotFoundException, SQLException {
        LOGGER.info("加载表详情");

        Connection conn;
        DataServiceEntity info;

        try (Connection _conn = getConnection()) {
            String sql = "SELECT * FROM " + getDataServiceTableName() + " WHERE id = " + id;
            info = JdbcHelper.queryAsBean(DataServiceEntity.class, _conn, sql);

            if (info == null)
                throw new NullPointerException("找不到 id 為 " + id + " 的数据服务配置。");

            // 获取所有字段
            if (dataService.getCfg().isMultiDataSource())
                conn = DataServiceUtils.getConnByDataSourceInfo(_conn, getDataSourceTableName(), info.getDatasourceId());
            else
                conn = JdbcConnection.getConnection();
        }

        List<Map<String, String>> columnComment = null;

        try {
            columnComment = DataBaseMetaHelper.getColumnComment(conn, info.getTableName(), dbName);
        } catch (Throwable e) {
            LOGGER.warning(e);
        } finally {
            conn.close();
        }

        if (columnComment != null) {
            Map<String, String> map = new HashMap<>();

            for (int i = 0; i < columnComment.size(); i++)
                map.put(columnComment.get(i).get("name"), columnComment.get(i).get("comment"));

            info.setFields(map);
        }

        str2Json(info);

        return info;
    }

    @GetMapping
    public List<DataServiceEntity> list(Long projectId) {
        LOGGER.info("获取表配置列表");
        String sql = "SELECT d.*, ds.name AS datasourceName FROM " + getDataServiceTableName()
                + " d LEFT JOIN aj_base.adp_datasource ds ON d.datasource_id = ds.id";

        if (projectId != null)
            sql += " WHERE project_id = " + projectId;

        try (Connection conn = getConnection()) {
            List<DataServiceEntity> list = JdbcHelper.queryAsBeanList(DataServiceEntity.class, conn, sql);

            for (DataServiceEntity e : list)
                str2Json(e);

            return list;
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e);
        }
    }

    private static void str2Json(DataServiceEntity e) {
        String json = e.getJson();
        Map<String, Object> map = JsonHelper.parseMap(json);
        e.setData(map);
        e.setJson(null);
    }

    @PostMapping
    @Override
    public Long create(@RequestBody DataServiceEntity entity) {
        LOGGER.info("创建 DataService");

        String url = entity.getUrlDir().replaceAll("\\.", "_"); // 不能加 . 否则 URL 解析错误
        entity.setUrlDir(url);
        entity.setUrlDir(entity.getTableName());
//        LOGGER.info("" + entity.getDatasourceId());
//        LOGGER.info(DataServiceAdminService.DAO.toString());

        try (Connection conn = getConnection()) {
            Long dsId = entity.getDatasourceId();
            DataServiceEntity repeatUrlDir = getRepeatUrlDir(conn, dsId, url);

            if (repeatUrlDir != null) {
                // 已经有重复的
                String maxId = getMaxId(conn, dsId, url);
                String dig;

                if (maxId != null) {
                    dig = RegExpUtils.regMatch("\\d+$", maxId);
                    int i = Integer.parseInt(dig);
                    dig = String.valueOf(++i);
                } else
                    dig = "1";

                entity.setUrlDir(entity.getUrlDir() + "_" + dig);
            }

            Long newlyId = (Long) JdbcHelper.createBean(conn, entity, getDataServiceTableName());
            dataService.init(); // 重新加载配置
            entity.setId(newlyId);

            return entity.getId();
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e);
        }
    }

    private DataServiceEntity getRepeatUrlDir(Connection conn, Long dsId, String url) {
        String sql;

        if (dsId == null) {
            sql = "SELECT id FROM ${tableName} WHERE urlDir = ? LIMIT 1";
            return JdbcHelper.queryAsBean(DataServiceEntity.class, conn, getDataServiceTableName(), sql, url);
        } else {
            sql = "SELECT id FROM ${tableName} WHERE urlDir = ? AND datasourceId = ? LIMIT 1";
            return JdbcHelper.queryAsBean(DataServiceEntity.class, conn, getDataServiceTableName(), sql, url, dsId);
        }
    }

    private String getMaxId(Connection conn, Long dsId, String url) {
        String sql;

        if (dsId == null) {
            sql = "SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') ORDER BY urlDir DESC LIMIT 1";
            return JdbcHelper.queryOne(conn, sql, String.class, url);
        } else {
            sql = "SELECT urlDir FROM ${tableName} WHERE urlDir REGEXP CONCAT(?, '_[0-9]+$') AND datasourceId = ? ORDER BY urlDir DESC LIMIT 1";
            return JdbcHelper.queryOne(conn, sql, String.class, url, dsId);
        }
    }

    @PutMapping
    @Override
    public Boolean update(@RequestBody DataServiceEntity entity) {
        try (Connection conn = getConnection()) {
            JdbcHelper.updateBean(conn, entity, getDataServiceTableName());
            dataService.init();// 重新加载配置

            return true;
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    @Override
    public Boolean delete(@PathVariable Long id) {
        LOGGER.info("删除配置 {0}", id);
        DataServiceEntity dataServiceTable = new DataServiceEntity();
        dataServiceTable.setId(id);

        try (Connection conn = getConnection()) {
            return JdbcHelper.delete(conn, dataServiceTable, getDataServiceTableName());
        } catch (SQLException e) {
            LOGGER.warning(e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{datasourceId}/get_databases")
    @ControllerMethod("查询数据库所有的库名")
    public List<String> getDatabases(@PathVariable Long datasourceId) throws SQLException {
        LOGGER.info("查询数据库所有的库名 {0}", datasourceId);
        List<String> databases;

        try (Connection conn = getConnection()) {
            DataSourceInfo info = DataServiceUtils.getDataSourceInfoById(conn, getDataSourceTableName(), datasourceId);

            if (info.getCrossDb() == null || !info.getCrossDb())
                throw new NullPointerException("不是跨库的数据库连接");

            try (Connection conn2 = DataServiceUtils.getConnection(info)) {
                databases = DataBaseMetaHelper.getDatabase(conn2);
            }
        }

        return databases;
    }

    /**
     * 单数据源返回数据源下的表名和表注释
     *
     * @param start
     * @param limit
     * @param tablename 搜索的关键字
     * @return
     * @throws SQLException
     */
    @GetMapping("/getAllTables")
    public PageResult<Map<String, Object>> getAllTables(Integer start, Integer limit, String tablename, String dbName) throws SQLException {
        LOGGER.info("查询表名和表注释");

        Connection conn = JdbcConnection.getConnection();
        return getTableAndComment(conn, start, limit, tablename, dbName);
    }

    /**
     * 指定数据源返回数据源下的表名和表注释
     *
     * @param dataSourceId
     * @param start
     * @param limit
     * @param tablename    搜索的关键字
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @GetMapping("/{dataSourceId}/getAllTables")
    public PageResult<Map<String, Object>> getTableAndComment(@PathVariable Long dataSourceId, Integer start, Integer limit, String tablename, String dbName)
            throws ClassNotFoundException, SQLException {
        LOGGER.info("查询表名和表注释");

        if (start == null)
            start = 0;
        if (limit == null)
            limit = 99;

        return getTableAndComment(DataServiceUtils.getConnByDataSourceInfo(getConnection(), getDataSourceTableName(), dataSourceId), start, limit, tablename, dbName);
    }

    /**
     * 获取所有字段
     *
     * @param dataSourceId
     * @param tableName
     * @param dbName
     * @return
     * @throws SQLException
     */
    @GetMapping("/{dataSourceId}/getFields/{tableName}")
    public List<Map<String, String>> getFields(@PathVariable Long dataSourceId, @PathVariable String tableName, String dbName) throws SQLException {
        LOGGER.info("获取所有字段:" + tableName + " 数据库：" + dbName);

        try (Connection conn = getConnection(); Connection conn2 = DataServiceUtils.getConnByDataSourceInfo(conn, getDataSourceTableName(), dataSourceId)) {

            return DataBaseMetaHelper.getColumnComment(conn2, tableName, dbName);
        }
    }

    /**
     * 返回数据源下的表名和表注释，支持分页和表名搜索
     *
     * @param _conn
     * @param start
     * @param limit
     * @param tablename 搜索的关键字
     * @return
     * @throws SQLException
     */
    private static PageResult<Map<String, Object>> getTableAndComment(Connection _conn, Integer start, Integer limit, String tablename, String dbName)
            throws SQLException {
        int total;
        List<Map<String, Object>> list = null;

        try (Connection conn = _conn) {
            List<String> allTableName = DataBaseMetaHelper.getAllTableName(conn, dbName);

            // 有可能出现配置表本身，删除
            allTableName.remove("adp_data_service");

            if (StringUtils.hasLength(tablename)) // 搜索关键字
                allTableName = allTableName.stream().filter(item -> item.contains(tablename)).collect(Collectors.toList());

            total = allTableName.size();

            if (total > 0) {
//                List<String> subList = allTableName.subList(start, limit); // 有坑 会返回空 List
                List<String> subList = new ArrayList<>();

                for (int i = start; i < (start + limit); i++) {
                    if (i < total)
                        subList.add(allTableName.get(i));
                }

                list = DataBaseMetaHelper.getTableCommentWithAnnotateAsList(conn, subList, dbName);
            }
        }

        PageResult<Map<String, Object>> result = new PageResult<>();

        if (list != null)
            result.addAll(list);

        // 排序
        Comparator<Map<String, Object>> byName = Comparator.comparing(o -> o.get("tableName").toString());
        result.sort(byName);
        result.setTotalCount(total);

        return result;
    }
}
