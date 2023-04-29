package com.ajaxjs.util.binrary;

/**
 * 二进制工具类
 *
 * @author 谢辉 <a href="https://blog.csdn.net/qq_41995919/article/details/107074339">...</a>
 *
 */
public class BinaryUtil {
	/**
	 * 十进制数字转二进制
	 * 
	 * @param num       十进制数字
	 * @param strResult 结果容器，追加结果用，
	 * @return 返回结果字符串
	 */
	public static String DecimalToBinary(Integer num, StringBuilder strResult) {
		if (num < 0)
			throw new IllegalArgumentException(num + "并非十进制的有效整数值，请正确输入！");

		int integer = num / 2;// 整除结果
		int StrInteger = num % 2;// 取余结果

		if (integer == 0) // 表示1/2=0；商为0，不能再往下除了
			strResult.append(StrInteger);
		else {
			// 递归追加结果
			strResult.append(StrInteger);
			DecimalToBinary(integer, strResult);
		}

		return new StringBuilder(strResult.toString()).reverse().toString();
	}

	/**
	 * 二进制转换十进制
	 * 
	 * @param binary 二进制字符串
	 * @return 转换的结果
	 * @throws IllegalArgumentException
	 */
	public static Integer BinaryToDecimal(String binary) {
		char[] array = binary.toCharArray();

		// 对传入的二进制字符串检测
		for (int i = 0; i < array.length; i++) {
			if (array[i] != '0' && array[i] != '1')
				throw new IllegalArgumentException("二进制只能有0或1！");

		}
		// 再次检测字符串，避免‘00010101’，转成‘10101’
		int index = binary.indexOf("1") > 0 ? binary.indexOf("1") : 0;
		binary = array[0] == '0' ? binary.substring(index) : binary;

		// 规范二进制字符串后，开始计算结果
		return compute(binary);
	}

	private static Integer compute(String binaryNum) {
		int decimalNum = 0;
		int parseInt = Integer.parseInt(binaryNum);

		if (parseInt == 1)
			return 1;
		else if (parseInt <= 0)
			return 0;
		else {
			char[] binaryArr = binaryNum.toCharArray();
			decimalNum = binaryArr[1] == '0' ? 2 : 3;

			for (int i = 2; i < binaryArr.length; i++)
				decimalNum = binaryArr[i] == '0' ? decimalNum * 2 : decimalNum * 2 + 1;

			return decimalNum;
		}
	}
}