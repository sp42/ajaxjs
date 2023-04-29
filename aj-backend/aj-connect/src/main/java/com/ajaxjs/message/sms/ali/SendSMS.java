package com.ajaxjs.message.sms.ali;


import com.ajaxjs.framework.ISendSMS;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.regex.Pattern;

@Component
public class SendSMS implements ISendSMS {
	private static final LogHelper LOGGER = LogHelper.getLog(SendSMS.class);

	@Autowired
	private AliyunSmsEntity sendSMS;

	@Override
	public boolean send(String phone, String code) {
		sendSMS.setTemplateParam(String.format("{\"code\":%s}", code));
		sendSMS.setPhoneNumbers(phone);

		String result = AliyunSMS.send(sendSMS);

		if (result.contains("触发分钟级流控"))
			throw new IllegalAccessError("发送短信过于频繁，请稍后再试。");

		return "OK".equals(result);
	}

	/**
	 * 缓存的前缀
	 */
	private final static String CACHE_PREFIX = "sms_";

	/**
	 * 短信代码有效时间，当前是 5 分钟
	 */
	private final static int SMS_EXPIRE_SECONDS = 5 * 60;

	@Override
	public String getRandomCodeAndSave(String phone, String userId, String username) {
		String key = CACHE_PREFIX + phone;
		String rad;

		if (ExpireCache.CACHE.containsKey(key)) {
			String radAndUserId = ExpireCache.CACHE.get(key, String.class);
			String[] _radAndUserId = radAndUserId.replace(CACHE_PREFIX, "").split("_");

			rad = _radAndUserId[0];
		} else {
			rad = (new Random().nextInt(900000) + 100000) + ""; // 6 位随机码
			ExpireCache.CACHE.put(key, rad + "_" + userId + "_" + username, SMS_EXPIRE_SECONDS);

			LOGGER.info("保存用户[{0}] 手机[{1}] 验证码[{2}] 缓存成功", "sms_" + userId, phone, rad);
			/*
			 * 服务端暂存手机号码，那么客户端就不用重复提供了。 验证验证码的时候，根据 userId 查找 手机号码，再得到验证码
			 */
		}

		return rad;
	}

	@Override
	public String[] checkSmsCode(String phone, String v_code) {
		if (!StringUtils.hasText(v_code))
			throw new IllegalArgumentException("请输入验证码");

		if (!PHONE_REG.matcher(phone).find())
			throw new IllegalArgumentException(phone + " 不是有效的手机号码");

		String key = CACHE_PREFIX + phone;
		String radAndUserId = ExpireCache.CACHE.get(key, String.class);

		if (!StringUtils.hasText(radAndUserId))
			throw new IllegalStateException("未发送短讯验证码或验证码已经失效。找不到该手机[" + phone + "]的用户的验证码");

		String[] _radAndUserId = radAndUserId.replace(CACHE_PREFIX, "").split("_");
		String rad = _radAndUserId[0], userId = _radAndUserId[1], userName = _radAndUserId[2];

		if (!rad.equals(v_code))
			throw new IllegalArgumentException("验证码不正确");

		ExpireCache.CACHE.remove(key);// 验证码正确，删除缓存

		return new String[] { userId, userName };
	}

	/**
	 * 验证手机号码是否合法正确
	 */
	private static final String PHONE_REGEXP = "^[1][3-8]\\d{9}$";

	public final static Pattern PHONE_REG = Pattern.compile(PHONE_REGEXP);
}
