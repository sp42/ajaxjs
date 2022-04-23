package com.ajaxjs.sql.util;

import java.util.Random;

import com.ajaxjs.util.StrUtil;

/**
 * *
 * 
 * <pre>
 *  生成随机银行卡号：
 *
 *  参考：效验是否为银行卡，用于验证：
 *  现行 16 位银联卡现行卡号开头 6 位是 622126～622925 之间的，7 到 15 位是银行自定义的，
 *  可能是发卡分行，发卡网点，发卡序号，第 16 位是校验码。
 *  16 位卡号校验位采用 Luhm 校验方法计算：
 * 1，将未带校验位的 15 位卡号从右依次编号 1 到 15，位于奇数位号上的数字乘以 2
 * 2，将奇位乘积的个十位全部相加，再加上所有偶数位上的数字
 * 3，将加法和加上校验位能被 10 整除。
 * </pre>
 * 
 * https://github.com/binarywang/java-testdata-generator/blob/master/src/main/java/cn/binarywang/tools/generator/bank/BankCardNumberGenerator.java
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class BankCard {
	/**
	 * 生成随机银行卡号
	 * 
	 * @return 银行卡号
	 */
	public static String generate() {
		Random random = new Random();
//        ContiguousSet<Integer> sets = ContiguousSet
//            .create(Range.closed(622126, 622925), DiscreteDomain.integers());
//        ImmutableList<Integer> list = sets.asList();

		Integer prev = 622126 + random.nextInt(925 + 1 - 126);
		return generateByPrefix(prev);
	}

	/**
	 * 根据给定前六位生成卡号
	 * 
	 * @param prefix
	 * @return
	 */
	public static String generateByPrefix(Integer prefix) {
		Random random = new Random(System.currentTimeMillis());
		String bardNo = prefix + StrUtil.leftPad(random.nextInt(999999999) + "", 9, "0");

		char[] chs = bardNo.trim().toCharArray();
		int luhnSum = getLuhnSum(chs);
		char checkCode = luhnSum % 10 == 0 ? '0' : (char) (10 - luhnSum % 10 + '0');

		return bardNo + checkCode;
	}

	/**
	 * 根据银行名称 及银行卡类型生成对应卡号
	 *
	 * @param bankName 银行名称
	 * @param cardType 银行卡类型
	 * @return 银行卡号
	 */
	public static String generate(BankNameEnum bankName, BankCardTypeEnum cardType) {
		Integer[] candidatePrefixes = null;

		if (cardType == null)
			candidatePrefixes = bankName.getAllCardPrefixes();
		else {
			switch (cardType) {
			case DEBIT:
				candidatePrefixes = bankName.getDebitCardPrefixes();
				break;
			case CREDIT:
				candidatePrefixes = bankName.getCreditCardPrefixes();
				break;
			default:
			}
		}

		if (candidatePrefixes == null || candidatePrefixes.length == 0)
			throw new RuntimeException("没有该银行的相关卡号信息");

		Integer prefix = candidatePrefixes[new Random().nextInt(candidatePrefixes.length)];

		return generateByPrefix(prefix);
	}

	/**
	 * 校验银行卡号是否合法
	 *
	 * @param cardNo 银行卡号
	 * @return 是否合法
	 */
	public static boolean validate(String cardNo) {
		if (cardNo.length() > 19 || cardNo.length() < 16)
			return false;

		int luhnSum = getLuhnSum(cardNo.substring(0, cardNo.length() - 1).trim().toCharArray());
		char checkCode = (luhnSum % 10 == 0) ? '0' : (char) ((10 - luhnSum % 10) + '0');

		return cardNo.substring(cardNo.length() - 1).charAt(0) == checkCode;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位 该校验的过程： 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
	 * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
	 * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
	 */
	private static int getLuhnSum(char[] chs) {
		int luhnSum = 0;

		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';

			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}

			luhnSum += k;
		}

		return luhnSum;
	}

	/**
	 * 银行卡类型枚举类
	 * 
	 * @author Frank Cheung<sp42@qq.com>
	 *
	 */
	public static enum BankCardTypeEnum {
		/**
		 * 借记卡/储蓄卡
		 */
		DEBIT("借记卡/储蓄卡"),
		/**
		 * 信用卡/贷记卡
		 */
		CREDIT("信用卡/贷记卡");

		@SuppressWarnings("unused")
		private final String name;

		BankCardTypeEnum(String name) {
			this.name = name;
		}
	}

	/**
	 * 常见银行名称枚举类
	 */
	public static enum BankNameEnum {
		/**
		 * <pre>
		 * 中国工商银行
		 * 中国工商银行VISA学生国际信用卡：427020
		 * 中国工商银行VISA国际信用卡金卡：427030
		 * 中国工商银行MC国际信用卡普通卡：530990
		 * 中国工商银行新版人民币贷记卡普卡：622230
		 * 中国工商银行新版人民币贷记卡金卡：622235
		 * 中国工商银行新版信用卡(准贷)普卡：622210
		 * 中国工商银行新版信用卡(准贷)金卡：622215
		 * 中国工商银行牡丹灵通卡借记卡：622200
		 * 中国工商银行原牡丹灵通卡借记卡：955880
		 * </pre>
		 */
		ICBC("102", "中国工商银行", "工行", new Integer[] { 622200, 955880 }, new Integer[] { 427020, 427030, 530990, 622230, 622235, 622210, 622215 }),
		/**
		 * <pre>
		 * 中国农业银行
		 * 中国农业银行人民币贷记卡 香港旅游卡贷记卡金卡：622836
		 * 中国农业银行人民币贷记卡 香港旅游卡贷记卡普卡：622837
		 * 中国农业银行世纪通宝借记卡：622848
		 * 农业银行：552599、404119、404121、519412、403361、558730、520083、520082、519413、49102、404120、404118、53591、404117
		 * </pre>
		 */
		ABC("103", "中国农业银行", "农行"),
		/**
		 * <pre>
		 * 中国银行
		 * 中国银行中银都市卡：622760
		 * 中国银行BOC系列VISA标准卡普通卡/VISA高校认同卡：409666
		 * 中国银行国航知音信用卡：438088
		 * 中国银行上海市分行长城人民币贷记卡普通卡：622752
		 * </pre>
		 */
		BOC("104", "中国银行", "中行"),
		/**
		 * <pre>
		 * 中国建设银行
		 * 中国建设银行VISA龙卡借记卡：436742
		 * 中国建设银行VISA龙卡贷记卡：436745
		 * 中国建设银行支付宝龙卡借记卡：622280
		 * </pre>
		 */
		CCB("105", "中国建设银行", "建行"),
		/**
		 * <pre>
		 * 交通银行
		 * 交通银行VISA普通卡：458123
		 * 交通银行MC信用卡普通卡：521899
		 * 交通银行太平洋卡借记卡：622260
		 * </pre>
		 */
		BCOM("301", "交通银行", "交行"),
		/**
		 * <pre>
		 * 中信银行
		 * 中信银行国航知音信用卡/万事达卡普通卡：518212
		 * 中信银行理财宝卡借记卡：622690
		 * 中信银行万事达卡金卡：520108
		 * 中信银行蓝卡/I卡信用卡：622680
		 * 中信银行：376968、376966、622918、622916、376969、622919、556617、403391、558916、514906、400360、433669、433667、433666、404173、404172、404159、404158、403393、403392、622689、622688、433668、404157、404171、404174、628209、628208、628206
		 * </pre>
		 */
		CITIC("302", "中信银行"),
		/**
		 * <pre>
		 * 中国光大银行
		 * 光大银行卡号开头：406254、622655、622650、622658、356839、486497、481699、543159、425862、406252、356837、356838、356840、622161、628201、628202
		 * </pre>
		 */
		CEB("303", "中国光大银行"),
		/**
		 * <pre>
		 * 华夏银行
		 * 华夏银行：539867,528709
		 * 华夏银行MC钛金卡：523959
		 * 华夏银行人民币卡金卡：622637
		 * 华夏银行人民币卡普卡：622636
		 * 华夏银行MC金卡：528708
		 * 华夏银行MC普卡：539868
		 * </pre>
		 */
		HXB("304", "华夏银行"),
		/**
		 * <pre>
		 * 中国民生银行
		 * 民生银行：407405,517636
		 * 中国民生银行MC金卡：512466
		 * 中国民生银行星座卡借记卡：415599
		 * 中国民生银行VISA信用卡金卡：421870
		 * 中国民生银行蝶卡银卡借记卡：622622
		 * 民生银行：528948,552288,556610,622600,622601,622602,622603,421869,421871,628258
		 * </pre>
		 */
		CMBC("305", "中国民生银行"),
		/**
		 * <pre>
		 * 广东发展银行
		 * 广东发展银行新理财通借记卡：622568
		 * 广东发展银行南航明珠卡MC金卡：520152
		 * 广东发展银行南航明珠卡MC普卡：520382
		 * 广东发展银行理财通借记卡：911121
		 * 广发真情卡：548844
		 * </pre>
		 */
		CGB("306", "广东发展银行"),
		/**
		 * <pre>
		 * 平安银行
		 * 深圳平安银行：622155,622156
		 * 深圳平安银行万事达卡普卡：528020
		 * 深圳平安银行万事达卡金卡：526855
		 * 深发展联名普卡：435744
		 * 深发展卡普通卡：622526
		 * 深发展联名金卡：435745
		 * 深圳发展银行：998801,998802
		 * 深发展卡金卡：622525
		 * 深圳发展银行发展卡借记卡：622538
		 * </pre>
		 */
		PAB("307", "平安银行"),
		/**
		 * <pre>
		 * 招商银行
		 * 招商银行哆啦A梦粉丝信用卡：518710
		 * 招商银行哆啦A梦粉丝信用卡珍藏版卡面/MC贝塔斯曼金卡/MC车主卡：518718
		 * 招商银行QQ一卡通借记卡：622588
		 * 招商银行HELLO KITTY单币卡：622575
		 * 招商银行：545947、521302、439229、552534、622577、622579、439227、479229、356890、356885、545948、545623、552580、552581、552582、552583、552584、552585、552586、552588、552589、645621、545619、356886、622578、622576、622581、439228、628262、628362、628362、628262
		 * 招商银行JCB信用卡普通卡：356889
		 * 招商银行VISA白金卡：439188
		 * 招商银行VISA信用卡普通卡：439225招商银行VISA信用卡金卡：439226
		 * </pre>
		 */
		CMB("308", "招商银行", "招行"),
		/**
		 * <pre>
		 * 兴业银行
		 * 兴业银行：451289、622902、622901、527414、524070、486493、486494、451290、523036、486861、622922
		 * </pre>
		 */
		CIB("309", "兴业银行"),
		/**
		 * <pre>
		 * 上海浦东发展银行
		 * 上海浦东发展银行奥运WOW卡美元单币：418152
		 * 上海浦东发展银行WOW卡/奥运WOW卡：456418
		 * 上海浦东发展银行东方卡借记卡：622521
		 * 上海浦东发展银行VISA普通卡：404738
		 * 上海浦东发展银行VISA金卡：404739
		 * 浦东发展银行：498451,622517,622518,515672,517650,525998,356850,356851,356852
		 * </pre>
		 */
		SPDB("310", "上海浦东发展银行"),

		/**
		 * <pre>
		 * 华润银行
		 * </pre>
		 */
		CR("999999", "华润银行", new Integer[] { 622363 }),

		/**
		 * <pre>
		 * 渤海银行
		 * </pre>
		 */
		BHB("318", "渤海银行"),

		/**
		 * <pre>
		 * 徽商银行
		 * </pre>
		 */
		HSB("319", "徽商银行"),
		/**
		 * <pre>
		 * 江苏银行
		 * </pre>
		 */
		JSB_1("03133010", "江苏银行"),
		/**
		 * <pre>
		 * 江苏银行
		 * </pre>
		 */
		JSB("03133120", "江苏银行"),

		/**
		 * <pre>
		 * 上海银行
		 * 上海银行VISA金卡：402674
		 * 上海银行借记卡：622892
		 * </pre>
		 */
		SHB("04012900", "上海银行"),

		/**
		 * <pre>
		 * 中国邮政储蓄银行
		 * 中国邮政储蓄绿卡借记卡：622188
		 * </pre>
		 */
		POST("403", "中国邮政储蓄银行"),

		/**
		 * <pre>
		 * 北京银行
		 * 北京银行京卡借记卡：602969
		 * </pre>
		 */
		BOB("", "北京银行"),

		/**
		 * <pre>
		 * 宁波银行
		 * 宁波银行：512431,520194,622318,622778
		 * 宁波银行汇通卡人民币金卡/钻石联名卡：622282
		 * </pre>
		 */
		BON("", "宁波银行");

		/**
		 * 银行代码
		 */
		private final String code;

		/**
		 * 银行名称
		 */
		private final String name;

		/**
		 * 银行简称
		 */
		private String abbrName;

		/**
		 * 信用卡卡号前缀数组
		 */
		private Integer[] creditCardPrefixes;

		/**
		 * 借记卡卡号前缀数组
		 */
		private Integer[] debitCardPrefixes;

		/**
		 * 所有卡号前缀数组
		 */
		private Integer[] allCardPrefixes;

		BankNameEnum(String code, String name) {
			this.code = code;
			this.name = name;
		}

		BankNameEnum(String code, String name, String abbrName) {
			this.code = code;
			this.name = name;
			this.abbrName = abbrName;
		}

		BankNameEnum(String code, String name, String abbrName, Integer[] debitCardPrefixes, Integer[] creditCardPrefixes) {
			this.code = code;
			this.name = name;
			this.abbrName = abbrName;
			this.creditCardPrefixes = creditCardPrefixes;
			this.debitCardPrefixes = debitCardPrefixes;

//			this.allCardPrefixes = ArrayUtils.addAll(this.creditCardPrefixes, this.debitCardPrefixes);
		}

		BankNameEnum(String code, String name, Integer[] debitCardPrefixes) {
			this.code = code;
			this.name = name;
			this.debitCardPrefixes = debitCardPrefixes;
			this.allCardPrefixes = debitCardPrefixes;
		}

		BankNameEnum(String code, String name, Integer[] debitCardPrefixes, Integer[] creditCardPrefixes) {
			this.code = code;
			this.name = name;
			this.creditCardPrefixes = creditCardPrefixes;
			this.debitCardPrefixes = debitCardPrefixes;

//			this.allCardPrefixes = ArrayUtils.addAll(this.creditCardPrefixes, this.debitCardPrefixes);
		}

		public String getName() {
			return this.name;
		}

		public String getAbbrName() {
			return this.abbrName;
		}

		public Integer[] getCreditCardPrefixes() {
			return this.creditCardPrefixes;
		}

		public Integer[] getDebitCardPrefixes() {
			return this.debitCardPrefixes;
		}

		public Integer[] getAllCardPrefixes() {
			return this.allCardPrefixes;
		}

		public String getCode() {
			return this.code;
		}
	}
}
