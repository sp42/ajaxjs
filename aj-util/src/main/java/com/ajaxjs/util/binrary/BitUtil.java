package com.ajaxjs.util.binrary;

public class BitUtil {

	static class Permission {
		private int permission;

		public Permission(int permission) {
			if (permission == 1 || isLog2(permission))
				this.permission = permission;
		}

		public int getPermission() {
			return permission;
		}

		public boolean test(int flag) {
			return testBitMask(this, flag);
		}
	}

	/**
	 * 快速判断一个数是否是2的幂次方
	 * 
	 * @param value
	 * @return
	 */
	static boolean isLog2(int value) {
		if ((value & (value - 1)) == 0)
			return true;
		else
			throw new RuntimeException(value + " 不是 2 的幂次方");
	}

	/**
	 * 测试 8421 码是否包含 v
	 * 
	 * @param v   当前权限值
	 * @param all 同值
	 * @return true=已包含
	 */
	public static boolean testBCD(int v, int all) {
		return (v & all) == v;
	}

	public static boolean testBitMask(Permission p, int all) {
		return (p.getPermission() & all) == p.getPermission();
	}

	class Test {
		Permission read = new Permission(1);
		Permission create = new Permission(2);
		Permission update = new Permission(4);
		Permission delete = new Permission(8);
	}

	/**
	 * 检查是否有权限
	 * 
	 * @return {Boolean} true = 有权限，反之无
	 */
	static boolean check(long num, int pos) {
		num = num >>> pos;
		return (num & 1) == 1;
	}

	/**
	 * 设置权限
	 */
	static long set(long num, int pos, boolean v) {
		boolean old = check(num, pos);

		if (v) {// 期望改为无权限
			if (!old) // 原来有权限
				num = num + (1 << pos);// 将第 pos 位设置为 1

		} else {// 期望改为有权限
			if (old) // 原来无权限
				num = num - (1 << pos);// 将第 pos 位设置为 0
		}

		return num;
	}

	// 取 n 的第 m 位
	public static int getBit(int n, int m) {
		return (n >> (m - 1)) & 1;
	}

	// 将 n 的第 m 位置 1
	public static int setBitToOne(int n, int m) {
		return n | (1 << (m - 1));
	}

	// 将 n 的第 m 位置 0
	public static int setBitToZero(int n, int m) {
		return n & ~(1 << (m - 1));
	}

	public static String toBinaryString(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 32; i > 0; i--)
			s.append(getBit(n, i));

		return s.toString();
	}
}
