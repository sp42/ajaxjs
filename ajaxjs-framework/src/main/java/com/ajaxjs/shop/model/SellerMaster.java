package com.ajaxjs.shop.model;

import com.ajaxjs.user.model.User;

/**
 * 
 * 
 * @author sp42 frank@ajaxjs.com frank@ajaxjs.com
 *
 */
public class SellerMaster extends User {
	private static final long serialVersionUID = -6434409791484443287L;
	
	private int sellerId;

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
}
