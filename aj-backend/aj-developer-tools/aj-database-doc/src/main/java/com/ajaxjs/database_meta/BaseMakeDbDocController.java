package com.ajaxjs.database_meta;
//
//import com.ajaxjs.data.jdbc_helper.JdbcConn;
//import com.ajaxjs.database_meta.model.DataSourceInfo;
//import com.ajaxjs.framework.spring.DiContextUtil;
//import com.ajaxjs.framework.spring.response.ResponseResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// * 生成数据库信息的 JSON，用于显示数据库文档
// *
// * @author Frank Cheung sp42@qq.com
// */
public class BaseMakeDbDocController {
//    /**
//     * 使用了缓存，就不用保持到磁盘
//     */
//    private String jsonPath = "D:\\code\\ajaxjs\\aj-framework\\aj-ui-widget\\database-doc\\";
//
//    /**
//     * 生成配置 JSON。这个操作会比较久。这是给多数据源的时候用的。
//     *
//     * @param ds 数据源信息
//     * @return database-doc 配置 JSON
//     * @throws SQLException 异常
//     */
//    @PostMapping
//    public Boolean genJsonFile(@RequestBody DataSourceInfo ds) throws SQLException {
//        try (Connection conn = JdbcConn.getMySqlConnection(ds.getUrl(), ds.getUsername(), ds.getPassword())) {
////			DataBaseQuery.saveToDiskJson(conn, getJsonPath() + "json.js");
//            DB_DOC_JSON = "DOC_DATA = " + DataBaseQuery.getDoc(conn, null);
//
//            return true;
//        }
//    }
//
//    /**
//     * JSON 缓存
//     */
//    public static String DB_DOC_JSON;
//
//    @GetMapping
//    public String getJson() {
//        if (DB_DOC_JSON == null) // 第一次启动，不管是不是多数据源，先加载当前数据源的
//            getSingleDataSource();
//
//        return ResponseResult.PLAIN_TEXT_OUTPUT + DB_DOC_JSON;
//    }
//
//    void getSingleDataSource() {
//        DataSource ds = DiContextUtil.getBean(DataSource.class);
//
//        try {
//            assert ds != null;
//            try (Connection conn = JdbcConn.getConnection(ds)) {
//                assert conn != null;
//                DB_DOC_JSON = "DOC_DATA = " + DataBaseQuery.getDoc(conn, conn.getCatalog());
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getJsonPath() {
//        return jsonPath;
//    }
//
//    public void setJsonPath(String jsonPath) {
//        this.jsonPath = jsonPath;
//    }
//
}
