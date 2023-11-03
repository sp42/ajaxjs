package com.ajaxjs.base.service.message;

/**
 * 发送短信的服务
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface ISendSMS {
	/**
	 * 发送短信
	 * 
	 * @param phone
	 * @param code
	 * @return
	 */
	boolean send(String phone, String code);

	/**
	 * 生成随机码，保存到缓存
	 *
	 * @param phone
	 * @param userId
	 * @param username
	 * @return 随机码
	 */
	String getRandomCodeAndSave(String phone, String userId, String username);

	/**
	 * 检查是否合法请求
	 *
	 * @param phone
	 * @param v_code
	 * @return 用户 id 和用户名
	 */
	String[] checkSmsCode(String phone, String v_code);
}
