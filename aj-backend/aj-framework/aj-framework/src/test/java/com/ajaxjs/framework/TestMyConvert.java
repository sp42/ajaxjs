package com.ajaxjs.framework;

import lombok.Data;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestMyConvert {
    @Data
    public static class A implements IBaseModel {
        private Integer a;
    }

    @Test
    public void test() {
        String json = "[{a:1, b:2}]";
        Object o = MyConvert.getConvertValue().to(json, List.class);
        System.out.println(o);

        json = "{a:1, b:2}";
        Map map = (Map) MyConvert.getConvertValue().to(json, Map.class);
        System.out.println(map);

        o = MyConvert.getConvertValue().to(json, A.class);
        System.out.println(o);

        Object o1 = MyConvert.getConvertValue().to(map, A.class);
        System.out.println(o1);
    }
}
