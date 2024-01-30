package com.ajaxjs.iam.permission;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestPermissionControl {

    @Test
    public void test() {
        long num = 0L;
        num = PermissionControl.set(num, 0, false);//设置[权限项60]为true
        num = PermissionControl.set(num, 1, true);//设置[权限项3]为true
        num = PermissionControl.set(num, 2, true);//设置[权限项3]为true
        num = PermissionControl.set(num, 4, false);//设置[权限项3]为true
//        num = PermissionControl.set(num, 8, true);//设置[权限项3]为true

        System.out.println(num + "-->" + Long.toBinaryString(num));
    }

    public static void main(String[] args) {
        long value = 1230L; // 64-bit long value
        String binaryString = Long.toBinaryString(value);
        System.out.println(binaryString);


        System.out.println("1001001100101100000001011010010".length());
    }

    @Test
    public void testRemoveBit() {
        long num = 1230L;
        System.out.println(Long.toBinaryString(num)); // 输出结果的二进制表示

        long result = PermissionControl.removeBit(num, 2); // 调用 removeBit 函数，去掉右起第三位

        System.out.println(Long.toBinaryString(result)); // 输出结果的二进制表示

        assertEquals("1001100110", Long.toBinaryString(result));
    }

}
