package com.ajaxjs.util;

import org.junit.Test;

import static com.ajaxjs.util.binrary.BinaryUtil.*;
import static com.ajaxjs.util.binrary.CheckSum.*;

import java.util.List;
import java.util.Map;

public class TestBinrary {
	@Test
	public void testBinrayUtil() {
		int num = 3;
		String string = DecimalToBinary(num, new StringBuilder());
		System.out.println(num + "的二进制结果：" + string);

		Integer binaryToDecimal = BinaryToDecimal(string);
		System.out.println(string + "的十进制结果：" + binaryToDecimal);
	}

	@Test
	public void testHammingCode() {
		byte[] bytes = new byte[] { (byte) Integer.parseInt("101101", 2), };
		System.out.printf("源码：%s%n", toBitStr(bytes));
		System.out.println("=========");

		byte[] hmBytes = encode(bytes);
		Map<Integer, List<Integer>> integerListMap = buildCheckBitTable(bytes);

		integerListMap.keySet().stream().sorted().forEach((key) -> {
			int bitValue;
			List<Integer> dataBitValues = integerListMap.get(key);
			bitValue = dataBitValues.get(0);
			for (int i = 1; i < dataBitValues.size(); i++)
				bitValue = bitValue ^ dataBitValues.get(i);

			System.out.printf("海明校验位%d,校验海明位集：%s,%d%n", key, dataBitValues, bitValue);
		});

		System.out.println("=========");
		System.out.printf("海明码：%s%n", toBitStr(hmBytes));
		System.out.printf("校验：%s%n", checkCode(hmBytes));
		System.out.printf("解码：%s%n", toBitStr(decode(hmBytes)));

		hmBytes[0] >>= 1;
		System.out.println("改变海明码其中一位之后的错误编码:" + toBitStr(hmBytes));
		System.out.printf("校验错误编码：%s%n", checkCode(hmBytes));
	}

	@Test
	public void testParityCheck() {
		String bs1 = "01101011";
		byte b1 = (byte) Integer.parseInt(bs1, 2);
		byte[] checkBytes = new byte[] { b1 };
		System.out.println("校验码：" + bs1 + " 奇校验：" + oddNumberCheck(checkBytes, false)); // 奇数校验
		System.out.println("校验码：" + bs1 + " 偶校验：" + evenNumberCheck(checkBytes, false)); // 偶数校验
	}
}
