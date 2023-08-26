package com.ajaxjs.developertools.api;

import com.ajaxjs.developertools.tools.mysql.MySqlProbe;
import com.ajaxjs.developertools.tools.mysql_meta.model.DataBaseDetail;
import com.ajaxjs.developertools.tools.mysql_meta.model.TableDesc;
import com.ajaxjs.developertools.tools.mysql_meta.model.TableDetailRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * MySQL 探针
 */
@RestController
@RequestMapping("/db_meta")
public class MySqlProbeController {
    @Autowired
    DataSource ds;

    @GetMapping("/test")
    DataBaseDetail test() {
        try (Connection connection = ds.getConnection()) {
            return MySqlProbe.detail(connection, "aj_base");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/table_list")
    List<TableDesc> tableList() {
        try (Connection connection = ds.getConnection()) {
            return MySqlProbe.list(connection, "aj_base");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/table_info/{tableName}")
    TableDetailRes tableInfo(@PathVariable String tableName) {
        try (Connection connection = ds.getConnection()) {
            return MySqlProbe.detail(connection, "aj_base", tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
