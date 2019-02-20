package com.ajaxjs.cms.user.idcard;

/**
 * 快速检测
 * 
 * @author https://blog.csdn.net/ZWLJavaWeb/article/details/55047332
 *
 */
public class FastDetect {
	private static String[] validtable = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };

	/**
	 * 
	 * @param ID
	 * @return
	 */
	public static boolean cleckIdNumber(String ID) {
		boolean flag = false;
		// 验证码
		String validatecode = ID.substring(17, 18);
		// 前17位称为本体码
		String selfcode = ID.substring(0, 17);
		String code[] = new String[17];

		for (int i = 0; i < 17; i++)
			code[i] = selfcode.substring(i, i + 1);

		// 加权因子公式：2的n-1次幂除以11取余数，n就是那个i，从右向左排列。
		int sum = 0; // 用于加权数求和
		for (int i = 0; i < code.length; i++) {
			// 计算该位加权因子
			int yi = adjustFactor(i + 1) % 11;
			// 得到对应数位上的数字
			int count = Integer.parseInt(code[code.length - i - 1]);
			// 加权求和
			sum += (count * yi);
		}

		// 验证校验码是否正确
		String valdate = validtable[sum % 11];
		if (valdate.equalsIgnoreCase(validatecode))
			flag = true;

		return flag;
	}

	/**
	 * 计算身份证数位数字加权因子
	 * 
	 * @param digit 表示数位
	 * @return
	 */
	public static int adjustFactor(int digit) {
		int sum = 1;

		for (int i = 0; i < digit; i++) {
			// sum=sum*2;
			sum = sum << 1;
		}

		return sum;
	}
}
