package com.ajaxjs.data;

import com.ajaxjs.data.util.SqlInjectionAnalyzer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSqlInject {
    @Test
    public void test() {
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where id in (select id from other)"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where 2=2.0 or 2 != 4"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where 1!=2.0"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where id=floor(2.0)"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where not true"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where 1 or id > 0"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where 'tom' or id > 0"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where '-2.3' "));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where 2 "));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where (3+2) "));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where  -1 IS TRUE"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where 'hello' is null "));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where '2022-10-31' and id > 0"));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where id > 0 or 1!=2.0 "));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device where id > 0 or 1 in (1,3,4) "));
        assertFalse(SqlInjectionAnalyzer.check("select * from dc_device UNION select name from other"));
        assertTrue(SqlInjectionAnalyzer.check("WITH SUB1 AS (SELECT user FROM t1) SELECT * FROM T2 WHERE id > 123 "));

        boolean check = SqlInjectionAnalyzer.check("SELECT * FROM mytable WHERE id = ;DROP TABLE mytable;");
        System.out.println(check);
    }

}
