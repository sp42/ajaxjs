package com.ajaxjs.framework.spring;

import com.ajaxjs.framework.spring.scheduled.ScheduleHandler;
import com.ajaxjs.util.DateUtil;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class TestScheduleHandler {
    @Test
    public void testIsValidExpress() {
        //正确的
        String cron1 = "0 0 10 * * ?";
        System.out.println("cron1=" + ScheduleHandler.isValidExpression(cron1));

        // 错误的
        String cron2 = "0 0 10 * 123 ?";
        System.out.println("cron2=" + ScheduleHandler.isValidExpression(cron2));
    }

    @Test
    public void testGetNextDate() {
        // https://yemon.blog.csdn.net/article/details/109250567
        Date now = new Date();
        List<Date> points = ScheduleHandler.calNextPoint("0 0 10 * * ?", now, 5);

        for (Date point : points)
            System.out.println(DateUtil.formatDate(point));


        System.out.println("-----");
        List<Date> points2 = ScheduleHandler.calNextPoint("0 0 0/2 * * ?", now, 6);

        for (Date date : points2)
            System.out.println(DateUtil.formatDate(date));
    }

}
