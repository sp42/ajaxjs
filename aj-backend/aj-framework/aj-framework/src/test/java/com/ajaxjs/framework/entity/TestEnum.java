package com.ajaxjs.framework.entity;

import org.junit.Test;

public class TestEnum {
    public static enum Bar {
        A, B
    }

    /**
     * 数字类型的
     */
    public static enum Foo {
        A(1), B(2);


        /**
         * 保存真正的枚举值
         */
        private final int v;

        /**
         * 构造器，必须的
         *
         * @param v 枚举值
         */
        Foo(int v) {
            this.v = v;
        }

        /**
         * 重写获取枚举值
         *
         * @return 枚举值
         */
        @Override
        public String toString() {
            return String.valueOf(v);
        }
    }

    @Test
    public void test() {
        System.out.println(Bar.A);
        System.out.println(Bar.A.toString());
        System.out.println(Foo.A);
    }
}
