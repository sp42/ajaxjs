package com.util.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份证号码
 * 
 * <pre>
 * 1、号码的结构
 * 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 2、地址码(前六位数）
 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
 * 3、出生日期码（第七位至十四位）
 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * 4、顺序码（第十五位至十七位）
 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
 * 顺序码的奇数分配给男性，偶数分配给女性。
 * 5、校验码（第十八位数）
 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
 * X 9 8 7 6 5 4 3 2
 * </pre>
 * 
 * https://github.com/binarywang/java-testdata-generator/blob/master/src/main/java/cn/binarywang/tools/generator/ChineseIDCardNumberGenerator.java
 * 
 * @author Frank Cheung
 *
 */
public class ChineseIDCardNumberGenerator extends Gen {
	/**
	 * 生成签发机关：XXX公安局/XX区分局 Authority
	 */
	public static String generateIssueOrg() {
		return getItem(PROVINCES) + getItem(CITES) + "公安局" + getItem(DISTRICTS) + "分局";
	}

	/**
	 * 生成有效期限：20150906-20350906 Valid Through
	 */
	public static String generateValidPeriod() {
		return getDate_yyyyMMdd() + "-" + getDate_yyyyMMdd();
//    	
//        Date beginDate =new Date(getDate()) ;
//        String formater = "yyyyMMdd";
//        DateTime endDate = beginDate.withYear(beginDate.getYear() + 20);
//        return beginDate.toString(formater) + "-" + endDate.toString(formater);
	}

	/**
	 * 生成身份证号码
	 * @return 身份证号码
	 */
	public static String generate() {
		String leftPad = BankCard.leftPad((getNum(0, 9998) + 1) + "", 4, "0");
		String areaCode = CODE.keySet().toArray(new String[0])[getNum(0, CODE.size())] + leftPad;

		String birthday = getDate_yyyyMMdd();
		String randomCode = String.valueOf(1000 + getNum(0, 999)).substring(1);
		String pre = areaCode + birthday + randomCode;

		return pre + getVerifyCode(pre);
	}
	
	public static void main(String[] args) {
		System.out.println(generate());
	}

	private static String getVerifyCode(String cardId) {
		String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		int tmp = 0;
		System.out.println(cardId);
		for (int i = 0; i < Wi.length; i++)
			tmp += Integer.parseInt(String.valueOf(cardId.charAt(i))) * Integer.parseInt(Wi[i]);

		int modValue = tmp % 11;
		String strVerifyCode = ValCodeArr[modValue];

		return strVerifyCode;
	}

	public static final Map<String, String> CODE = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("11", "北京");
			put("12", "天津");
			put("13", "河北");
			put("14", "山西");
			put("15", "内蒙古");
			put("21", "辽宁");
			put("22", "吉林");
			put("23", "黑龙江");
			put("31", "上海");
			put("32", "江苏");
			put("33", "浙江");
			put("34", "安徽");
			put("35", "福建");
			put("36", "江西");
			put("37", "山东");
			put("41", "河南");
			put("42", "湖北");
			put("43", "湖南");
			put("44", "广东");
			put("45", "广西");
			put("46", "海南");
			put("50", "重庆");
			put("51", "四川");
			put("52", "贵州");
			put("53", "云南");
			put("54", "西藏");
			put("61", "陕西");
			put("62", "甘肃");
			put("63", "青海");
			put("64", "宁夏");
			put("65", "新疆");
			put("71", "台湾");
			put("81", "香港");
			put("82", "澳门");
			put("91", "国外");
		}
	};

}
