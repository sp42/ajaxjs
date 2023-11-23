package com.ajaxjs.database_meta;

import com.ajaxjs.data.util.SnowflakeId;
import com.ajaxjs.database_meta.model.Column;
import com.ajaxjs.database_meta.model.Database;
import com.ajaxjs.database_meta.model.Table;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.ConvertToJson;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.regexp.RegExpUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库信息查询
 *
 * @author frank
 */
public class DataBaseQuery extends BaseMetaQuery {
    public DataBaseQuery(Connection conn) {
        super(conn);
    }

    /**
     * 获取所有库名
     *
     * @return 所有库名
     */
    public String[] getDatabase() {
        List<String> list = getResult("SHOW DATABASES", rs -> {
            try {
                return rs.getString("Database");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }, String.class);

        return list.toArray(new String[0]);
    }

    /**
     * 两级结构，所有库和库下面所有的表名
     *
     * @return 所有库和库下面所有的表名
     */
    public Database[] getDataBaseWithTable() {
        return getDataBaseWithTable(getDatabase());
    }

    static final String[] IGNORE_SYSTEM_TABLE = {"information_schema", "performance_schema", "mysql", "sys"};

    public Database[] getDataBaseWithTable(String[] databases) {
        List<Database> list = new ArrayList<>();
        TableQuery tableQuery = new TableQuery(conn);

        for (String databaseName : databases) {
            // ignore system table
            if (StrUtil.isWordOneOfThem(databaseName, IGNORE_SYSTEM_TABLE)) continue;

            Database database = new Database();
            database.setUuid(String.valueOf(SnowflakeId.get()));
            database.setName(databaseName);
            database.setTables(tableQuery.getAllTableName(databaseName));

            list.add(database);
        }

        return list.toArray(new Database[0]);
    }

    /**
     * 完整的信息，包括 CreateDDL
     *
     * @return 完整的信息，包括 CreateDDL
     */
    public Database[] getDataBaseWithTableFull(String dbName) {
        Database[] databases = getDataBaseWithTable();

        if (StringUtils.hasText(dbName)) {
            Database _database = null;
            for (Database database : databases) {
                if (database.getName().equals(dbName)) {
                    _database = database;
                    break;
                }
            }

            if (_database == null) return null; // 找不到 dbName 的
            else {
                List<Table> full = getDataBaseWithTableFull(_database.getTables(), _database.getName());
                _database.setTableInfo(full);

                return new Database[]{_database};
            }
        } else {
            for (Database database : databases) {
                List<Table> full = getDataBaseWithTableFull(database.getTables(), database.getName());
                database.setTableInfo(full);
            }

            return databases;
        }
    }

    public Database[] getDataBaseWithTableFull() {
        return getDataBaseWithTableFull(null);
    }

    /**
     * 获取表和所有列的信息
     *
     * @param tableNames 表名
     * @param dbName     数据库名，可选
     * @return 所有列的信息
     */
    public List<Table> getDataBaseWithTableFull(List<String> tableNames, String dbName) {
        List<Table> tables = new ArrayList<>();
        boolean hasDbName = StringUtils.hasText(dbName);

        try (Statement stmt = conn.createStatement()) {
            for (String tableName : tableNames) {
                String t = hasDbName ? dbName + "." + tableName : tableName;

                try (ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + tableName)) {
                    String createDDL = null;
                    if (rs.next())
                        createDDL = rs.getString(2);

                    Table table = new Table();
                    tables.add(table);

                    table.setUuid(String.valueOf(SnowflakeId.get()));
                    table.setName(tableName);
                    table.setDdl((createDDL));
                    table.setComment(TableQuery.parse(createDDL));
                    table.setColumns(parseColumns(createDDL));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }

    /**
     * 根据 DDL 语句解析各个列
     *
     * @param ddl DDL 语句
     * @return 列信息
     */
    private List<Column> parseColumns(String ddl) {
        List<Column> list = new ArrayList<>();

        try {
            CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(ddl);

            for (ColumnDefinition col : createTable.getColumnDefinitions()) {
                Column colInfo = new Column();
                list.add(colInfo);

                colInfo.setName(col.getColumnName().replaceAll("`", ""));

                String type = col.getColDataType().toString();
                type = type.replaceAll("\\s+\\(", "("); // remove space

                colInfo.setType(type);
                String regMatch = RegExpUtils.regMatch("\\((\\d+)\\)", type, 1);
                if (StringUtils.hasText(regMatch)) colInfo.setLength(Integer.parseInt(regMatch));

                String ddlItem = col.toString();
                String comment = RegExpUtils.regMatch("COMMENT '(.*)'", ddlItem, 1);
                colInfo.setComment(comment);
                colInfo.setIsRequired(ddlItem.contains("NOT NULL"));
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static String getDoc(Connection conn, String dbName) {
        DataBaseQuery d = new DataBaseQuery(conn);
        Database[] dataBaseWithTable = d.getDataBaseWithTableFull(dbName);

        return ConvertToJson.toJson(dataBaseWithTable);
    }

    public static void saveToDiskJson(Connection conn, String path, String dbName) {
        FileHelper.saveText(path, "DOC_DATA = " + getDoc(conn, dbName));
    }
}
