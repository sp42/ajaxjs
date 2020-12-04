package com.ajaxjs.cms.service;

import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mvc.MvcRequest;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface ThirdPartyService {
	/**
	 * 发送邮件
	 * 
	 * @param mail 邮件
	 * @return 是否发送成功
	 */
	public boolean sendEmail(Mail mail);

	/**
	 * 发送短信
	 * 
	 * @param phoneNo  手机号码
	 * @param template 模板 id
	 * @param param    参数
	 * @return 是否发送成功
	 */
	public boolean sendSms(String phoneNo, String template, String param);

	public void uploadFile(MvcRequest request, UploadFileInfo info);

	public boolean deleteFile(String objectKey);
}
