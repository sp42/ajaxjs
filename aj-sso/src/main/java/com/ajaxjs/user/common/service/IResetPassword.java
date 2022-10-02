package com.ajaxjs.user.common.service;

/**
 * 重置密码
 * 
 * @author Frank Cheung
 *
 */
public abstract interface IResetPassword extends IBaseUserService {
	/**
	 * 根据 email 重置密码
	 * 
	 * @param email    用户邮件
	 * @param tenantId 租户 id
	 * @return true 表示发送成功
	 */
	Boolean sendRestEmail(String email, int tenantId);

	/**
	 * 校验 Token 并更新密码。
	 * 
	 * 根据邮件查询用户，验证 token，若通过更新密码
	 * 
	 * @param token    用户令牌
	 * @param newPsw   用户输入的新密码
	 * @param email    邮件地址
	 * @param tenantId
	 * @return true 表示更新成功
	 */
	Boolean verifyTokenUpdatePsw(String token, String newPsw, String email, int tenantId);

	/**
	 * 根据 手机 重置密码
	 * 
	 * @param phone    手机号码
	 * @param tenantId 租户 id
	 * @return true 表示发送成功
	 */
	Boolean sendRestPhone(String phone, int tenantId);

	/**
	 * 校验 Sms code 并更新密码。
	 * 
	 * 根据邮件查询用户，验证 token，若通过更新密码
	 * 
	 * @param token    用户令牌
	 * @param newPsw   用户输入的新密码
	 * @param email    邮件地址
	 * @param tenantId
	 * @return true 表示更新成功
	 */
	Boolean verifySmsUpdatePsw(String code, String newPsw, String phone, int tenantId);
}
