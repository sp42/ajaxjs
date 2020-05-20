package com.ajaxjs.shop;

public class ShopHelper implements ShopConstant {
	/**
	 * 看是否有这个状态
	 * 
	 * @param total 总的状态值
	 * @param kind  具体那个状态
	 * @return true 表示有那个状态
	 */
	public static boolean hasState(int total, int kind) {
		return (total & kind) == kind;
	}
}
