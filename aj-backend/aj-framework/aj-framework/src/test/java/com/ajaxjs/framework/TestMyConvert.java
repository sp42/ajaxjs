package com.ajaxjs.framework;

import com.ajaxjs.util.convert.ConvertComplexValue;
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
        MyConvert.set();

        String json = "[{a:1, b:2}]";
        Object o = ConvertComplexValue.INSTANCE.convert(json, List.class);
        System.out.println(o);

        json = "{a:1, b:2}";
        Map map = (Map) ConvertComplexValue.INSTANCE.convert(json, Map.class);
        System.out.println(map);

        o = ConvertComplexValue.INSTANCE.convert(json, A.class);
        System.out.println(o);

        Object o1 = ConvertComplexValue.INSTANCE.convert(map, A.class);
        System.out.println(o1);
    }
}
