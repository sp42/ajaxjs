package com.ajaxjs.developertools.tools.mysql;

import com.ajaxjs.developertools.tools.mysql.tools.BasicFormatterImpl;
import org.junit.Test;

public class TestSqlFormat {
    @Test
    public void test() {
        System.out.println(new BasicFormatterImpl()
                .format("SELECT aa,bb,cc,dd from ta1,(select ee,ff,gg from ta2 where ee=ff) ta3 where aa=bb and cc=dd group by dd order by createtime desc limit 3 "));
    }
}
