package test.com.ajaxjs.net;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.MailException;
import com.ajaxjs.net.mail.Sender;

public class TestMail {
	@org.junit.Test
	public void test163() throws MailException, UnknownHostException, IOException {
		Mail mail = new Mail();
		mail.setMailServer("smtp.163.com");
		mail.setAccount("pacoweb");
		mail.setPassword("1120");
		mail.setFrom("pacoweb@163.com");
		mail.setTo("sp42@qq.com");
		mail.setSubject("hihi你好");
		mail.setHTML_body(true);
		mail.setContent("dsfds放到沙发dfsfd<a href=\"http://qq.com\">fdsfds</a>");
		
		try(Sender sender = new Sender(mail)){
			boolean isOk = sender.sendMail();
			System.out.println(isOk);
			assertTrue(isOk);
		};
	}
}