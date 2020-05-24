package com.ajaxjs.user;

import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.app.ThirdPartyService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.SimpleSMSFilter;
import com.ajaxjs.sms.SmsMessage;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;

@Path("sendSMScode")
public class CmsUserController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(CmsUserController.class);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String sendSMScode(@NotNull @QueryParam("phoneNo") String phoneNo) {
		LOGGER.info("发送短信到::" + phoneNo);

		if (phoneNo.length() != 11) {
			return jsonNoOk(phoneNo + " 中国大陆手机号应为11位数");
		} else {
			if (UserUtil.isVaildPhone(phoneNo)) {
				return jsonNoOk(phoneNo + " 为非法手机号码");
			} else {
				int randomCode = (int) ((Math.random() * 9 + 1) * 100000);

				SmsMessage message = new SmsMessage();
				message.setPhoneNo(phoneNo);
				message.setSignName("我是zyjf");
				message.setTemplateParam(String.format("{\"code\":\"%s\"}", randomCode));

				ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);

				if (services.sendSms(phoneNo, "", "")) {
					LOGGER.info("发送手机 " + phoneNo + " 验证码成功");
					MvcRequest.getHttpServletRequest().getSession().setAttribute(SimpleSMSFilter.SMS_KEY_NAME, randomCode + "");
					
					return jsonOk("发送手机 " + phoneNo + " 验证码成功");
				} else {
					return jsonNoOk("发送手机 " + phoneNo + " 验证码失敗");
				}
			}
		}
	}

	@Override
	public UserService getService() {
		return null;
	}
}
