package com.ajaxjs.util.reflect;


import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class TestClazz {
    @Test
    public void testGetClassByName() {
        Class<?> actual = Clazz.getClassByName("java.lang.String");
        Assert.assertEquals(String.class, actual);
    }

    @Test
    public void testGetClassByName_whenClassNotFound() {
        Class<?> actual = Clazz.getClassByName("com.example.NotFoundClass");
        Assert.assertNull(actual);
    }

    @Test
    public void testGetClassByName_whenClassFoundWithGenerics() {
        Class<?> actual = Clazz.getClassByName("java.util.ArrayList");
        Assert.assertEquals(ArrayList.class, actual);
    }

    @Test
    public void testGetClassByInterface() {
        Class<?> actual = Clazz.getClassByInterface(List.class);
        Assert.assertEquals(List.class, actual);
    }

    @Test
    public void testGetDeclaredInterface() {
        // Given
        Class<?> clz = TestClass.class;
        Type[] interfaces = {List.class, Comparable.class};
        List<Class<?>> expected = Arrays.asList(Comparable.class, List.class);

        // When
        Class<?>[] actual = Clazz.getDeclaredInterface(clz);

        // Then
//        List<Class<?>> actualInterfaces = Arrays.stream(actual)
//                .map(Class::cast)
//                .collect(Collectors.toList());
//        Assert.assertEquals(expected, actualInterfaces);
    }

    @Test
    public void testEachFields() {
        // Given
        TestClass testClass = new TestClass();
        BiConsumer<String, Object> fn = (fieldName, fieldValue) -> {
            // Do something
        };

        // When
        Clazz.eachFields(testClass, fn);

        // Then
        // Assertions
    }

    @Test
    public void testEachFields2() {
        // Given
        Class<?> clz = TestClass.class;
        BiConsumer<String, Field> fn = (fieldName, field) -> {
            // Do something
        };

        // When
        Clazz.eachFields2(clz, fn);

        // Then
        // Assertions
    }

    @Test
    public void testEachField() {
        // Given
        TestClass testClass = new TestClass();
        Clazz.EachFieldArg fn = (key, value, property) -> {
            // Do something
        };

        // When
        Clazz.eachField(testClass, fn);

        // Then
        // Assertions
    }
}