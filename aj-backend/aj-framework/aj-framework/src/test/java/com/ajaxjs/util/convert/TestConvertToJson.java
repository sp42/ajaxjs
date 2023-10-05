package com.ajaxjs.util.convert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestConvertToJson {
    @Test
    public void testToJsonBasicType() {
        Object obj = true;
        String json = JsonHelper.toJson(obj);
        assertEquals("true", json);

        obj = 123;
        json = JsonHelper.toJson(obj);
        assertEquals("123", json);

        obj = 100000000000000001L;
        json = JsonHelper.toJson(obj);
        assertEquals("\"100000000000000001\"", json);

        obj = 99999L;
        json = JsonHelper.toJson(obj);
        assertEquals("99999", json);

        obj = "hello";
        json = JsonHelper.toJson(obj);
        assertEquals("\"hello\"", json);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "John");
        map.put("age", 30);
        json = JsonHelper.toJson(map);
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }

    @Test
    public void testList2Json() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        String json = ConvertToJson.list2Json(list);
        assertEquals("[1, 2, 3]", json);

        List<Long> listLong = new ArrayList<>();
        listLong.add(100L);
        listLong.add(200L);
        listLong.add(300L);
        json = ConvertToJson.list2Json(listLong);

        assertEquals("[100, 200, 300]", json);

        List<String> listStr = new ArrayList<>();
        listStr.add("hello");
        listStr.add("world");
        listStr.add("!");
        json = ConvertToJson.list2Json(listStr);
        assertEquals("[\"hello\", \"world\", \"!\"]", json);

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "John");
        map1.put("age", 30);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "Jane");
        map2.put("age", 25);
        listMap.add(map1);
        listMap.add(map2);
        json = ConvertToJson.list2Json(listMap);
        assertEquals("[{\"name\":\"John\",\"age\":30}, {\"name\":\"Jane\",\"age\":25}]", json);

        /* 这种转不了 */
//        List<Object> listMix = new ArrayList<>();
//        listMix.add("hello");
//        listMix.add(123);
//        listMix.add(true);
//        json = ConvertToJson.list2Json(listMix);
//        assertEquals("[\"hello\", 123, true]", json);

        List<Object> listEmpty = new ArrayList<>();
        json = ConvertToJson.list2Json(listEmpty);
        assertEquals("[]", json);
    }
}
