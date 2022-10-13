package com.ajaxjs.framework.shop.model;

import com.ajaxjs.user.User;

/**
 * 
 * 
 * @author sp42 frank@ajaxjs.com frank@ajaxjs.com
 *
 */
public class SellerMaster extends User {
	private int sellerId;

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
}
