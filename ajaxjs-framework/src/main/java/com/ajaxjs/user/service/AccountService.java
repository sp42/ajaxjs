package com.ajaxjs.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.ajaxjs.app.ThirdPartyService;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.user.UserUtil;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.Symmetri_Cipher;

/**
 * 账号服务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class AccountService {
	/**
	 * 验证邮件
	 */
	public static final int EMAIL = 1;

	/**
	 * 忘记密码
	 */
	public static final int FORGET_PASSWORD = 2;

	/**
	 * 检查邮件参数（明文）与加密中的邮件是否一致。这是高阶函数
	 * 
	 * @param email        邮件地址
	 * @param userIdHolder 用户保存用户 id 的容器
	 * @param ex           记录异常信息
	 * @return 检查函数
	 */
	private final static Predicate<String[]> checkEmail(String email, String[] userIdHolder, List<Throwable> ex) {
		return arr -> {
			String value = arr[1];
			String[] _arr = value.split("_");

			if (email.equals(_arr[0])) {
				userIdHolder[0] = _arr[1];
				return true;
			} else {
				ex.add(new IllegalAccessError("邮件地址不匹配！"));
				return false;
			}
		};
	}

	/**
	 * 邮箱审核的显示，单位是分钟
	 */
	private static final int EMAIL_TIMEOUT = 20;

	/**
	 * 如果校验通过，返回用户 id
	 * 
	 * @param token 令牌
	 * @param email 邮件地址
	 * @return 用户 id
	 */
	public static Long checkEmail_VerifyToken(String token, String email) {
		String decrypted = Symmetri_Cipher.AES_Decrypt(token, ConfigService.getValueAsString("System.api.AES_Key"));

		if (decrypted != null) {
			String[] arr = decrypted.split("-");

			List<Throwable> ex = new ArrayList<>();
			Predicate<String[]> checkTimespam_arr = _arr -> TokenMaker.checkTimespam(EMAIL_TIMEOUT, ex).test(_arr[3]);

			String[] userIdHolder = new String[1];
			if (checkEmail(email, userIdHolder, ex).and(checkTimespam_arr).test(arr)) {
				// 审核通过
				Long userId = Long.parseLong(userIdHolder[0]);

				return userId;
			} else
				throw new IllegalAccessError(ex.get(0).getMessage());
		} else {
			throw new IllegalAccessError("非法请求");
		}
	}

	/**
	 * 邮件模板
	 */
	private final static String HTML = "用户 %s 您好：<br />&nbsp;&nbsp;&nbsp;&nbsp;请点击下面的链接进行%s：<br />" + "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"%s\" target=\"_blank\">%s</a>。"
			+ "<br /> &nbsp;&nbsp;&nbsp;&nbsp;提示：1）请勿回复本邮件；2）本邮件超过 " + EMAIL_TIMEOUT + " 分钟链接将会失效，需要重新申请%s；3）如不能打开，请复制该链接到浏览器。";

	/**
	 * 发送 Token 邮件
	 * 
	 * @param email 邮件地址
	 * @param title 此次动作的名称，例如“重置密码”、“邮箱审核”
	 * @param URL   审核 token 的回调地址，例如 /user/account/emailVerify/
	 * @return 邮件是否发送成功
	 */
	public static boolean sendTokenMail(String email, String title, String URL) {
		if (!UserUtil.isVaildEmail(email))
			throw new IllegalArgumentException(email + "不是合法的邮件地址");

		User user = UserService.dao.findByEmail(email);
		Objects.requireNonNull(user, "没有该邮件的用户，目标邮件是： " + email);

		String value = email + "_" + user.getId();
		Function<String, String> fn = TokenMaker::addSalt;
		fn = fn.andThen(TokenMaker.value(value).andThen(TokenMaker::addTimespam).andThen(TokenMaker.encryptAES(ConfigService.getValueAsString("System.api.AES_Key"))));

		String url = String.format(URL + "?email=%s&token=%s", Encode.urlEncode(email), Encode.urlEncode(fn.apply(TokenMaker.TOKEN_TPL)));

		Mail mail = new Mail();
		mail.setTo(email);
		mail.setSubject(title);
		mail.setHTML_body(true);
		mail.setContent(String.format(HTML, email, title, url, url, title));

		ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);

		return services.sendEmail(mail);
	}
}
