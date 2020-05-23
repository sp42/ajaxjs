package com.ajaxjs;

import java.io.IOException;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.net.mail.MailException;
import com.ajaxjs.net.mail.Sender;
import com.ajaxjs.sms.SmsMessage;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 默认的服务供应器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class DefaultServices extends ThirdServices {
	private static final LogHelper LOGGER = LogHelper.getLog(DefaultServices.class);

	{
		setSendSms((phoneNo, smsText) -> {
			SmsMessage msg = new SmsMessage();
			msg.setPhoneNo(phoneNo);
			msg.setSignName(ConfigService.getValueAsString("sms.aliyun.signName"));
			msg.setTemplateCode(ConfigService.getValueAsString("sms.aliyun.templateCode"));
			msg.setTemplateParam(String.format("{\"code\":\"%s\"}", 3444));

//			return AliyunSMS.send(msg);
		});

		setSendEmail(mail -> {
			mail.setMailServer(ConfigService.getValueAsString("mailServer.server"));
			mail.setAccount(ConfigService.getValueAsString("mailServer.user"));
			mail.setPassword(ConfigService.getValueAsString("mailServer.password"));

			try (Sender sender = new Sender(mail)) {
				return sender.sendMail();
			} catch (IOException | MailException e) {
				LOGGER.warning(e);

				return false;
			}
		});
	}
}
