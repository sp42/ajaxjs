package com.ajaxjs.database_meta.tools;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.data.jdbc_helper.JdbcConn;
import com.ajaxjs.database_meta.tools.innodb.ResultParser;
import com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection;
import com.ajaxjs.util.convert.MapTool;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class MVC {
    public static Map<String, String> showMap(String id) {
        DataBaseConnection.initDb();
        Map<String, String> map = null;

        try {
            switch (id) {
                case "inno_status_summary":
                    map = new ResultParser().parse("BACKGROUND THREAD");
                    break;
                case "inno_status_file_io":
                    map = new ResultParser().parse("FILE I/O");
                    break;
                case "inno_status_buffer_pool":
                    map = new ResultParser().parse("BUFFER POOL AND MEMORY");
                    break;
                case "innodb_buffer_pool_status":
                    Map<String, Object> _map = CRUD.infoMap("SELECT * FROM information_schema.innodb_buffer_pool_stats");
                    map = MapTool.as(_map, Object::toString);
                    break;
                case "inno_status_ibuf":
                    map = new ResultParser().parse("INSERT BUFFER AND ADAPTIVE HASH INDEX");
                    break;
                case "inno_status_row_operations":
                    map = new ResultParser().parse("ROW OPERATIONS");
                    break;
                case "inno_status_semaphores":
                    map = new ResultParser().parse("SEMAPHORES");
                    break;
                case "inno_status_txs":
                    map = new ResultParser().parse("SEMAPHORES");
                    break;
                case "inno_status_deadlocks":
                    map = new ResultParser().parse("SEMAPHORES");
                    break;

                case "inno_status_log":
                    map = new ResultParser().parse("LOG");
                    break;

            }
        } finally {
            JdbcConn.closeDb();
        }

        return map;
    }

    public static List<Map<String, Object>> showList(String id) {
        DataBaseConnection.initDb();
        List<Map<String, Object>> list;

        try {
            switch (id) {
                case "innodb_metrics":
                    list = CRUD.listMap("SELECT NAME, COUNT, TIME_ELAPSED from information_schema.innodb_metrics ORDER BY NAME");
                    break;
                case "mysql_innodb_mutex":
                    list = CRUD.listMap("SHOW ENGINE INNODB MUTEX");
                    break;
                default:
                    list = CRUD.listMap(id);
            }
        } finally {
            JdbcConn.closeDb();
        }

        return list;
    }

    public static String[] getTh(HttpServletRequest req) {
        String th = req.getParameter("th");

        if (!StringUtils.hasText(th))
            th = Explain.TH.get(req.getParameter("id"));

        return th.split(",");
    }
}
