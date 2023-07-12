package com.ajaxjs.utils.map;

import com.ajaxjs.util.map.JsonHelper;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class TestJsonUtils {
    @SuppressWarnings("unchecked")
    @Test
    public void testJson2Map() {
        Object obj = JsonUtils.json2Map("{x1:true, x2:2000, x3:\"1:hello world\",a:{b:{c:{d:{e:[1,\"j\",3,4,5,6]}}}}}");
        assertTrue(obj instanceof Map);
        Map<String, Object> map = (Map<String, Object>) obj;
        assertTrue(map.get("a") instanceof Map);
        assertEquals(true, map.get("x1"));
        assertEquals(2000, map.get("x2"));
        assertEquals("1:helloworld", map.get("x3")); // remove space, that's wrong.
        assertNotNull(obj);
    }
}
