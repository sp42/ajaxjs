package com.ajaxjs.user.token;

import java.util.Objects;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.Sender;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.user.UserCommonAuthService;
import com.ajaxjs.user.UserService;
import com.ajaxjs.user.UserUtil;

/**
 * 忘记密码
 * 
 * @author Administrator
 *
 */
public class ForgetPassword extends VerifyToken {

	/**
	 * 根据电子邮件重设密码
	 * 
	 * @param email
	 */
	public static String findByEmail(String siteBasePath, String email) {
		if (!UserUtil.isVaildEmail(email))
			throw new IllegalArgumentException(email + "不是合法的邮件地址");

		User user = UserService.dao.findByEmail(email);
		Objects.requireNonNull(user, "没有该邮件的用户，目标邮件是： " + email);

		// 如果已经存在 token 先删除。这时是用户重复按下“找回密码”按钮的时候
		findDel(user, FORGET_PASSWORD);

		String token = saveTokenWithEmailUser(email, user.getUid(), FORGET_PASSWORD);

		String url = siteBasePath + "?token=" + token;
		String html = "用户 %s 您好：<br />&nbsp;&nbsp;&nbsp;&nbsp;请点击下面的链接重设密码：<a href=\"%s\" target=\"_blank\">%s</a>。"
				+ "<br /> &nbsp;&nbsp;&nbsp;&nbsp;提示：1）请勿回复本邮件；2）本邮件超过 30 分钟链接将会失效，需要重新申请找回密码。";
		html = String.format(html, email, url, url);

		Mail mail = new Mail();// 创建邮件实体
		mail.setMailServer(ConfigService.getValueAsString("mailServer.server"));// 指定邮件 SMTP 服务器
		mail.setAccount(ConfigService.getValueAsString("mailServer.user"));// 发送人在 SMTP 上的账户
		mail.setPassword(ConfigService.getValueAsString("mailServer.password"));// 密码
		mail.setFrom("pacoweb@163.com");// 指定发件人
		mail.setTo("sp42@qq.com");// 收件人
		mail.setSubject("用户 " + email + " 重置密码");// 邮件主题
		mail.setHTML_body(true);// 是否 HTML 格式邮件
		mail.setContent(html);// 邮件正文

		String msg = Sender.send(mail) ? "操作成功！已经发送找回密码链接到您邮箱。请在30分钟内重置密码" : "邮件发送失败";

		return msg;
	}

	public boolean verifyTokenRestPsw(String token, String psw) throws ServiceException {
		try {
			User user = verifyToken(token, getTimeout(), false);
			UserCommonAuth old = UserCommonAuthService.dao.findByUserId(user.getId());
			boolean isOk = new UserCommonAuthService().updatePwd(old, psw);
			findDel(user, FORGET_PASSWORD); // 任务完成，删除 token

			return isOk;
		} catch (Throwable e) {
			throw new ServiceException(e.getMessage());
		}
	}
}
