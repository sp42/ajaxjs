package com.ajaxjs.shop;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.service.Pay;

public class TestPayment {
	@BeforeClass
	public static void init() {
		ConfigService.load("D:\\project\\leidong\\WebContent\\META-INF\\site_config.json");
	}

	@Test
	public void testWxPc() {
		OrderInfo order = new OrderInfo();
		order.setId(1L);
		order.setOrderNo("202011122220332");
		order.setTotalPrice(new BigDecimal("0.01"));
		
		Pay.pcUnifiedOrder(order);
	}
}
