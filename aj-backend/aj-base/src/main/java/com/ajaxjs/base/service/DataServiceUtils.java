package com.ajaxjs.base.service;


import com.ajaxjs.base.model.DataServiceConstant;
import com.ajaxjs.base.model.DataSourceInfo;
import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.data.jdbc_helper.JdbcReader;
import com.ajaxjs.framework.spring.DiContextUtil;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 工具类，主要是数据源的工具方法
 *
 * @author Frank Cheung sp42@qq.com
 */
@Slf4j
public class DataServiceUtils {
    public static Connection initDb() {
        return new JdbcConn().getConnection(Objects.requireNonNull(DiContextUtil.getBean(DataSource.class)));
    }

    /**
     * 通过 JDBCHelper 实现 DAO
     */
    public static DataSourceInfo getDataSourceInfoById(Connection conn, String tableName, Long id) {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);

        JdbcReader reader = new JdbcReader();
        reader.setConn(conn);

        CRUD<DataSourceInfo> crud = new CRUD<>();
        crud.setReader(reader);

        return crud.setBeanClz(DataSourceInfo.class).setSql(sql).setOrderedParams(new Object[]{id}).infoBean();
    }

//	/**
//	 * 根据数据源 id 获取数据源（可能会太慢）
//	 *
//	 * @param datasourceId
//	 * @return
//	 */
//	public static Connection getConnectionByDatasourceId(long datasourceId) {
//		DataSourceInfo myds = mulitDataSource.get(datasourceId);
//		DataSource ds;
//
//		if (myds != null) {
//			LOGGER.info("Get DS from Cache!");
//			ds = myds.getInstance();
//		} else {
//			myds = JdbcHelper.queryAsBean(DataSourceInfo.class, null, "SELECT * FROM WHERE id = ?", datasourceId);
//			LOGGER.info("Created MyDs " + myds);
//
//			if (myds != null)
//				ds = DataSerivceUtils.getDataSourceByDataSourceInfo(myds);
//			else
//				return null;
//		}
//
//		return JdbcConnection.getConnection(ds);
//	}

    /**
     * 通过 Tomcat JDBC Pool 获取 DataSource
     */
    public static DataSource getDataSourceByDataSourceInfo(DataSourceInfo info) {
        return JdbcConn.setupJdbcPool(getDbDriver(info), info.getUrl(), info.getUsername(), info.getPassword());
    }

    /**
     * 通过 Tomcat JDBC Pool 获取 Connection
     *
     * @return 数据库连接对象
     */
    public static Connection getConnectionByDataSourceInfo(DataSourceInfo info) {
        return new JdbcConn().getConnection(getDataSourceByDataSourceInfo(info));
    }

    /**
     * 根据数据源记录之 id，返回连接字符串、用户密码等信息，进行数据库连接
     */
    public static Connection getConnByDataSourceInfo(Connection conn, String tableName, Long id) {
        DataSourceInfo info = getDataSourceInfoById(conn, tableName, id);

        return getConnectionByDataSourceInfo(info);
    }

    /**
     * 普通创建数据库连接（没有池化的）
     *
     * @return 数据库连接对象
     */
    public static Connection getConnection(DataSourceInfo info) {
        try {
            Class.forName(getDbDriver(info));
        } catch (ClassNotFoundException e) {
            log.warn("Err:", e);
        }

        try {
            return DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
        } catch (SQLException e) {
            log.warn("Err:", e);
            return null;
        }
    }

    /**
     * 返回适合数据库的驱动名称
     */
    public static String getDbDriver(DataSourceInfo info) {
        return getDbDriver(info.getType());
    }

    /**
     * 返回适合数据库的驱动名称
     *
     * @return 数据库的驱动名称
     */
    public static String getDbDriver(DataServiceConstant.DatabaseType type) {
        if (Objects.requireNonNull(type) == DataServiceConstant.DatabaseType.MY_SQL) {
            return "com.mysql.cj.jdbc.Driver";
        }

        return null;
    }
}
