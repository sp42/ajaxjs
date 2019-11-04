package com.ajaxjs.user;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ajaxjs.util.CommonUtil;

/**
 * 用户工具类
 * 
 * @author Administrator
 *
 */
public class UserUtil {

	/**
	 * 生成指定长度的随机字符，可能包含数字
	 * 
	 * @param length 户要求产生字符串的长度
	 * @return 随机字符
	 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}

		return sb.toString();
	}

	/**
	 * 验证 email 是否合法正确
	 */
	private final static String emailRegexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 是否合法的邮件
	 * 
	 * @param email 待测试的邮件地址
	 * @return true 表示为合法邮件
	 */
	public static boolean isVaildEmail(String email) {
		return CommonUtil.regTest(emailRegexp, email);
	}

	/**
	 * 验证手机号码是否合法正确
	 */
	private static final String phoneRegexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

	/**
	 * 是否合法的手机号码，仅限中国大陆号码
	 * 
	 * @param phoneNo 待测试的手机号码
	 * @return true 表示为手机号码
	 */
	public static boolean isVaildPhone(String phoneNo) {
		return CommonUtil.regTest(phoneRegexp, phoneNo);
	}

	/**
	 * 测试 8421 码是否包含 v
	 * 
	 * @param v		当前权限值
	 * @param all	同值
	 * @return true=已包含
	 */
	public static boolean testBCD(int v, int all) {
		return (v & all) == v;
	}

	public static class SmsService {
		private static ConcurrentMap<String, Long> map = new ConcurrentHashMap<>();

		static long cleanMapInterval = 6000;

		static long sendInterval = 1000 * 60;

		/**
		 * 是否可以发送
		 * 
		 * @param id
		 *            手机号或者 ip
		 * @return 如果成功将发送时间修改为当前时间, 则返回true. 否则返回false
		 */
		public static boolean canSend(String id) {
			long now = System.currentTimeMillis();
			Long sent = map.putIfAbsent(id, now);

			if (sent == null) // 第一次发送短信, 而且现在的时间也已经放到了map中, 可以发送
				return true;

			long nextCanSendTime = sent + sendInterval;

			if (now < nextCanSendTime)
				return false;

			return map.replace(id, sent, now);
		}

		static {
			// clear map
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					long expire = System.currentTimeMillis() - sendInterval;

					for (String key : map.keySet()) {
						Long sendTime = map.get(key);

						if (sendTime < expire)
							map.remove(key, sendTime);
					}
				}
			}, 3000, cleanMapInterval);
		}

	}
}