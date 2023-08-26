package com.ajaxjs.developertools.api;

import com.ajaxjs.developertools.tools.CalculateRows;
import com.ajaxjs.developertools.tools.mysql.SqlFormatter;
import com.ajaxjs.framework.spring.filter.dbconnection.IgnoreDataBaseConnect;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/toolkit")
public class ToolkitController {
    @PostMapping("format_sql")
    @IgnoreDataBaseConnect
    public String formatSQL(@RequestParam String sql) {
        return new SqlFormatter().format(sql);
    }

    @PostMapping("/calculate_rows")
    @IgnoreDataBaseConnect
    public CalculateRows calculateRows(@RequestParam String dir) {
        CalculateRows info = new CalculateRows();
        info.treeFile(new File(dir), "java");

        return info;
    }
}
