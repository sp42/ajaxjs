package com.ajaxjs.iam.user.common;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestUserUtils {
    @Test
    public void testAddressResolution() {
        List<Map<String, String>> table = UserUtils.addressResolution("浙江省杭州市拱墅区湖墅南路湖墅新村4幢");
        System.out.println(table);
        System.out.println(table.get(0).get("province"));
        System.out.println(table.get(0).get("city"));
        System.out.println(table.get(0).get("county"));
        System.out.println(table.get(0).get("town"));
        System.out.println(table.get(0).get("village"));
    }
}
