package com.ajaxjs.util.convert;

import com.ajaxjs.util.TestCaseUserBean;
import lombok.Data;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.ajaxjs.util.convert.EntityConvert.*;
import static org.junit.Assert.*;

public class TestConvert {
    @Test
    public void testBean2Map() {
        TestCaseUserBean user = map2Bean(TestMapTool.MapMock.user, TestCaseUserBean.class, true);
        Map<String, Object> map = bean2Map(user);

        assertNotNull(map);
        assertEquals("Jack", map.get("name"));
        assertEquals("directField", map.get("directField"));
    }

    public static class SimpleBean {
        public int id;
        public String name;
        public boolean active;
    }

    @Test
    public void testSimpleBean2Map_ReturnsMap() {
        SimpleBean bean = new SimpleBean();
        bean.id = 1;
        bean.name = "John";
        bean.active = true;

        Map<String, Object> expected = new HashMap<>();
        expected.put("id", 1);
        expected.put("name", "John");
        expected.put("active", true);

        Map<String, Object> result = bean2MapSimple(bean);
        assertEquals(expected, result);
    }

    @Data
    public static class Student {
        private Long id;
        private String name;
        private String remark;
        private Integer age;
    }

    public static class Teacher {
        public Long id;
        public String name;
        public String remark;
        public Integer age;
    }

    @Test
    public void testBeanToJson() {
        Student student = new Student();
        student.setId(123L);
        student.setName("Tom");
        student.setAge(18);

        String expectedOutput = "{\"age\":18,\"id\":123,\"name\":\"Tom\",\"remark\":null}";
        String output = beanToJson(student);

        assertEquals(expectedOutput, output);

        student.setId(123L);
        student.setName(null);
        student.setAge(18);
        student.setRemark("\"Hello world!\"");

        expectedOutput = "{\"age\":18,\"id\":123,\"name\":null,\"remark\":\"\\\"Hello world!\\\"\"}";
        output = beanToJson(student);

        assertEquals(expectedOutput, output);

        Teacher teacher = new Teacher();
        teacher.id = 123L;
        teacher.name = "Jack";

        expectedOutput = "{\"id\":123,\"name\":\"Jack\",\"remark\":null,\"age\":null}";
        output = beanToJson(teacher);

        assertEquals(expectedOutput, output);
    }



}
