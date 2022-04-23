package com.ajaxjs.user.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.common.service.ResetPasswordService;
import com.ajaxjs.user.common.service.SendEmail;
import com.ajaxjs.user.common.service.SendSMS;
import com.ajaxjs.user.common.service.UserDAO;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.util.StrUtil;

/**
 * 重置密码
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/user/reset_password")
public class ResetPasswordController implements UserDAO {
	@Autowired
	ResetPasswordService resetPasswordService;

	@Autowired
	SendEmail sendEmail;

	@Value("${Website.basePath}")
	String websiteBasePath;

	@Value("${ResetPassword.tokenTimeout}")
	String tokenTimeout;

	/**
	 * 根据 email 重置密码
	 * 
	 * @param email    用户邮件
	 * @param tenantId 租户 id
	 * @return
	 */
	@PostMapping(value = "/sendRestEmail", produces = MediaType.APPLICATION_JSON_VALUE)
	public String sendRestEmail(@RequestParam(required = true) String email, @RequestParam(required = true) int tenantId) {
		if (!StringUtils.hasText(email) || !UserUtils.EAMIL_REG.matcher(email).find())
			throw new IllegalArgumentException("请提交有效的邮件地址");

		User user = findByEmail(email, tenantId);
		if (user == null)
			throw new IllegalAccessError("该 email：" + email + " 的用户不存在！");

		String token = resetPasswordService.makeEmailToken(email, tenantId);
		String url = websiteBasePath + FIND_BY_EMAIL;
		url += String.format("?email=%s&token=%s", StrUtil.urlEncode(email), StrUtil.urlEncode(token));

		String title = "重置密码";
		Map<String, String> map = new HashMap<>(4);
		map.put("username", user.getUsername());
		map.put("link", url);
		map.put("desc", title);
		map.put("timeout", tokenTimeout);

		String content = sendEmail.getEmailContent(map);

		if (sendEmail.send(email, title, content))
			return BaseController.jsonOk("发送邮件成功");
		else
			return BaseController.jsonNoOk("发送邮件失败");
	}

	private final static String FIND_BY_EMAIL = "/user/reset_password/findByEmail/";

	/**
	 * 校验 Token 并更新密码。
	 * 
	 * 根据邮件查询用户，验证 token，若通过更新密码
	 * 
	 * @param token
	 * @param newPsw   用户输入的新密码
	 * @param email
	 * @param tenantId
	 * @return
	 */
	@PostMapping(value = "/verifyTokenUpdatePsw", produces = MediaType.APPLICATION_JSON_VALUE)
	public String verifyTokenUpdatePsw(
	// @formatter:off
			@RequestParam(required = true) String token, 
			@RequestParam(required = true) String newPsw, 
			@RequestParam(required = true) String email, 
			@RequestParam(required = true) int tenantId) {
	// @formatter:on
		UserDAO.setWhereQuery(" u.email = '" + email + "' AND u.tenantId = 1 ");
		Map<String, Object> user = UserDAO.findOneWithAuth();

		if (user == null)
			throw new IllegalAccessError("该 email：" + email + " 的用户不存在！");

		if (!resetPasswordService.checkEmailToken(token, email))
			throw new IllegalAccessError("校验 Token　失败");

		if (resetPasswordService.updatePwd(user, newPsw))
			return BaseController.jsonOk("重置密码成功");
		else
			return BaseController.jsonNoOk("重置密码失败");
	}

	@Autowired
	SendSMS sendSMS;

	/**
	 * 根据 手机 重置密码
	 * 
	 * @param phone    手机号码
	 * @param tenantId 租户 id
	 * @return
	 */
	@PostMapping(value = "/sendRestPhone", produces = MediaType.APPLICATION_JSON_VALUE)
	public String sendRestPhone(@RequestParam(required = true) String phone, @RequestParam(required = true) int tenantId) {
		if (!StringUtils.hasText(phone) || !UserUtils.PHONE_REG.matcher(phone).find())
			throw new IllegalArgumentException("请提交有效的手机");

		User user = findByPhone(phone, tenantId);
		if (user == null)
			throw new IllegalAccessError("该手机： " + phone + " 的用户不存在！");

		String code = SendSMS.getRandomCodeAndSave(phone, user.getId() + "", user.getUsername());

		if (sendSMS.send(phone, code))
			return BaseController.jsonOk("发送手机短信 " + phone + " 成功");
		else
			return BaseController.jsonNoOk("发送手机短信 " + phone + " 失败");
	}

	/**
	 * 校验 Sms code 并更新密码。
	 * 
	 * 根据邮件查询用户，验证 token，若通过更新密码
	 * 
	 * @param token
	 * @param newPsw   用户输入的新密码
	 * @param email
	 * @param tenantId
	 * @return
	 */
	@PostMapping(value = "/verifySmsUpdatePsw", produces = MediaType.APPLICATION_JSON_VALUE)
	public String verifySmsUpdatePsw(
	// @formatter:off
			@RequestParam(required = true) String code, 
			@RequestParam(required = true) String newPsw, 
			@RequestParam(required = true) String phone, 
			@RequestParam(required = true) int tenantId) {
	// @formatter:on
		UserDAO.setWhereQuery(" u.phone = '" + phone + "' AND u.tenantId = 1 ");
		Map<String, Object> user = UserDAO.findOneWithAuth();

		if (user == null)
			throw new IllegalAccessError(String.format("不存在手机号码[%s]的用户", phone));

		SendSMS.checkSmsCode(phone, code); // 没有异常就表示通过

		if (resetPasswordService.updatePwd(user, newPsw))
			return BaseController.jsonOk("重置密码成功");
		else
			return BaseController.jsonNoOk("重置密码失败");
	}
}
