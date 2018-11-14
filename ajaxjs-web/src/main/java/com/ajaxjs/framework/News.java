package com.ajaxjs.framework;

import java.math.BigDecimal;

/**
 * 测试用
 * 
 * @author Frank Cheung
 *
 */
public class News extends BaseModel {

	private static final long serialVersionUID = 438665753522656855L;

	private BigDecimal price;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
