package com.ajaxjs.app;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.Sender;
import com.ajaxjs.sms.AliyunSMS;
import com.ajaxjs.sms.SmsMessage;
import com.ajaxjs.util.ioc.Component;

/**
 * 默认的服务供应器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class DefaultServices implements ThirdPartyService {

	@Override
	public boolean sendEmail(Mail mail) {
		mail.setMailServer(ConfigService.getValueAsString("mailServer.server"));
		mail.setAccount(ConfigService.getValueAsString("mailServer.user"));
		mail.setPassword(ConfigService.getValueAsString("mailServer.password"));
		mail.setFrom(ConfigService.getValueAsString("mailServer.user"));

		return Sender.send(mail);
	}

	@Override
	public boolean sendSms(String phoneNo, String template, String param) {
		SmsMessage msg = new SmsMessage();
		msg.setPhoneNo(phoneNo);
		msg.setSignName(ConfigService.getValueAsString("sms.aliyun.signName"));
		msg.setTemplateCode(ConfigService.getValueAsString("sms.aliyun.templateCode"));
		msg.setTemplateParam(param);

		return AliyunSMS.send(msg);
	}
}
