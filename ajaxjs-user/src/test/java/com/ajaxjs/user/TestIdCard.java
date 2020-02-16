package com.ajaxjs.user;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ajaxjs.user.idcard.GetInfo;
import com.ajaxjs.user.idcard.IdcardInfoExtractor;

public class TestIdCard {
	@Test
	public void testGetInfo() {
		String idcard15 = "142431199001145";// 15位
		String idcard18 = "33072619901128272X";// 18位
		GetInfo iv = new GetInfo();
		assertNotNull(iv.isValidatedAllIdcard(idcard15));
		assertNotNull(iv.isValidatedAllIdcard(idcard18));
	}

	@Test
	public void testIdcardInfoExtractor() {
		String idcard = "440105198309060315";
		IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);
		assertNotNull(ie.toString());
	}
}
