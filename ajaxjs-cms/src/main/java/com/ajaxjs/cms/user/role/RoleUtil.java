package com.ajaxjs.cms.user.role;

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
}
