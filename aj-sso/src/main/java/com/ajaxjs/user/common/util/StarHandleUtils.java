package com.ajaxjs.user.common.util;

import java.util.StringJoiner;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.logger.EasyPrint;

/**
 * 星号（*）处理工具类
 * 
 * @author Erwin Feng
 * @since 2020/8/23 1:57 上午
 */
public class StarHandleUtils {
	/** 空的字符串（""） */
	public static final String BLANK = "";

	public static String ip(String ip) {
		if (!StringUtils.hasText(ip))
			return BLANK;

		String[] array = ip.split("\\.");

		if (array.length == 4)
			return String.format("%s.%s.*.*", array[0], array[1]);

		return BLANK;
	}

	/**
	 * 手机号星号（*）处理
	 * <p>
	 * 如一个手机号是：12345678901，处理之后是：123****8901
	 * </p>
	 * 
	 * @param phone 待处理的手机号，11位
	 * @return 返回星号处理之后的手机号
	 */
	public static String phone(String phone) {
		if (!StringUtils.hasText(phone))
			return BLANK;

		if (phone.length() == 11) {
			String left = getLeft(phone, 3);
			String right = getRight(phone, 4);
			String star = generateStar(4);
			return left + star + right;
		}
		
		return BLANK;
	}

	/**
	 * 邮箱星号（*）处理
	 * 
	 * @param email 待处理的邮箱
	 * @return 返回处理之后的邮箱
	 */
	public static String email(String email) {
		if (!StringUtils.hasText(email))
			return BLANK;

		int index = email.lastIndexOf("@");
		
		if (index > 0) {
			int indexLeft = index == 1 ? 0 : 1;
			String right = getRight(email, email.length() - index);
			String left = getLeft(email, indexLeft);
			String star = generateStar(index - indexLeft);
			return left + star + right;
		}

		return BLANK;
	}

	/**
	 * 身份证号码星号处理
	 *
	 * <p>
	 * 前留3，后留4，中间由星号填充
	 * </p>
	 *
	 * @param idCardNo 待处理的身份证号码
	 * @return 返回处理之后的身份证号码
	 */
	public static String idCardNo(String idCardNo) {
		return idCardNo(idCardNo, 3, 4);
	}

	/**
	 * 身份证号码星号处理
	 * 
	 * @param idCardNo    待处理的身份证号码
	 * @param leftLength  左边保留长度
	 * @param rightLength 右边保留长度
	 * @return 返回处理之后的身份证号码
	 */
	public static String idCardNo(String idCardNo, int leftLength, int rightLength) {
		if (!StringUtils.hasText(idCardNo))
			return BLANK;

		if (idCardNo.length() <= leftLength + rightLength)
			return BLANK;

		String leftString = getLeft(idCardNo, leftLength);
		String rightString = getRight(idCardNo, rightLength);
		int starLength = idCardNo.length() - leftLength - rightLength;
		String starString = generateStar(starLength);

		return leftString + starString + rightString;
	}

	/**
	 * 密码星号（*）处理
	 * 
	 * @return 返回32位的星号密码
	 */
	public static String password() {
		return generateStar(32);
	}

	/**
	 * 真实姓名星号处理
	 * <p>
	 * 两个字，第一个字星号处理
	 * </p>
	 * <p>
	 * 三个字及以上，只保留第一个和最后一个字，中间都用星号处理
	 * </p>
	 * 
	 * @param realName 真实姓名
	 * @return 姓名带星号
	 */
	public static String realName(String realName) {
		if (!StringUtils.hasText(realName))
			return BLANK;

		if (realName.length() == 2) {
			String right = getRight(realName, 1);
			String star = generateStar(1);
			return star + right;
		}

		if (realName.length() > 2) {
			String left = getLeft(realName, 1);
			String right = getRight(realName, 1);
			String star = generateStar(realName.length() - 2);
			return left + star + right;
		}

		return generateStar(1);
	}

	/**
	 * 生成一个指定长度的星号（*）字符串
	 *
	 * @param length 生成星号的长度
	 * @return 生成一个指定长度的星号（*）字符串
	 */
	public static String generateStar(int length) {
		StringJoiner sj = new StringJoiner("", "", "");

		for (int i = 0; i < length; i++)
			sj.add("*");

		return sj.toString();
	}

	/**
	 * 获取字符串左边指定长度的字符串。
	 * <p>
	 * 例如，字符串是：张三，截取长度为1，得到的结果是：张。
	 * </p>
	 * <p>
	 * 注意，如果需要截取的长度大于字符串的长度，那么会返回整个字符串。 如截取的长度是3，那么得到的结果是：张三。
	 * </p>
	 *
	 * @param source 需要截取的字符串
	 * @param length 需要获取左边字符串的长度
	 * @return 返回字符串左边指定长度的字符串
	 */
	public static String getLeft(String source, int length) {
		if (length > source.length()) {
			EasyPrint.warn("length(" + length + ") > source's length(" + source.length() + ")");
			length = source.length();
		}

		return source.substring(0, length);
	}

	/**
	 * 获取字符串右边指定长度的字符串。
	 * <p>
	 * 例如，字符串是：张三，截取长度为1，得到的结果是：三。
	 * </p>
	 * <p>
	 * 注意，如果需要截取的长度大于字符串的长度，那么会返回整个字符串。 如截取的长度是3，那么得到的结果是：张三。
	 * </p>
	 *
	 * @param source 需要截取的字符串
	 * @param length 需要获取右边字符串的长度
	 * @return 返回字符串右边指定长度的字符串
	 */
	public static String getRight(String source, int length) {
		if (length > source.length()) {
			EasyPrint.warn("length(" + length + ") > source's length(" + source.length() + ")");
			length = source.length();
		}

		return source.substring(source.length() - length);
	}

}