package com.ajaxjs.user.role;

import java.util.*;

public class RoleUtil {
	/**
	 * 分析这个数是不是质数
	 * 
	 * @param num
	 */
	public static boolean isZhishu(int num) {
		switch (num) {
		case 1:
		case 2:
		case 3:
			return true;
		}

		int temp = 0;
		for (int i = 2; i < num / 2 + 1; i++) {
			if (num % i == 0) {
				temp++;
				break;
			}
		}

		if (temp != 0)
			return false;

		return true;
	}

	/**
	 * 得到一个数所有的因数
	 * 
	 * @param num
	 * @return
	 */
	public static List<Integer> zhengChu(int num) {
		List<Integer> integers = new ArrayList<>();

		for (int i = 2; i < num / 2; i++) {
			if (num % i == 0)
				integers.add(i);
		}

		return integers;
	}

	/**
	 * 正式求解
	 * 
	 * @param num
	 * @param data
	 * @return
	 */
	public static Set<Integer> getSingleKeyLock(int num, Set<Integer> data) {
		if (data == null)
			data = new HashSet<>();

		if (isZhishu(num)) {
			data.add(num);
		} else {
			List<Integer> temp = zhengChu(num);
			for (Integer integer : temp)
				getSingleKeyLock(integer, data);
		}

		return data;
	}

	public static Set<Integer> getSingleKeyLock(int num) {
		return getSingleKeyLock(num, null);
	}

	/**
	 * 求 1 到 n 所有质数
	 * 
	 * @param n
	 * @return
	 */
	private static int[] _getPrimeNumber(int n) {
		int[] priArr = new int[n];

		// 质数为大于1的自然数, 故i从2开始
		for (int i = 2; i < n; i++) {
			// isPrime作为当前这个数是否为质数的标记位
			boolean isPrime = true;

			for (int j = 2; j < i; j++) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
			}

			if (isPrime)
				priArr[i] = i;
		}

		return priArr;
	}

	/**
	 * 求 1 到 n 所有质数
	 * 
	 * @param n
	 * @return
	 */
	public static Integer[] getPrimeNumber(int n) {
		int[] arr = _getPrimeNumber(n);
		List<Integer> list = new ArrayList<>();

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != 0)
				list.add(arr[i]);
		}

		return list.toArray(new Integer[list.size()]);
	}

	static int Rmax = 4; // 权限值的最大值

	public static int getR(int Ki, int Lj) {
		int Rij = 0, Temp = Lj;

		while (true) {
			if (Rij == Rmax)
				break;

			if ((Temp % Ki) == 0) { // isInt() 判断是否整数
				Rij++;
				Temp = Temp / Ki;
			} else
				break;
		}

		return Rij;
	}

}
