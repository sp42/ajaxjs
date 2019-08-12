package com.ajaxjs.user.idcard;

import org.junit.Test;

public class TestIdCard {
	@Test
	public void testGetInfo() {
		String idcard15 = "142431199001145";// 15位
		String idcard18 = "33072619901128272X";// 18位
		GetInfo iv = new GetInfo();
		System.out.println(iv.isValidatedAllIdcard(idcard15));
		System.out.println(iv.isValidatedAllIdcard(idcard18));
	}

	@Test
	public void testIdcardInfoExtractor() {
		String idcard = "440105198309060315";
		IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);
		System.out.println(ie.toString());
	}
}
