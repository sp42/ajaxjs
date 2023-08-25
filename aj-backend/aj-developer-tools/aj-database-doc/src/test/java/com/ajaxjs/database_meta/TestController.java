package com.ajaxjs.database_meta;

import com.ajaxjs.database_meta.model.DataBaseDetail;
import com.ajaxjs.database_meta.model.TableDesc;
import com.ajaxjs.database_meta.model.TableDetailRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/db_meta")
public class TestController extends BaseMakeDbDocController {
    @Autowired
    DataSource ds;

    @GetMapping("/test")
    DataBaseDetail test() {
        try (Connection connection = ds.getConnection()) {
            return DB.detail(connection, "aj_base");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/table_list")
    List<TableDesc> tableList() {
        try (Connection connection = ds.getConnection()) {
            return DB.list(connection, "aj_base");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/table_info/{tableName}")
    TableDetailRes tableInfo(@PathVariable String tableName) {
        try (Connection connection = ds.getConnection()) {
            return DB.detail(connection, "aj_base", tableName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
