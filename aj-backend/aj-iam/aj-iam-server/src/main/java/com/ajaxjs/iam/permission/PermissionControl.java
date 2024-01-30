package com.ajaxjs.iam.permission;

public class PermissionControl {
    /**
     * 检查是否有权限
     * 这个Java函数用于检查一个权限值是否具有某个权限位的权限。
     * 函数通过将权限值进行右移操作来获取权限位的值，
     * 然后通过与1进行按位与操作来判断该位是否为1，最终返回结果。
     *
     * @param num      权限值
     * @param position 权限位
     * @return true = 有权限，反之无
     */
    public static boolean check(long num, int position) {
        num = num >>> position;
        return (num & 1) == 1;
    }

    /**
     * 设置权限
     *
     * @param num      权限值
     * @param position 权限位
     * @param v        true = 有权限，反之无
     * @return 权限值
     */
    public static long set(long num, int position, boolean v) {
        boolean old = check(num, position);

        if (v) {// 期望改为无权限
            if (!old) // 原来有权限
                num = num + (1L << position);// 将第 pos 位设置为 1

        } else {// 期望改为有权限
            if (old) // 原来无权限
                num = num - (1L << position);// 将第 pos 位设置为 0
        }

        return num;
    }
}
