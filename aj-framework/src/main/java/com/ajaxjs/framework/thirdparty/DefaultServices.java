/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.thirdparty;

import java.io.IOException;

import com.ajaxjs.cms.model.SmsMessage;
import com.ajaxjs.cms.service.ThirdPartyService;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.net.mail.Sender;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mvc.MvcRequest;

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

		return false; // todo
//		return AliyunSMS.send(msg);
	}

	@Override
	public void uploadFile(MvcRequest request, UploadFileInfo info) {
		NsoHttpUploader uploadRequest = new NsoHttpUploader(request, info);
		try {
			uploadRequest.upload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteFile(String objectKey) {
		return NsoHttpUpload.delete(objectKey);
	}
}
