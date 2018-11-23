package com.ajaxjs.cms.controller;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Test {
    public static interface inter<T extends Serializable> {
        public void foo(T s);
    }
    public static class Foo implements inter<Long> {
        @Deprecated
        public void foo(Long s) {
        }
    }

    public static void main(String[] args) {
        Method[] methods = Foo.class.getMethods();
        for (Method m : methods) {
            if (m.getAnnotation(Deprecated.class) != null) {
                System.out.println(m);
            }
        }
    }
}
