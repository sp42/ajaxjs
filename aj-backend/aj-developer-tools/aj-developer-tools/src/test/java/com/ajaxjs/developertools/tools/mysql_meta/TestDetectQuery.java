package com.ajaxjs.developertools.tools.mysql_meta;

import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.developertools.BaseTest;
import com.ajaxjs.developertools.tools.mysql_meta.model.Column;
import com.ajaxjs.developertools.tools.mysql_meta.model.Table;
import com.ajaxjs.util.TestHelper;
import org.junit.Test;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestDetectQuery extends BaseTest {
    @Test
    public void testGetDatabase() {
        Connection connection = JdbcConn.getConnection();
        DataBaseQuery query = new DataBaseQuery(connection);
        String[] database = query.getDatabase();

        System.out.println(Arrays.toString(database));

        TableQuery tableQuery = new TableQuery(connection);
        List<String> tableNames = tableQuery.getAllTableName("aj_base");

        List<Table> dataBaseWithTableFull = query.getDataBaseWithTableFull(tableNames, null);
        System.out.println(dataBaseWithTableFull);
    }

    @Test
    public void testColumnQuery() {
        Connection connection = JdbcConn.getConnection();
        ColumnQuery columnQuery = new ColumnQuery(connection);
        List<Column> article = columnQuery.getColumnComment("article", null);

        System.out.println(article);

        Map<String, List<Column>> list = columnQuery.getColumnComment(Collections.singletonList("article"));
        TestHelper.printJson(list);
    }

    @Test
    public void testMetaQuery() {
        Connection connection = JdbcConn.getConnection();
        MetaQuery metaQuery = new MetaQuery(connection);

        Map<String, String> allVariable = metaQuery.getAllVariable();
        TestHelper.printJson(allVariable);

        String value = metaQuery.getVariable("Value", "SHOW VARIABLES LIKE '%basedir%'");
        System.out.println(value);
    }

    @Test
    public void testTableQuery() {
        Connection connection = JdbcConn.getConnection();
        TableQuery tableQuery = new TableQuery(connection);
        List<String> tableName = tableQuery.getAllTableName(null);
        TestHelper.printJson(tableName);

        String adpDataService = tableQuery.getTableComment("adp_data_service");

        System.out.println(adpDataService);
    }
}
