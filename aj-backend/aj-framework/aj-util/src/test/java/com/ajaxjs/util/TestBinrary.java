package com.ajaxjs.util;

import static com.ajaxjs.util.binrary.BinaryUtil.BinaryToDecimal;
import static com.ajaxjs.util.binrary.BinaryUtil.DecimalToBinary;

import org.junit.Test;

public class TestBinrary {
	@Test
	public void testBinrayUtil() {
		int num = 3;
		String string = DecimalToBinary(num, new StringBuilder());
		System.out.println(num + "的二进制结果：" + string);

		Integer binaryToDecimal = BinaryToDecimal(string);
		System.out.println(string + "的十进制结果：" + binaryToDecimal);
	}
}
