/**
  * Copyright Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.cms.utils.sms;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ajaxjs.cms.controller.CommonController;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.util.logger.LogHelper;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里云短信
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean("AliyunSMSSender")
public class Aliyun implements SMS {
	private static final LogHelper LOGGER = LogHelper.getLog(CommonController.class);

	/**
	 * 产品名称:云通信短信API产品,开发者无需替换
	 */
	private static final String product = "Dysmsapi";

	/**
	 * 产品域名,开发者无需替换
	 */
	private static final String domain = "dysmsapi.aliyuncs.com";
 

	static {
		//可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
	}

	public static SendSmsResponse sendSms(Message message) {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		//必填:待发送手机号
		request.setPhoneNumbers(message.getPhoneNo());
		//必填:短信签名-可在短信控制台中找到
		request.setSignName(message.getSignName());
		//必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(message.getTemplateCode());
		//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(message.getTemplateParam());

		/*
		 * //选填-上行短信扩展码(无特殊需求用户请忽略此字段) request.setSmsUpExtendCode("90997");
		 * 
		 * //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者 request.setOutId("yourOutId");
		 */

		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			return sendSmsResponse;
		} catch (ClientException e) {
			e.printStackTrace();
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 查询已发送的明细，查看响应结果
	 * 
	 * @param bizId
	 *            流水号
	 * @return 响应结果
	 */
	public static QuerySendDetailsResponse querySendDetails(String bizId) {
		QuerySendDetailsResponse querySendDetailsResponse = null;

		// 组装请求对象
		QuerySendDetailsRequest request = new QuerySendDetailsRequest();
		request.setPhoneNumber("15000000000");// 必填-号码
		request.setBizId(bizId);// 可选-流水号
		request.setSendDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));// 必填-发送日期 支持30天内记录查询，格式yyyyMMdd
		request.setPageSize(10L);// 必填-页大小
		request.setCurrentPage(1L);// 必填-当前页码从1开始计数

		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			IAcsClient acsClient = new DefaultAcsClient(profile);
			querySendDetailsResponse = acsClient.getAcsResponse(request);
		} catch (ClientException e) {
			LOGGER.warning(e);
		}

		return querySendDetailsResponse;
	}

	@Override
	public boolean send(Message message) {
		SendSmsResponse response = sendSms(message);
		if(response == null)
			return false;
		
		return response.getCode().equals("OK");
	}
}
