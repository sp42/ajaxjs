package test.com.ajaxjs.net.mail;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.MailException;
import com.ajaxjs.net.mail.Sender;

public class TestMail {
	@org.junit.Test
	public void test163() {
		Mail mail = new Mail();
		mail.setMailServer("smtp.163.com");
		mail.setAccount("pacoweb");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("sp42@qq.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");

		assertTrue(send(mail));

	}

	/**
	 * 发送邮件
	 * 
	 * @param mail
	 *            服务器信息和邮件信息
	 * @return true 表示为发送成功，否则为失败
	 */
	private static boolean send(Mail mail) {
		try (Sender sender = new Sender(mail)) {
			return sender.sendMail();
		} catch (IOException | MailException e) {
			e.printStackTrace();
			return false;
		}
	}
}