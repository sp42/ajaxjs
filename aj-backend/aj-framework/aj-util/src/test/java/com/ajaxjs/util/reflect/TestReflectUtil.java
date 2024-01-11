package com.ajaxjs.util.reflect;

import com.ajaxjs.util.regexp.RegExpUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;


import static org.junit.Assert.*;

public class TestReflectUtil {
    public static class Foo {
        public Foo() {
        }

        public Foo(String str, String str2) {
        }

        public void Bar() {

        }

        public void CC(String cc) {

        }

        public String Bar2() {
            return "bar2";
        }

        public String Bar3(String arg) {
            return arg;
        }
    }

    @Test
    public void testNewInstance() {
        assertNotNull(NewInstance.newInstance(Foo.class));
        assertNotNull(NewInstance.newInstance(Foo.class, "a", "b"));
        assertNotNull(NewInstance.newInstance(Objects.requireNonNull(NewInstance.getConstructor(Foo.class))));
        assertNotNull(NewInstance.newInstance(Objects.requireNonNull(NewInstance.getConstructor(Foo.class, String.class, String.class)), "a", "b"));
        assertNotNull(NewInstance.newInstance("com.ajaxjs.util.reflect.TestReflectUtil"));
        assertNotNull(Clazz.getClassByName("com.ajaxjs.util.reflect.TestReflectUtil"));

        Class<?>[] cs = Clazz.getDeclaredInterface(ArrayList.class);
        assertNotNull(cs);
    }

    static class Foo2 {
        public void m1() {
        }

        public void m1(String arg) {
        }
    }

    static class Bar extends Foo {
        public void m2() {
        }
    }

    @Test
    public void testGetMethod() {
        assertNotNull(Methods.getMethod(new Foo(), "m1"));// 按实际对象
        assertNotNull(Methods.getMethod(Foo2.class, "m1"));// 按类引用
        assertNotNull(Methods.getMethod(Foo2.class, "m1", String.class)); // 按参数类型
        assertNotNull(Methods.getMethod(Foo2.class, "m1", "foo"));// 按实际参数
        assertNotNull(Methods.getMethod(Bar.class, "m1"));
        assertNotNull(Methods.getMethod(Bar.class, "m1", String.class));
        assertNotNull(Methods.getMethod(Bar.class, "m2"));
    }

    static class Foo1 {
        public void foo(Foo1 a) {

        }
    }

    static class Bar2 extends Foo1 {

    }

    @Test
    public void testGetMethodByUpCastingSearch() {
        assertNull(Methods.getMethod(Foo1.class, "foo", new Bar2())); // 找不到
        assertNotNull(Methods.getMethodByUpCastingSearch(Foo1.class, "foo", new Bar2())); // 找到了
    }

    public static class A {
        public String foo(A a) {
            return "A.foo";
        }

        public String bar(C c) {
            return "A.bar";
        }
    }

    public static class B extends A {
    }

    public static interface C {
    }

    public static class D implements C {
    }

    @Test
    public void testDeclaredMethod() {
        assertNull(Methods.getMethodByUpCastingSearch(A.class, "bar", new D())); // 找不到
        assertNotNull(Methods.getDeclaredMethodByInterface(A.class, "bar", new D()));// 找到了
    }

    public static class Foo3 {
        public void m1() {
        }

        public String m1(String arg) {
            return arg;
        }
    }

    static class Bar3 extends Foo3 {
        public void m2() {
        }
    }

    @Test
    public void testExecuteMethod() {
        assertNull(Methods.executeMethod(new Foo3(), "m1"));
        assertEquals(Methods.executeMethod(new Foo3(), "m1", "foo"), "foo");
        assertNull(Methods.executeMethod(new Bar2(), "m1"));
        assertEquals(Methods.executeMethod(new Bar3(), "m1", "bar"), "bar");
        assertEquals(Methods.executeMethod(new Bar3(), "m1", String.class, "foo"), "foo");
    }

    @Test
    public void testRegMatch() {
        assertEquals(RegExpUtils.regMatch("^a", "abc"), "a");// 匹配结果，只有匹配第一个
        assertEquals(RegExpUtils.regMatch("^a", "abc", 0), "a");// 可指定分组
        assertEquals(RegExpUtils.regMatch("^a(b)", "abc", 1), "b");
    }

}
