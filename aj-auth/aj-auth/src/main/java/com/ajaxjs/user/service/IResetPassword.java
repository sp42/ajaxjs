package com.ajaxjs.user.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.spring.easy_controller.ControllerMethod;

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
	@PostMapping("/send_reset_email/{email}")
	@ControllerMethod("发送重置邮件")
	boolean sendRestEmail(@PathVariable String email);

	/**
	 * 校验 Token 并更新密码。
	 * 
	 * 根据邮件查询用户，验证 token，若通过更新密码
	 * 
	 * @param token  用户令牌
	 * @param newPsw 用户输入的新密码
	 * @param email  邮件地址
	 * @return true 表示更新成功
	 */
	@PostMapping("/verify_token_update_psw/{email}/{token}")
	@ControllerMethod("根据邮件重置密码")
	boolean verifyTokenUpdatePsw(@PathVariable String token, @RequestParam(required = true) String newPsw, @PathVariable String email);

	/**
	 * 根据 手机 重置密码
	 * 
	 * @param phone 手机号码
	 * @return true 表示发送成功
	 */
	@PostMapping("/send_reset_phone/{phone}")
	@ControllerMethod("发送手机短信")
	boolean sendRestPhone(@PathVariable String phone);

	/**
	 * 校验 Sms code 并更新密码。
	 * 
	 * 根据邮件查询用户，验证 token，若通过更新密码
	 * 
	 * @param token  用户令牌
	 * @param newPsw 用户输入的新密码
	 * @param email  邮件地址
	 * @return true 表示更新成功
	 */
	@PostMapping("/verify_sms_update_psw/{phone}/{code}")
	@ControllerMethod("根据短信重置密码")
	Boolean verifySmsUpdatePsw(@PathVariable String code, @RequestParam(required = true) String newPsw, @PathVariable String phone);
}
