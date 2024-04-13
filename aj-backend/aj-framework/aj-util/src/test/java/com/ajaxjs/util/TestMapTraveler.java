package com.ajaxjs.util;

import com.ajaxjs.util.map_traveler.MapUtils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestMapTraveler {
    @Test
    public void testFlatMap() {
        // 构建一个多层嵌套的 Map 作为测试输入
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("key1", "value1");
        nestedMap.put("key2", 123);
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("innerKey1", "innerValue1");
        innerMap.put("innerKey2", "innerValue2");
        nestedMap.put("nestedMap", innerMap);

        // 执行扁平化操作
        Map<String, Object> flattenedMap = MapUtils.flatMap(nestedMap);

        // 构建预期的扁平化结果
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", 123);
        expectedMap.put("nestedMap_innerKey1", "innerValue1");
        expectedMap.put("nestedMap_innerKey2", "innerValue2");

        // 断言结果是否如预期
        assertEquals(expectedMap.size(), flattenedMap.size());
        assertTrue(flattenedMap.containsKey("nestedMap_innerKey1"));
        assertTrue(flattenedMap.containsKey("nestedMap_innerKey2"));
        assertEquals("innerValue1", flattenedMap.get("nestedMap_innerKey1"));
        assertEquals("innerValue2", flattenedMap.get("nestedMap_innerKey2"));
    }

    @Test
    public void testBuildPath() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("name", "Map1");
        list.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("name", "Map2");
        list.add(map2);

        // 调用待测试的 buildPath 方法
        MapUtils.buildPath(list, "id", "path", "level", true);

        // 验证结果
        assertEquals("/1", list.get(0).get("path"));
        assertEquals("/2", list.get(1).get("path"));
        assertEquals(0, list.get(0).get("level"));
        assertEquals(0, list.get(1).get("level"));
        assertEquals("Map1", list.get(0).get("name"));
        assertEquals("Map2", list.get(1).get("name"));

        // 如果 saveSupers 为 true，验证 supers 是否正确设置
        String expectedSupers1 = null; // 第一个map没有superMap，预期为空
        String expectedSupers2 = "/1:Map1,"; // 第二个map的superMap是第一个map

        assertEquals(expectedSupers1, list.get(0).get("supers"));
        assertEquals(expectedSupers2, list.get(1).get("supers"));
    }

    @Test
    public void testFindByPath() {
        // 创建测试数据
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> level1 = new HashMap<>();
        level1.put("key1", "value1");
        Map<String, Object> level2 = new HashMap<>();
        level2.put("key2", "value2");
        level1.put("nestedMap", level2);
        list.add(level1);

        // 测试能够找到确切路径的情况
        assertEquals(level2, MapUtils.findByPath("nestedMap/key2", list));

        // 测试路径不存在的情况
        assertNull(MapUtils.findByPath("/nonExistingKey", list));

        // 测试路径字符串开头和结尾有额外的'/'的情况
        assertEquals(level2, MapUtils.findByPath("/nestedMap//key2/", list));

        // 测试空路径的情况
        assertNull(MapUtils.findByPath("", list));

        // 测试null的情况
        assertNull(MapUtils.findByPath(null, list));

        // 测试列表为空的情况
        assertNull(MapUtils.findByPath("/nestedMap/key2", new ArrayList<>()));
    }
}
