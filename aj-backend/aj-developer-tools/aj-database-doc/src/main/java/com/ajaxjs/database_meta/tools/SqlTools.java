package com.ajaxjs.database_meta.tools;

import com.ajaxjs.data.CRUD;

import java.util.List;
import java.util.Map;

public class SqlTools {
    public static class ExplainVO {
        public List<Map<String, Object>> list;

        public String cost;

    }

    public ExplainVO runExplainPlan(String sqlText) {
        List<Map<String, Object>> list = CRUD.listMap("EXPLAIN " + sqlText);

        Map<String, Object> info = CRUD.infoMap("SHOW STATUS LIKE 'Last_query_cost'");
        Object cost = info.get("Value");

        ExplainVO vo = new ExplainVO();
        vo.list = list;
        vo.cost = cost.toString();

        return vo;
    }

    public String runExplainPlanJson(String sqlText) {
        return CRUD.queryOne(String.class, "EXPLAIN FORMAT=JSON " + sqlText);
    }

    // Display MySQL processlist

    /**
     * Display MySQL processlist
     *
     * @param isActive
     * @return
     */
    public List<Map<String, Object>> processList(boolean isActive) {
        String sql;

        if (isActive)
            sql = "SELECT * FROM information_schema.processlist LIMIT 5000";
        else
            sql = "SELECT * FROM information_schema.processlist where command!='Sleep' limit 5000";

        return CRUD.listMap(sql);
    }
}
